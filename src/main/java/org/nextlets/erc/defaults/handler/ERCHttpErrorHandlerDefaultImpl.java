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
package org.nextlets.erc.defaults.handler;

import org.nextlets.erc.exception.ERCHttpResponseException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default handler for HTTP errors.
 * @author Y.Sh.
 */
public class ERCHttpErrorHandlerDefaultImpl implements ERCHttpErrorHandler {

    protected Logger log = LoggerFactory.getLogger(ERCHttpErrorHandler.class);

    @Override
    public void handleError(
            int statusCode,
            String reasonPhrase,
            byte[] responseBody)
                    throws ERCHttpResponseException {

        log.error("HTTP request failed, status code: {}, reason: {}", statusCode, reasonPhrase);
        log.error("Response body: {}", new String(responseBody));
        throw new ERCHttpResponseException(statusCode, reasonPhrase);
    }




}
