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
package org.nextlets.erc.exception;

public class ERCSerializationException extends ERCClientException {

    private static final long serialVersionUID = 1L;

    private String parameterName;
    private Object parameterValue;

    public ERCSerializationException(String parameterName, Object parameterValue) {
        super();
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public ERCSerializationException(String message, String parameterName, Object parameterValue) {
        super(message);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public ERCSerializationException(String message, Throwable cause, String parameterName, Object parameterValue) {
        super(message, cause);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Object getParameterValue() {
        return parameterValue;
    }


}
