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

import java.net.CookieManager;

import org.nextlets.erc.core.ERCCore;
import org.nextlets.erc.defaults.factory.ERCBasicHttpAuthFactoryImpl;
import org.nextlets.erc.defaults.factory.ERCDefaultFactoryImpl;
import org.nextlets.erc.http.ERCHttpAuthenticator;
import org.nextlets.erc.http.ERCHttpInvoker;

/**
 * Easy Rest Clients MegaFactory.
 *
 * @author Y.Sh.
 */
public abstract class ERCFactory {

    public abstract ERCConfiguration getConfiguration();
    public abstract CookieManager getCookieManager();
    protected abstract ERCHttpInvoker getHttpInvoker();
    protected abstract ERCHttpAuthenticator getAuthenticator();

	protected ERCHeaders autoHeaders;
	protected ERCParams autoParams;
    
	protected ERCFactory() {
		this.autoHeaders = new ERCHeaders();
		this.autoParams = new ERCParams();
	}
	
    public <T> T createRestClient(Class<T> clientInterfaceClass) {
        return ERCCore.getInstance(
                clientInterfaceClass,
                getConfiguration(),
                getCookieManager(),
                getHttpInvoker(),
                getAuthenticator(),
                autoHeaders,
                autoParams);
    }

    public static ERCFactory getInstance(String serviceUrl) {
        return new ERCDefaultFactoryImpl(serviceUrl);
    }

    public static ERCFactory getInstance(String serviceUrl, String user, String password) {
        return new ERCBasicHttpAuthFactoryImpl(serviceUrl, user, password);
    }

    public ERCParams getAutoParams() {
		return autoParams;
	}

    public ERCHeaders getAutoHeaders() {
		return autoHeaders;
	}

}
