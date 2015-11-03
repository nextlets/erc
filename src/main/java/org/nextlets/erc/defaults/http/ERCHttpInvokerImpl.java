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
package org.nextlets.erc.defaults.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.exception.ERCClientException;
import org.nextlets.erc.exception.ERCException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.nextlets.erc.http.ERCHttpInvoker;
import org.nextlets.erc.http.ERCHttpMethod;
import org.nextlets.erc.http.ERCHttpResponse;
import org.nextlets.erc.utils.ERCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Y.Sh.
 */
@SuppressWarnings("deprecation")
public class ERCHttpInvokerImpl implements ERCHttpInvoker {

    private Logger log = LoggerFactory.getLogger(ERCHttpInvokerImpl.class);

    /**
     * TODO: deprecated class is used for compatibility with Android HTTP client.
     */
    private static HttpClient client = new DefaultHttpClient();

    @Override
    public ERCHttpResponse invoke(
            ERCConfiguration configuration,
            ERCHttpErrorHandler errorHandler,
            String methodEndpoint,
            ERCHttpMethod method,
            String contentType,
            Map<String, String> params,
            Map<String, String> headers,

            CookieManager cookieManager) throws ERCException
    {

        HttpRequestBase req = getHttpRequest(method);

        addHeaders(req, headers);
        addProxy(configuration, req);
        URI uri = createUri(configuration, methodEndpoint, req, contentType, params);

        HttpContext ctx = new BasicHttpContext();
        CookieStore cookieStore = toCookieStore(uri, cookieManager);
        ctx.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        log.debug("Executing HTTP request...");
        HttpResponse httpResp;
        try {
            httpResp = client.execute(req, ctx);
        } catch (IOException ex) {
            throw new ERCClientException(ex);
        }

        int statusCode = httpResp.getStatusLine().getStatusCode();
        log.debug("HTTP status code: {}", statusCode);

        toCookieManager(uri, cookieStore, cookieManager);
        byte[] responseBody = getResponseBody(configuration, httpResp);
        checkResponseError(statusCode, errorHandler, httpResp.getStatusLine().getReasonPhrase(), responseBody);

        return makeResponse(statusCode, getContentType(httpResp), responseBody);
    }

    protected void addHeaders(HttpRequestBase req, Map<String, String> headers) {
        for (Entry<String,String>hdr : headers.entrySet()) {
            req.addHeader(hdr.getKey(), hdr.getValue());
        }
    }

    protected void addProxy(ERCConfiguration configuration, HttpRequestBase req) {
        if (configuration.getProxyUrl() != null && !configuration.getProxyUrl().isEmpty()) {
            log.debug("Proxy: {}", configuration.getProxyUrl());
            URI proxyUri;
            try {
                proxyUri = new URI(configuration.getProxyUrl());
            } catch (URISyntaxException ex) {
                throw new ERCClientException(ex);
            }
            HttpHost proxy = new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
            req.setConfig(RequestConfig.custom().setProxy(proxy).build());
        }

    }

    protected URI createUri(ERCConfiguration configuration, String methodEndpoint, HttpRequestBase req, String contentType, Map<String, String> params) {
        String suri;
        try {
            suri = ERCUtils.buildUrlAndParameters(configuration, methodEndpoint, req, contentType, params);
            log.debug("URI: {}", suri);
        } catch (UnsupportedEncodingException ex) {
            throw new ERCClientException(ex.getMessage(), ex);
        }
        URI uri = null;
        try {
            uri = new URI(suri);
            req.setURI(uri);
        } catch (URISyntaxException ex) {
            throw new ERCClientException("Could not make URI from: " + suri, ex);
        }
        return uri;
    }

    protected byte[] getResponseBody(ERCConfiguration configuration, HttpResponse httpResp) {
        log.debug("Getting response body...");
        HttpEntity entity = httpResp.getEntity();
        byte[] responseBody = new byte[0];
        try {
            responseBody = (entity == null ? responseBody : EntityUtils.toByteArray(entity));
        } catch (ParseException ex) {
            throw new ERCClientException(ex);
        } catch(IOException ex) {
            throw new ERCClientException(ex);
        }
        log.debug("Response body length: ", responseBody.length);
        return responseBody;
    }

    protected String getContentType(HttpResponse httpResp) {
        HttpEntity entity = httpResp.getEntity();
        return entity.getContentType() == null ? null : entity.getContentType().getValue();
    }

    protected void checkResponseError(int statusCode, ERCHttpErrorHandler errorHandler, String reasonPhrase, byte[] responseBody) {
        if (statusCode >= 400) {
            errorHandler.handleError(statusCode, reasonPhrase, responseBody);
        }
    }

    protected ERCHttpResponse makeResponse(int statusCode, String contentType, byte[] responseBody) {
        ERCHttpResponse res = new ERCHttpResponse();
        res.setStatusCode(statusCode);
        res.setContentType(contentType);
        res.setResponseBody(responseBody);
        return res;
    }

    private HttpRequestBase getHttpRequest(ERCHttpMethod httpMethod) throws ERCClientException {
        switch (httpMethod) {
            case GET:
                return new HttpGet();
            case POST:
                return new HttpPost();
            case PUT:
                return new HttpPut();
            case DELETE:
                return new HttpDelete();
            default:
                throw new ERCClientException("Unsupported HTTP method: " + httpMethod.name());
        }
    }

    private CookieStore toCookieStore(URI uri, CookieManager cm) {
        CookieStore cs = new BasicCookieStore();
        for(HttpCookie hc : cm.getCookieStore().getCookies()) {
            cs.addCookie(toCookie(uri, hc));
        }
        return cs;
    }

    private void toCookieManager(URI uri, CookieStore cs, CookieManager cm) {
        cm.getCookieStore().removeAll();
        for(Cookie c : cs.getCookies()) {
            cm.getCookieStore().add(uri, toHttpCookie(c));
        }
    }

    private HttpCookie toHttpCookie(Cookie cookie) {
        HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
        httpCookie.setComment(cookie.getComment());
        httpCookie.setDomain(cookie.getDomain());
        if (cookie.getExpiryDate() != null) {
            httpCookie.setMaxAge(cookie.getExpiryDate().getTime() / 1000);
        }
        httpCookie.setPath(cookie.getPath());
        httpCookie.setSecure(cookie.isSecure());
        httpCookie.setVersion(cookie.getVersion());
        return httpCookie;
    }

    private Cookie toCookie(URI uri, HttpCookie httpCookie) {
        SetCookie cookie = new BasicClientCookie(httpCookie.getName(), httpCookie.getValue());
        cookie.setComment(httpCookie.getComment());
        if (httpCookie.getDomain() != null) {
            cookie.setDomain(httpCookie.getDomain());
        } else {
            cookie.setDomain(uri.getHost());
        }
        if (httpCookie.getMaxAge() != -1) {
            cookie.setExpiryDate(new Date(httpCookie.getMaxAge() * 1000));
        }
        if (httpCookie.getPath() != null) {
            cookie.setPath(httpCookie.getPath());
        } else {
            cookie.setPath(uri.getPath());
        }
        cookie.setSecure(httpCookie.getSecure());
        cookie.setVersion(httpCookie.getVersion());
        return cookie;
    }
}
