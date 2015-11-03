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
package org.nextlets.erc.http;

import java.net.CookieManager;
import java.util.Map;

import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.exception.ERCException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;

/**
 * Invoker interface.
 *
 * @author Y.Sh.
 */
public interface ERCHttpInvoker {

    /**
     * Invokes HTTP service.
     * @param configuration - configuration
     * @param errorHandler - error handler
     * @param methodEndpoint - method endpoint
     * @param method - HTTP method
     * @param contentType - content type for case if serialized object should be sent in the request body
     * @param parameters - request parameters
     * @param headers - request headers
     * @param cookieManager - cookie manager
     */
    ERCHttpResponse invoke(
            ERCConfiguration configuration,
            ERCHttpErrorHandler errorHandler,
            String methodEndpoint,
            ERCHttpMethod method,
            String contentType,
            Map<String, String> parameters,
            Map<String, String> headers,
            CookieManager cookieManager) throws ERCException;
}
