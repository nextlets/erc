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
package org.nextlets.erc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nextlets.erc.defaults.handler.ERCHttpErrorHandlerDefaultImpl;
import org.nextlets.erc.defaults.handler.ERCHttpInterceptorDefaultImpl;
import org.nextlets.erc.defaults.handler.ERCResultDeserializerJsonImpl;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.nextlets.erc.handler.ERCHttpInterceptor;
import org.nextlets.erc.handler.ERCResultDeserializer;
import org.nextlets.erc.http.ERCHttpMethod;

/**
 * Annotation to define request endpoints.
 *
 * @author Y.Sh.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ERCEndpoint {

    /**
     * Method endpoint (for short - just endpoint)
     */
    public String endpoint();

    /**
     * Defines HTTP method (one of specified in ERCHttpMethod enum), ERCHttpMethod.GET by default.
     */
    public ERCHttpMethod method() default ERCHttpMethod.GET;

    /**
     * Defines class of result deserializer, DefaultERCResultDeserializer.class by default.
     */
    public Class<? extends ERCResultDeserializer<?>> deserializer() default ERCResultDeserializerJsonImpl.class;

    /**
     * Defines class of error handler, DefaultERCHttpErrorHandler.class by default.
     */
    public Class<? extends ERCHttpErrorHandler> errorHandler() default ERCHttpErrorHandlerDefaultImpl.class;

    /**
     * Defines class of HTTP call interceptor, DefaultERCHttpInterceptor.class by default.
     */
    public Class<? extends ERCHttpInterceptor<?>> httpInterceptor() default ERCHttpInterceptorDefaultImpl.class;
}
