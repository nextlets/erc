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
package org.nextlets.erc.defaults.config;

import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.ERCConstants;

/**
 *
 * @author Y.Sh.
 */
public class ERCConfigurationDefaultImpl implements ERCConfiguration {

    private String serviceUrl;
    private String dateFormat;
    private String charset;
    private String proxyUrl;

    protected ERCConfigurationDefaultImpl() {
    }

    public ERCConfigurationDefaultImpl(String serviceUrl) {
        setServiceUrl(serviceUrl);
        setDateFormat(ERCConstants.ISO8601_DATE_FORMAT);
        setCharset(ERCConstants.CHARSET);
        setProxyUrl(null);
    }

    @Override
    public String getServiceUrl() {
        return serviceUrl;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public String getProxyUrl() {
        return proxyUrl;
    }

    @Override
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    @Override
    public String toString() {
        return "ERCConfiguration [serviceUrl=" + serviceUrl + ", dateFormat=" + dateFormat + ", charset="
                + charset + ", proxyUrl=" + proxyUrl + "]";
    }

}
