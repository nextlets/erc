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
package org.nextlets.erc.cinfo;

import org.nextlets.erc.ERCEndpoint;
import org.nextlets.erc.ERCFactory;
import org.nextlets.erc.ERCParam;
import org.nextlets.erc.cinfo.caps.Caps;
import org.nextlets.erc.cinfo.curr.Curr;

public class CinfoTest {


    interface CapabilitiesService {
        @ERCEndpoint(endpoint="/capabilities")
        Caps getCapabilities();
    }

    interface CurrencyService {
        @ERCEndpoint(endpoint="/today/{provider}")
        Curr today(@ERCParam(name="provider") String provider, @ERCParam(name="relevance") String relevance);
    }

    public static void main(String[] args) throws Exception {
        ERCFactory fact = ERCFactory.getInstance("http://localhost:8080/cinfo", "test", "test");
        CapabilitiesService capser = fact.createRestClient(CapabilitiesService.class);
        Caps caps = capser.getCapabilities();
        fact.getConfiguration().setDateFormat(caps.getMeta().getDateFormat());

        CurrencyService curser = fact.createRestClient(CurrencyService.class);
        Curr curr = curser.today("NBRB", "USD");
        System.out.println(curr);

    }

}
