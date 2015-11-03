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
package org.nextlets.erc.jsonplaceholder;

import org.nextlets.erc.ERCEndpoint;
import org.nextlets.erc.ERCFactory;
import org.nextlets.erc.ERCParam;
import org.nextlets.erc.http.ERCHttpMethod;

public class JsonPlaceholderTest {

    static class Resource {
        int id;
        String title;
        String body;
        int userId;
        @Override
        public String toString() {
            return "Resource [id=" + id + ", userId=" + userId + ", title=" + title + ", body=" + body + "]";
        }
    }

    interface JsonPlaceholderService {
        @ERCEndpoint(endpoint="/posts/{id}")
        Resource getResource(@ERCParam(name="id") int id);

        @ERCEndpoint(endpoint="/posts", method=ERCHttpMethod.POST)
        Resource postResource(@ERCParam(name="userId") int userId, @ERCParam(name="title") String title, @ERCParam(name="body") String body);
    }

    public static void main(String[] args) {

        ERCFactory f = ERCFactory.getInstance("http://jsonplaceholder.typicode.com");
        JsonPlaceholderService serv = f.createRestClient(JsonPlaceholderService.class);

        Resource res1 = serv.getResource(1);
        System.out.println(res1);

        Resource res2 = serv.postResource(1,"1","11");
        System.out.println(res2);
    }
}
