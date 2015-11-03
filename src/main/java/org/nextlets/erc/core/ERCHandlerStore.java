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

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.nextlets.erc.ERCObject;
import org.nextlets.erc.ERClient;
import org.nextlets.erc.defaults.handler.ERCHttpErrorHandlerDefaultImpl;
import org.nextlets.erc.defaults.handler.ERCHttpInterceptorDefaultImpl;
import org.nextlets.erc.defaults.handler.ERCParameterSerializerJsonImpl;
import org.nextlets.erc.defaults.handler.ERCResultDeserializerJsonImpl;
import org.nextlets.erc.exception.ERCInstatiateException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.nextlets.erc.handler.ERCHttpInterceptor;
import org.nextlets.erc.handler.ERCParameterSerializer;
import org.nextlets.erc.handler.ERCResultDeserializer;
import org.nextlets.erc.utils.ERCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Store of handlers.
 * This store caches all handlers to minimize time losses for instantiating.
 *
 * @author Y.Sh.
 */
public class ERCHandlerStore {

    private static Map<Class<ERCParameterSerializer<?>>, ERCParameterSerializer<?>> serializers;
    private static Map<Class<ERCResultDeserializer<?>>, ERCResultDeserializer<?>> deserializers;
    private static Map<Class<ERCHttpErrorHandler>, ERCHttpErrorHandler> errorHandlers;
    private static Map<Class<ERCHttpInterceptor<?>>, ERCHttpInterceptor<?>> interceptors;

    private static Logger log = LoggerFactory.getLogger(ERCHandlerStore.class);

    static {
        serializers = new ConcurrentHashMap<Class<ERCParameterSerializer<?>>, ERCParameterSerializer<?>>();
        deserializers = new ConcurrentHashMap<Class<ERCResultDeserializer<?>>, ERCResultDeserializer<?>>();
        errorHandlers = new ConcurrentHashMap<Class<ERCHttpErrorHandler>, ERCHttpErrorHandler>();
        interceptors = new ConcurrentHashMap<Class<ERCHttpInterceptor<?>>, ERCHttpInterceptor<?>>();
    }

    @SuppressWarnings("unchecked")
    public static ERCParameterSerializer<?> getParameterSerializer(
            Class<?> clientInterfaceClass,
            Class<? extends ERCParameterSerializer<?>> methodHandlerClass,
            Object parameter) throws ERCInstatiateException {

        Class<? extends ERCParameterSerializer<?>> serializerClass = null;
        // 1. Highest priority: serializer is defined inside parameter class
        if (parameter != null) {
            ERCObject ercObject = ERCUtils.getClassAnnotation(parameter.getClass(), ERCObject.class);
            if (ercObject != null && ercObject.serializer() != ERCParameterSerializerJsonImpl.class) {
                serializerClass = ercObject.serializer();
            }
        }
        // 2. Medium priority: serializer is defined inside client interface class
        if (serializerClass == null) {
            ERClient erClient = ERCUtils.getClassAnnotation(clientInterfaceClass, ERClient.class);
            if (erClient != null) {
                serializerClass = getHandlerClass(erClient.serializer(), methodHandlerClass, ERCParameterSerializerJsonImpl.class);
            }
        }
        // 3. Lower priority: serializer by default
        if (serializerClass == null) {
            serializerClass = methodHandlerClass;
        }
        log.debug("Serializer {} for parameter class {}", serializerClass.getName(),
                parameter == null ? null : parameter.getClass().getName());
        ERCParameterSerializer<?> serializer = serializers.get(serializerClass);
        if (serializer == null) {
            serializer = instantiate(serializerClass);
            serializers.put((Class<ERCParameterSerializer<?>>) serializerClass, serializer);
        }
        return serializer;
    }

    @SuppressWarnings("unchecked")
    public static ERCResultDeserializer<?> getResultDeserializer(
            Class<?> clientInterfaceClass,
            Class<? extends ERCResultDeserializer<?>> methodHandlerClass,
            Class<?> returnType) throws ERCInstatiateException {

        Class<? extends ERCResultDeserializer<?>> deserializerClass = null;

        ERCObject ercObject = ERCUtils.getClassAnnotation(returnType, ERCObject.class);
        if (ercObject != null && ercObject.deserializer() != ERCResultDeserializerJsonImpl.class) {
            deserializerClass = ercObject.deserializer();
        }
        if (deserializerClass == null) {
            ERClient client = ERCUtils.getClassAnnotation(clientInterfaceClass, ERClient.class);
            if (client != null) {
                deserializerClass = getHandlerClass(client.deserializer(), methodHandlerClass, ERCResultDeserializerJsonImpl.class);
            }
        }
        if (deserializerClass == null) {
            deserializerClass = methodHandlerClass;
        }
        log.debug("Deserializer {} for return type {}", deserializerClass.getName(), returnType.getName());
        ERCResultDeserializer<?> deserializer = deserializers.get(deserializerClass);
        if (deserializer == null) {
            deserializer = instantiate(deserializerClass);
            deserializers.put((Class<ERCResultDeserializer<?>>) deserializerClass, deserializer);
        }
        return deserializer;
    }

    @SuppressWarnings("unchecked")
    public static ERCHttpErrorHandler getErrorHandler(
            Class<?> clientInterfaceClass,
            Class<? extends ERCHttpErrorHandler> methodHandlerClass) throws ERCInstatiateException {

        ERClient client = ERCUtils.getClassAnnotation(clientInterfaceClass, ERClient.class);
        if (client != null) {
            methodHandlerClass = getHandlerClass(client.errorHandler(), methodHandlerClass, ERCHttpErrorHandlerDefaultImpl.class);
        }

        ERCHttpErrorHandler errorHandler = errorHandlers.get(methodHandlerClass);
        if (errorHandler == null) {
            errorHandler = instantiate(methodHandlerClass);
            errorHandlers.put((Class<ERCHttpErrorHandler>) methodHandlerClass, errorHandler);
        }

        return errorHandler;
    }

    @SuppressWarnings("unchecked")
    public static ERCHttpInterceptor<?> getHttpInterceptor(
            Class<?> clientInterfaceClass,
            Class<? extends ERCHttpInterceptor<?>> methodHandlerClass) throws ERCInstatiateException {

        ERClient client = ERCUtils.getClassAnnotation(clientInterfaceClass, ERClient.class);
        if (client != null) {
            methodHandlerClass = getHandlerClass(client.httpInterceptor(), methodHandlerClass, ERCHttpInterceptorDefaultImpl.class);
        }

        ERCHttpInterceptor<?> interceptor = interceptors.get(methodHandlerClass);
        if (interceptor == null) {
            interceptor = instantiate(methodHandlerClass);
            interceptors.put((Class<ERCHttpInterceptor<?>>) methodHandlerClass, interceptor);
        }
        return interceptor;
    }

    private static <T> T instantiate(Class<T> clazz) throws ERCInstatiateException {
        try {
            log.debug("Instantiating handler {} ...", clazz);
            Class<?> enclosingClass = clazz.getEnclosingClass();
            if (enclosingClass != null) {
                // TODO: instance of inner class is created on new instance of outer class!
                // Values of fields of original outer class is not are not visible!
                // If outer class has not empty constructor this trick won't work!
                Object instanceOfEnclosingClass = enclosingClass.newInstance();
                Constructor<T> constructor = clazz.getConstructor(enclosingClass);
                return constructor.newInstance(instanceOfEnclosingClass);
            } else {
                return clazz.newInstance();
            }
        } catch (Exception e) {
            throw new ERCInstatiateException("Could not instantiate class " + clazz.getName(), e);
        }
    }

    private static <T> Class<? extends T> getHandlerClass(
            Class<? extends T> clientHandlerClass,
            Class<? extends T> methodHandlerClass,
            Class<? extends T> defaultHandlerClass) {
        if (methodHandlerClass == defaultHandlerClass) {
            if (clientHandlerClass != defaultHandlerClass) {
                methodHandlerClass = clientHandlerClass;
            }
        }
        return methodHandlerClass;
    }
}
