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

import java.io.IOException;
import java.util.Properties;

public class ERCConfigurationPropertiesImpl extends ERCConfigurationDefaultImpl {

    public static final String ERC_PROPERTIES_FILE = "erc.properties";

    public static final String SERVICE_URL_PROPERTY = "service.url";
    public static final String CHARSET_PROPERTY = "charset";
    public static final String DATE_FORMAT_PROPERTY = "date.format";
    public static final String PROXY_URL_PROPERTY = "proxy.url";

    public ERCConfigurationPropertiesImpl() throws IOException {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream(ERC_PROPERTIES_FILE));

        setServiceUrl(props.getProperty(SERVICE_URL_PROPERTY));
        setCharset(props.getProperty(CHARSET_PROPERTY));
        setDateFormat(props.getProperty(DATE_FORMAT_PROPERTY));
        setProxyUrl(props.getProperty(PROXY_URL_PROPERTY));
    }

}
