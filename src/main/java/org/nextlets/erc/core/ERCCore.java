/**
 * Copyright (c) 2015 nextlets.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * The Software shall be used for Good, not Evil.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.nextlets.erc.core;

import static org.nextlets.erc.core.ERCHandlerStore.getErrorHandler;
import static org.nextlets.erc.core.ERCHandlerStore.getHttpInterceptor;
import static org.nextlets.erc.core.ERCHandlerStore.getParameterSerializer;
import static org.nextlets.erc.core.ERCHandlerStore.getResultDeserializer;
import static org.nextlets.erc.utils.ERCUtils.isJavaLangObjectMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.ERCEndpoint;
import org.nextlets.erc.ERCHeader;
import org.nextlets.erc.ERCHeaders;
import org.nextlets.erc.ERCParam;
import org.nextlets.erc.ERCParams;
import org.nextlets.erc.ERClient;
import org.nextlets.erc.exception.ERCClientException;
import org.nextlets.erc.exception.ERCException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.nextlets.erc.handler.ERCHttpInterceptor;
import org.nextlets.erc.handler.ERCParameterSerializer;
import org.nextlets.erc.handler.ERCResultDeserializer;
import org.nextlets.erc.http.ERCHttpAuthenticator;
import org.nextlets.erc.http.ERCHttpInvoker;
import org.nextlets.erc.http.ERCHttpResponse;
import org.nextlets.erc.utils.ERCStopWatch;
import org.nextlets.erc.utils.ERCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ERC Core.
 * Performs all dirty jobs.
 *
 * @author Y.Sh.
 */
public class ERCCore implements InvocationHandler {

    private Class<?> clientInterfaceClass;
    private ERCConfiguration configuration;
    private CookieManager cookieManager;
    private ERCHttpInvoker httpInvoker;
    private ERCHttpAuthenticator authenticator;
	private ERCHeaders autoHeaders;
	private ERCParams autoParams;

    private Logger log = LoggerFactory.getLogger(ERCCore.class);

    private ERCCore(
            Class<?> clientInterfaceClass,
            ERCConfiguration configuration,
            CookieManager cookieManager,
            ERCHttpInvoker httpInvoker,
            ERCHttpAuthenticator authenticator,
            ERCHeaders autoHeaders,
            ERCParams autoParams) {
        super();
        this.clientInterfaceClass = clientInterfaceClass;
        this.configuration = configuration;
        this.cookieManager = cookieManager;
        this.httpInvoker = httpInvoker;
        this.authenticator = authenticator;
        this.autoHeaders = autoHeaders;
        this.autoParams = autoParams;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(
            Class<T> clientInterfaceClass,
            ERCConfiguration configuration,
            CookieManager cookieManager,
            ERCHttpInvoker httpInvoker,
            ERCHttpAuthenticator authenticator,
            ERCHeaders autoHeaders,
            ERCParams autoParams) {

        return (T) Proxy.newProxyInstance(
                clientInterfaceClass.getClassLoader(),
                new Class[] { clientInterfaceClass },
                new ERCCore(clientInterfaceClass, configuration, cookieManager, httpInvoker, authenticator, autoHeaders, autoParams));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // looking for ECR method annotation
        ERCEndpoint ercEndpoint = method.getAnnotation(ERCEndpoint.class);

        if (ercEndpoint != null) {
            return invokeEndpoint(ercEndpoint, proxy, method, args);
        } else {
            if (isJavaLangObjectMethod(method)) {
                return method.invoke(this, args);
            } else {
                throw new ERCClientException("Method " + this.clientInterfaceClass.getName() + "." + method.getName()
                        + "(...) is not annotated with @ERCEndpoint.");
            }
        }
    }

    protected class InvocationParameters {
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        CookieManager methodCookieManager = cookieManager;
        String contentType;
    }

    private static final String SW_AFTER = "after";
    private static final String SW_DESERIALIZATION = "deserialization";
    private static final String SW_ENDPOINT = "call endpoint";
    private static final String SW_AUTH = "auth";
    private static final String SW_BEFORE = "before";
    private static final String SW_PREPARATION = "preparation";

    protected Object invokeEndpoint(
            ERCEndpoint ercEndpoint,
            Object proxy,
            Method method, Object[] args) throws Throwable {

        log.debug("Invoking endpoint {}/{} ...", configuration.getServiceUrl(), ercEndpoint.endpoint());

        ERCStopWatch sw = new ERCStopWatch(SW_ENDPOINT);

        sw.start(SW_PREPARATION);
        InvocationParameters iparams = buildInvocationParameters(ercEndpoint, proxy, method, args);
        sw.stop(SW_PREPARATION);

        sw.start(SW_AUTH);
        invokeAuthenticator(ercEndpoint, iparams);
        sw.stop(SW_AUTH);

        sw.start(SW_BEFORE);
        invokeInterceptorBefore(ercEndpoint, iparams, method.getReturnType());
        sw.stop(SW_BEFORE);

        sw.start(SW_ENDPOINT);
        ERCHttpResponse reswrp = invokeHttpMethod(ercEndpoint, iparams, method.getReturnType());
        sw.stop(SW_ENDPOINT);

        sw.start(SW_DESERIALIZATION);
        Object result = deserializeResponse(ercEndpoint, reswrp, method.getReturnType());
        sw.stop(SW_DESERIALIZATION);

        sw.start(SW_AFTER);
        invokeInterceptorAfter(ercEndpoint, reswrp, result);
        sw.stop(SW_AFTER);

        log.debug("Endpoint invocation complete. {}", sw);
        return result;
    }

    protected Map<String, String> getAnnotatedHeaders(Method method) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.putAll(autoHeaders);
        ERClient client = ERCUtils.getClassAnnotation(clientInterfaceClass, ERClient.class);
        if (client != null) {
            for(ERCHeader hdr : client.headers()) {
                if (!hdr.value().isEmpty()) {
                    headers.put(hdr.name(), hdr.value());
                }
            }
        }
        client = method.getAnnotation(ERClient.class);
        if (client != null) {
            for(ERCHeader hdr : client.headers()) {
                headers.put(hdr.name(), hdr.value());
            }
        }
        return headers;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected InvocationParameters buildInvocationParameters(
            ERCEndpoint ercEndpoint,
            Object proxy,
            Method method,
            Object[] args) throws Exception {

        log.debug("Serializing method parameters...");
        Map<String, String> parameters = serializeParametersMap(autoParams);
        Map<String, String> headers = getAnnotatedHeaders(method);

        Annotation annotations[][] = method.getParameterAnnotations();

        CookieManager methodCookieManager = cookieManager;

        String contentType = null;

        for (int i = 0; i < annotations.length; i++) {

            Annotation[] anns = annotations[i];
            for (Annotation ann : anns) {

                // request parameter
                if (ann instanceof ERCParam) {
                    ERCParam ercp = (ERCParam) ann;
                    if (ercp.required() && args[i] == null) {
                        throw new ERCClientException(
                                "Method " + this.clientInterfaceClass.getName() + "." + method.getName() +
                                "(...): parameter " + ercp.name() + " is null but declared as requred.");
                    }
                    if (args[i] != null) {
                        ERCParameterSerializer serializer = getParameterSerializer(clientInterfaceClass,
                                ercp.serializer(), args[i]);
                        String serPar = serializer.serialize(configuration, ercp.name(), args[i]);
                        if (ercp.name().equals(ERCParam.REQUEST_BODY)) {
                            contentType = serializer.getContentType();
                        }
                        parameters.put(ercp.name(), serPar);
                        log.debug("Added annotated parameter {}={}", ercp.name(), serPar);
                    }
                }

                // request header
                if (ann instanceof ERCHeader) {
                    if (args[i] != null) {
                        ERCHeader hdr = (ERCHeader) ann;
                        headers.put(hdr.name(), args[i].toString());
                        log.debug("Added annotated header {}={}", hdr.name(), args[i].toString());
                    }
                }
            }

            // parameters as map
            if (args[i] instanceof ERCParams) {
                ERCParams pars = (ERCParams) args[i];
                Map<String, String>tmpMap = serializeParametersMap(pars);
                parameters.putAll(tmpMap);
                log.debug("Added parameters map {}.", tmpMap);
            }

            // headers as map
            if (args[i] instanceof ERCHeaders) {
                ERCHeaders hdrs = (ERCHeaders) args[i];
                headers.putAll(hdrs);
                log.debug("Added headers map {}.", hdrs.toString());
            }

            // Cookie manager as method parameter
            if (args[i] instanceof CookieManager) {
                methodCookieManager = (CookieManager) args[i];
                log.debug("Default Cookie Manager overrided with {}.", methodCookieManager);
            }

            // Cookie as method parameter
            if (args[i] instanceof HttpCookie) {
                methodCookieManager.getCookieStore().add(new URI(configuration.getServiceUrl()),
                        (HttpCookie) args[i]);
                log.debug("Added cookie {}.", args[i]);
            }
        }

        InvocationParameters result = new InvocationParameters();
        result.parameters = parameters;
        result.headers = headers;
        result.methodCookieManager = methodCookieManager;
        result.contentType = contentType;

        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<String, String> serializeParametersMap(ERCParams pars) throws NoSuchMethodException, SecurityException {
        Class<? extends ERCParameterSerializer<?>> defaultSerializerClass =
                (Class<? extends ERCParameterSerializer<?>>) ERClient.class.getMethod(
                        ERClient.SERIALIZER_METHOD, (Class[]) null).getDefaultValue();
        Map<String, String> resMap = new HashMap<String, String>();
        for (Entry<String, Object> ent : pars.entrySet()) {
            String paramName = ent.getKey();
            Object paramValue = ent.getValue();
            ERCParameterSerializer serializer = getParameterSerializer(clientInterfaceClass,
                    defaultSerializerClass, paramValue);
            String serPar = serializer.serialize(configuration, paramName, paramValue);
            resMap.put(ent.getKey(), serPar);
        }
        return resMap;

    }
    
    protected ERCHttpResponse invokeHttpMethod(
            ERCEndpoint ercEndpoint,
            InvocationParameters iparams,
            Class<?>returnType) throws ERCException {

        ERCHttpErrorHandler errorHandler = getErrorHandler(clientInterfaceClass, ercEndpoint.errorHandler());
        log.debug("Performing HTTP request...");

        ERCHttpResponse reswrp = httpInvoker.invoke(configuration, errorHandler, ercEndpoint.endpoint(),
                ercEndpoint.method(), iparams.contentType, iparams.parameters, iparams.headers, iparams.methodCookieManager);

        log.debug("HTTP request performed, status {}.", reswrp.getStatusCode());

        return reswrp;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object deserializeResponse(ERCEndpoint ercEndpoint, ERCHttpResponse reswrp, Class<?>returnType) throws ERCException {

        Object result = null;
        if ((Class<?>) returnType != void.class && (Class<?>) returnType != Void.class) {
            log.debug("Deserializing response...");

            ERCResultDeserializer<?> deserializer = getResultDeserializer(clientInterfaceClass,
                    ercEndpoint.deserializer(), returnType);

            result = deserializer.deserealize(configuration, reswrp.getStatusCode(), reswrp.getContentType(),
                    reswrp.getResponseBody(), (Class) returnType);

            log.debug("Response deserialized.");
        }

        return result;
    }

    protected void invokeAuthenticator(
            ERCEndpoint ercEndpoint,
            InvocationParameters iparams) {
        if (authenticator != null) {
            authenticator.authenticate(configuration, ercEndpoint.endpoint(), ercEndpoint.method(),
                    iparams.parameters, iparams.headers, iparams.methodCookieManager);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void invokeInterceptorBefore(
            ERCEndpoint ercEndpoint,
            InvocationParameters iparams,
            Class<?>returnType) throws ERCClientException {

        ERCHttpInterceptor httpInterceptor = getHttpInterceptor(clientInterfaceClass, ercEndpoint.httpInterceptor());

        log.debug("Invoking HTTP interceptor (before) {} ...", httpInterceptor.getClass().getName());

        httpInterceptor.before(configuration, ercEndpoint.endpoint(), ercEndpoint.method(),
                iparams.parameters, iparams.headers, iparams.methodCookieManager, returnType);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void invokeInterceptorAfter(
            ERCEndpoint ercEndpoint,
            ERCHttpResponse reswrp,
            Object result) throws ERCClientException {

        ERCHttpInterceptor httpInterceptor = getHttpInterceptor(clientInterfaceClass, ercEndpoint.httpInterceptor());

        log.debug("Invoking HTTP interceptor (after) {} ...", httpInterceptor.getClass().getName());

        httpInterceptor.after(configuration, reswrp.getStatusCode(), reswrp.getContentType(), result);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "(" + clientInterfaceClass.getName() + ")@" + Integer.toHexString(this.hashCode());
    }

}
