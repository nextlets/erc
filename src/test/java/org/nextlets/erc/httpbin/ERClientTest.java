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
package org.nextlets.erc.httpbin;

import java.net.CookieManager;
import java.util.Date;

import org.nextlets.erc.ERCEndpoint;
import org.nextlets.erc.ERCFactory;
import org.nextlets.erc.ERCHeader;
import org.nextlets.erc.ERCParam;
import org.nextlets.erc.http.ERCHttpMethod;

interface Q {
    @ERCEndpoint(method=ERCHttpMethod.GET, endpoint="get")
    HttpBin getDate(
            @ERCParam(name = "oppa") Date a,
            @ERCParam(name = "io", serializer=ParSerPar.class) int i, @ERCHeader(name="app-zzz") int j, CookieManager cm, @ERCParam(name = "aaa") TestObj aaa);
}

interface ArinWhois {
    @ERCEndpoint(method=ERCHttpMethod.GET, endpoint="")
    String whois(@ERCParam(name="ip") String ip);
}

public class ERClientTest {

    public static void main(String[] args) throws Exception {

        ERCFactory f = ERCFactory.getInstance("https://httpbin.org");
//        Q q = f.createRestClient(Q.class);
//
//        CookieManager cm = new CookieManager ();
//        cm.getCookieStore().add(new URI(f.getConfiguration().getServiceUrl()), new HttpCookie("11111", "22222"));
//        cm.getCookieStore().add(new URI(f.getConfiguration().getServiceUrl()), new HttpCookie("AAAAA", "BBBBB"));
//
//        HttpBin s =
//        		q.getDate(new Date(), 10, 12, cm, new TestObj());
//
//        System.out.println(s);


        f = ERCFactory.getInstance("http://whois.arin.net/rest/nets;q={ip}?showDetails=true&showARIN=false&showNonArinTopLevelNet=false&ext=netref2");
        ArinWhois w = f.createRestClient(ArinWhois.class);

        System.out.println(w.whois("74.93.6.188"));

    }

}
