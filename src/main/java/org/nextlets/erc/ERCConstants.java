/**
 * Copyright (c) 2015 nextlets.net
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
package org.nextlets.erc;

public interface ERCConstants {

    /**
     * Default charset: UTF-8
     */
    static final String CHARSET = "UTF-8";

    /**
     * Default date format: yyyy-MM-dd'T'HH:mm:ss.SSS+0000
     */
    static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS+0000";

    /**
     * HTTP header
     */
    static final String HDR_ACCEPT = "Accept";

    /**
     * HTTP header
     */
    static final String HDR_CONTENT_TYPE = "Content-Type";

    /**
     * Content type.
     */
    static final String JSON_CONTENT_TYPE = "application/json";

    /**
     * Text content type.
     */
    static final String TEXT_CONTENT_TYPE = "text/";

}
