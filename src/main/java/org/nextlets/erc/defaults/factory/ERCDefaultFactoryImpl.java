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
package org.nextlets.erc.defaults.factory;

import java.net.CookieManager;

import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.ERCFactory;
import org.nextlets.erc.defaults.config.ERCConfigurationDefaultImpl;
import org.nextlets.erc.defaults.http.ERCHttpInvokerImpl;
import org.nextlets.erc.http.ERCHttpAuthenticator;
import org.nextlets.erc.http.ERCHttpInvoker;

/**
 * Easy Rest Clients Factory.
 *
 * @author Y.Sh.
 */
public class ERCDefaultFactoryImpl extends ERCFactory {

	protected ERCConfiguration configuration;
	protected CookieManager cookieManager;
	protected ERCHttpInvoker httpInvoker;
	protected ERCHttpAuthenticator authenticator;

    public ERCDefaultFactoryImpl(String serviceUrl) {
    	super();
        this.configuration = createConfiguration(serviceUrl);
        this.cookieManager = createCookieManager();
        this.httpInvoker = createHttpInvoker();
        this.authenticator = createHttpAuthenticator();
    }

    protected ERCConfiguration createConfiguration(String serviceUrl) {
        return new ERCConfigurationDefaultImpl(serviceUrl);
    }

    protected CookieManager createCookieManager() {
        return new CookieManager();
    }

    protected ERCHttpInvoker createHttpInvoker() {
        return new ERCHttpInvokerImpl();
    }

    protected ERCHttpAuthenticator createHttpAuthenticator() {
        return null;
    }

	@Override
    public ERCConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public CookieManager getCookieManager() {
        return cookieManager;
    }

    @Override
    public ERCHttpInvoker getHttpInvoker() {
        return httpInvoker;
    }

    @Override
    public ERCHttpAuthenticator getAuthenticator() {
        return authenticator;
    }

}
