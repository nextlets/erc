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

import java.net.CookieManager;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.nextlets.erc.defaults.factory.ERCDefaultFactoryImpl;
import org.nextlets.erc.defaults.handler.ERCParameterSerializerJsonImpl;
import org.nextlets.erc.exception.ERCException;
import org.nextlets.erc.exception.ERCSerializationException;
import org.nextlets.erc.handler.ERCHttpErrorHandler;
import org.nextlets.erc.http.ERCHttpInvoker;
import org.nextlets.erc.http.ERCHttpMethod;
import org.nextlets.erc.http.ERCHttpResponse;

public class ParametersSerializersTests {

	private ERCFactory factory;
	private static int parameterSerializerCalled, clientInterfcateSerializerCalled, objectSerializerCalled;

	private void resetCounters() {
		parameterSerializerCalled = clientInterfcateSerializerCalled = objectSerializerCalled = 0;
	}

	@Before
	public void init() {
		resetCounters();
		factory = new ERCDefaultFactoryImpl("/1/2/3") {
	        @Override
	        protected ERCHttpInvoker createHttpInvoker() {
	            return new ERCHttpInvoker() {
	                @Override
	                public ERCHttpResponse invoke(ERCConfiguration configuration, ERCHttpErrorHandler errorHandler,
	                        String methodEndpoint, ERCHttpMethod method, String contentType, Map<String, String> parameters,
	                        Map<String, String> headers, CookieManager cookieManager) throws ERCException {
	                    return new ERCHttpResponse();
	                }
	            };
	        }
		};
	}

	public class ParameterSerializer extends ERCParameterSerializerJsonImpl {
		@Override
		public String serialize(ERCConfiguration configuration, String parameterName, Object parameterValue)
				throws ERCSerializationException {
			ParametersSerializersTests.parameterSerializerCalled++;
			return super.serialize(configuration, parameterName, parameterValue);
		}
	}

	public class ClientInterfcateSerializer extends ERCParameterSerializerJsonImpl {
		@Override
		public String serialize(ERCConfiguration configuration, String parameterName, Object parameterValue)
				throws ERCSerializationException {
			ParametersSerializersTests.clientInterfcateSerializerCalled++;
			return super.serialize(configuration, parameterName, parameterValue);
		}
	}


	public class ObjectSerializer extends ERCParameterSerializerJsonImpl {
		@Override
		public String serialize(ERCConfiguration configuration, String parameterName, Object parameterValue)
				throws ERCSerializationException {
			ParametersSerializersTests.objectSerializerCalled++;
			return super.serialize(configuration, parameterName, parameterValue);
		}
	}

	@ERCObject(serializer = ObjectSerializer.class)
	class TestObject {
	}

	interface TestClientForParameterSerializer {
		@ERCEndpoint(endpoint = "test")
		void test0();

		@ERCEndpoint(endpoint = "test")
		void test1(@ERCParam(name = "param") String s);

		@ERCEndpoint(endpoint = "test")
		void test2(@ERCParam(name = "param", serializer = ParameterSerializer.class) String s);
	}

	interface TestClientForObjectSerializer {
		@ERCEndpoint(endpoint = "test")
		void test0();

		@ERCEndpoint(endpoint = "test")
		void test1(@ERCParam(name = "param") TestObject to);

		@ERCEndpoint(endpoint = "test")
		void test2(@ERCParam(name = "param", serializer = ParameterSerializer.class) TestObject to);
	}

    @ERClient(serializer = ClientInterfcateSerializer.class,
            headers={@ERCHeader(name="AAA", value="BBB"), @ERCHeader(name="Accept", value="text/xml")})
	interface TestClientForClientInterfcateSerializerParent {
		@ERCEndpoint(endpoint = "test")
		void test0();

        @ERCEndpoint(endpoint = "test")
        void test1(@ERCParam(name = "param") String s);
	}

	interface TestClientForClientInterfcateSerializer extends TestClientForClientInterfcateSerializerParent {
        @ERCEndpoint(endpoint = "test")
        void test2(@ERCParam(name = "param", serializer = ParameterSerializer.class) String s);

        @ERCEndpoint(endpoint = "test")
        void test3(@ERCParam(name = "param") TestObject to);

        @ERCEndpoint(endpoint = "test")
        void test4(@ERCParam(name = "param", serializer = ParameterSerializer.class) TestObject to);
	}

	@Test
	public void testClientForParameterSerializer() {
		TestClientForParameterSerializer client = factory.createRestClient(TestClientForParameterSerializer.class);

		resetCounters();
		client.test0();
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);

		resetCounters();
		client.test1("Hi!");
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);

		resetCounters();
		client.test2("Hi!");
		assertEquals(1, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);
	}

	@Test
	public void testClientForObjectSerializer() {
		TestClientForObjectSerializer client = factory.createRestClient(TestClientForObjectSerializer.class);

		resetCounters();
		client.test0();
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);

		resetCounters();
		client.test1(new TestObject());
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(1, objectSerializerCalled);

		resetCounters();
		client.test2(new TestObject());
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(1, objectSerializerCalled);
	}

	@Test
	public void testClientForClientInterfcateSerializer() {
		TestClientForClientInterfcateSerializer client = factory
				.createRestClient(TestClientForClientInterfcateSerializer.class);

		resetCounters();
		client.test0();
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);

		resetCounters();
		client.test1("Hi!");
		assertEquals(0, parameterSerializerCalled);
		assertEquals(1, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);

		resetCounters();
		client.test2("Hi!");
		assertEquals(1, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(0, objectSerializerCalled);

		resetCounters();
		client.test3(new TestObject());
		assertEquals(0, parameterSerializerCalled);
		assertEquals(0, clientInterfcateSerializerCalled);
		assertEquals(1, objectSerializerCalled);
	}

}
