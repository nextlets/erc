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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.CookieManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.nextlets.erc.defaults.factory.ERCDefaultFactoryImpl;
import org.nextlets.erc.exception.ERCException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.nextlets.erc.http.ERCHttpInvoker;
import org.nextlets.erc.http.ERCHttpMethod;
import org.nextlets.erc.http.ERCHttpResponse;

import com.google.gson.annotations.SerializedName;

public class ParametersSerializationTests {

	Map<String, String> params;

	private ERCFactory factory;

	class TestObject {
		int i = 1;
		@SerializedName("str")
		String s = "HELLO!";
	}

	interface TestClient {
		@ERCEndpoint(endpoint="test")
		void testNumbers(@ERCParam(name="short")short s, @ERCParam(name="int")int i, @ERCParam(name="long")long l, @ERCParam(name="double")double d, @ERCParam(name="float")float f);

		@ERCEndpoint(endpoint="test")
		void testNumbers(@ERCParam(name="short")Short s, @ERCParam(name="int")Integer i, @ERCParam(name="long")Long l, @ERCParam(name="double")Double d, @ERCParam(name="float")Float f);

		@ERCEndpoint(endpoint="test")
		void testBoolean(@ERCParam(name="bool")boolean b);

		@ERCEndpoint(endpoint="test")
		void testBoolean(@ERCParam(name="bool")Boolean b);

		@ERCEndpoint(endpoint="test")
		void testString(@ERCParam(name="str")String s);

		@ERCEndpoint(endpoint="test")
		void testDate(@ERCParam(name="date")Date d);

		@ERCEndpoint(endpoint="test")
		void testObject(@ERCParam(name="obj")TestObject to);

	}

	@Before
	public void init() {
		factory = new ERCDefaultFactoryImpl("/1/2/3") {
	        @Override
	        protected ERCHttpInvoker createHttpInvoker() {
	            return new ERCHttpInvoker() {
	                @Override
	                public ERCHttpResponse invoke(ERCConfiguration configuration, ERCHttpErrorHandler errorHandler,
	                        String methodEndpoint, ERCHttpMethod method, String contentType, Map<String, String> parameters,
	                        Map<String, String> headers, CookieManager cookieManager) throws ERCException {
	                    params = parameters;
	                    return new ERCHttpResponse();
	                }
	            };
	        }
		};
	}

	@Test
	public void testNumbers() {
		TestClient client = factory.createRestClient(TestClient.class);
		client.testNumbers((short)0, 1, 2L, 3.3, (float)4.4);
		assertEquals(0, Short.parseShort(params.get("short")));
		assertEquals(1, Integer.parseInt(params.get("int")));
		assertEquals(2, Long.parseLong(params.get("long")));
		assertEquals(3.3, Double.parseDouble(params.get("double")), 1);
		assertEquals(4.4, Float.parseFloat(params.get("float")), 1);

		client.testNumbers(new Short((short)0), new Integer(1), new Long(2L), new Double(3.3), new Float(4.4));
		assertEquals(new Short((short)0), Short.valueOf(params.get("short")));
		assertEquals(new Integer(1), Integer.valueOf(params.get("int")));
		assertEquals(new Long(2), Long.valueOf(params.get("long")));
		assertEquals(new Double(3.3), Double.valueOf(params.get("double")));
		assertEquals(new Float(4.4), Float.valueOf(params.get("float")));

	}

	@Test
	public void testBoolean() {
		TestClient client = factory.createRestClient(TestClient.class);

		client.testBoolean(true);
		assertTrue(Boolean.parseBoolean(params.get("bool")));
		client.testBoolean(false);
		assertTrue(!Boolean.parseBoolean(params.get("bool")));

		client.testBoolean(Boolean.TRUE);
		assertEquals(Boolean.TRUE, Boolean.valueOf(params.get("bool")));
		client.testBoolean(Boolean.FALSE);
		assertEquals(Boolean.FALSE, Boolean.valueOf(params.get("bool")));
	}

	@Test
	public void testString() {
		TestClient client = factory.createRestClient(TestClient.class);
		client.testString("HELLO!");
		assertEquals("HELLO!", params.get("str"));
	}

	@Test
	public void testDate() {
		Date d = new Date();
		TestClient client = factory.createRestClient(TestClient.class);
		client.testDate(d);
		SimpleDateFormat sdf = new SimpleDateFormat(factory.getConfiguration().getDateFormat());
		assertEquals(sdf.format(d), params.get("date"));
	}

	@Test
	public void testObject() {
		TestClient client = factory.createRestClient(TestClient.class);
		client.testObject(new TestObject());
		assertEquals("{\"i\":1,\"str\":\"HELLO!\"}", params.get("obj"));
	}

}
