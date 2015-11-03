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

/**
 * <h3>HTTP header as annotated parameter.</h3>
 * <p>
 * Annotated HTTP headers are useful in cases you wish to clearly define them in your service interface.
 * It is a practice which can help you to design your service interfaces more readable and understandable.
 * </p>
 * Example:
 * <pre>
 * {@literal @}ERClient
 * interface MyService {
 * 	{@literal @}ERCEndpoint(method=ERCHttpMethod.GET, endpoint="/string")
 * 	String getString({@literal @}ERCHeader(name="My-Header") mhdr);
 * }
 *
 * ERCFactory factory; // it is already initialized
 * MyService myService = factory.createRestClient(MyService.class);
 * ...
 *
 * String result = MyService.getString("my header value");
 * </pre>
 * HTTP request will contain header:
 * <pre>
 * My-Header: my header value
 * </pre>
 * <p>
 * However, if number of headers which you wish to use is too large, or, set of them is not clear beforehand,
 * maybe it is make sense to use map of headers instead.
 * </p>
 * <p>
 * You can use approaches with annotated headers and with map of headers together, but, in any case,
 * you need to care about header names in annotated parameters and map of headers yourself.
 * </p>
 *
 * @see org.nextlets.erc.ERCHeaders
 *
 * @author Y.Sh.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ERCHeader {

    /**
     * Defines header name
     */
    public String name();

    /**
     * Header value
     */
    public String value() default "";

}
