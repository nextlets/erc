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

import java.util.HashMap;

/**
 * <h3>HTTP headers.</h3>
 * <p>
 * Sometimes, instead of use {@literal @}ERCHeader annotated parameter, it is more comfortable
 * to use map of headers, especially in cases when set of headers is not defined in advance or
 * number of headers is too large (and number of method parameters, accordingly, also).
 * </p>
 * 
 * <p>
 * This class is used to send HTTP headers in the HTTP request, just add headers what you need
 * to this map and pass it as one of parameters of your method, for example:
 * </p>
 * <pre>
 * {@literal @}ERClient
 * interface MyService {
 * 	{@literal @}ERCEndpoint(method=ERCHttpMethod.GET, endpoint="/string")
 * 	String getString(ERCHeaders headers);
 * }
 * 
 * ERCFactory factory; // it is already initialized 
 * MyService myService = factory.createRestClient(MyService.class); 
 * ...
 * 
 * ERCHeaders headers = new ERCHeaders();
 * headers.put("My-Header-One", "my header one value");
 * headers.put("My-Header-Two", "my header two value");
 * String result = MyService.getString(headers); 
 * </pre>
 * <p>
 * HTTP request will contain headers:
 * <pre>
 * My-Header-One: my header one value
 * My-Header-Two: my header two value
 * </pre>
 * You can use such parameter in mix with other parameters, in this case an order of parameters does not matter,
 * for example, the following two examples are identical from point of view of ERC:<br>
 * </p>
 * <b>1)</b>
 * <pre>
 * {@literal @}ERClient
 * interface MyService {
 * 	{@literal @}ERCEndpoint(method=ERCHttpMethod.GET, endpoint="/echo")
 * 	String echo(ERCHeaders headers, {@literal @}ERCParam(name = "echo") String echo);
 * }
 * </pre>
 * <b>2)</b>
 * <pre>
 * {@literal @}ERClient
 * interface MyService {
 * 	{@literal @}ERCEndpoint(method=ERCHttpMethod.GET, endpoint="/echo")
 * 	String echo({@literal @}ERCParam(name = "echo") String echo, ERCHeaders headers);
 * }
 * </pre>
 * <p>
 * It is possible to define more then one set of headers in one method (some parameters with this type),
 * but, it is unlikely to be useful on a practice.
 * </p>
 * @see org.nextlets.erc.ERCHeader
 *
 * @author Y.Sh.
 */
public class ERCHeaders extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;

    public void addHeader(String headerName, String headerValue) {
        put(headerName, headerValue);
    }

    public String getHeader(String headerName) {
        return get(headerName);
    }

}
