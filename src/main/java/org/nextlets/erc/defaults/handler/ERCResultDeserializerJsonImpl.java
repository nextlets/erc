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
package org.nextlets.erc.defaults.handler;

import java.io.UnsupportedEncodingException;

import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.ERCConstants;
import org.nextlets.erc.exception.ERCDeserializationException;
import org.nextlets.erc.handler.ERCResultDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Default implementation of result deserializer.
 * @author Y.Sh.
 */
public class ERCResultDeserializerJsonImpl implements ERCResultDeserializer<Object> {

    protected Logger log = LoggerFactory.getLogger(ERCResultDeserializer.class);

    @Override
    public Object deserealize(
            ERCConfiguration configuration,
            int statusCode,
            String contentType,
            byte[] responseBody,
            Class<Object> resultClass) throws ERCDeserializationException {

        if (contentType != null) {
            String sects[] = contentType.split(";");
            String responseBodyStr;
            try {
                responseBodyStr = new String(responseBody, configuration.getCharset());
            } catch (UnsupportedEncodingException ex) {
                throw new ERCDeserializationException("Unsupported charset: " + configuration.getCharset() , ex);
            }
            for (String sect : sects) {
                if (ERCConstants.JSON_CONTENT_TYPE.equals(sect)) {
                    Gson gson = new GsonBuilder().setDateFormat(configuration.getDateFormat()).create();
                    try {
                        return gson.fromJson(responseBodyStr, resultClass);
                    } catch (JsonSyntaxException ex) {
                        if (resultClass.isAssignableFrom(String.class)) {
                            return responseBodyStr;
                        } else {
                            throw ex;
                        }
                    }
                } else
                if (sect.startsWith(ERCConstants.TEXT_CONTENT_TYPE) && resultClass.isAssignableFrom(String.class)) {
                    return responseBodyStr;
                } else
                if (resultClass.isAssignableFrom(byte[].class) || resultClass.isAssignableFrom(Byte[].class)) {
                    return responseBody;
                }
            }
        }
        return null;
    }

}
