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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.ERCConstants;
import org.nextlets.erc.exception.ERCSerializationException;
import org.nextlets.erc.handler.ERCParameterSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Y.Sh.
 */
public class ERCParameterSerializerJsonImpl implements ERCParameterSerializer<Object> {

    protected Logger log = LoggerFactory.getLogger(ERCParameterSerializer.class);

    @Override
    public String getContentType() {
        return ERCConstants.JSON_CONTENT_TYPE;
    }

    @Override
    public String serialize(
            ERCConfiguration configuration,
            String parameterName,
            Object parameterValue) throws ERCSerializationException {

        String result = null;

        if (parameterValue != null) {
            if (parameterValue instanceof String) {
                result = serializeString(configuration, (String)parameterValue);
            } else
            if (parameterValue instanceof Number) {
                result = serializeNumber(configuration, (Number)parameterValue);
            } else
            if (parameterValue instanceof Boolean) {
                result = serializeBoolean(configuration, (Boolean)parameterValue);
            } else
            if (parameterValue instanceof Date) {
                result = serializeDate(configuration, (Date) parameterValue);
            } else {
                result = serializeObject(configuration, parameterValue);
            }
        }

        return result;
    }

    protected String serializeString(ERCConfiguration configuration, String string) {
        return string.toString();
    }

    protected String serializeNumber(ERCConfiguration configuration, Number number) {
        return number.toString();
    }

    protected String serializeBoolean(ERCConfiguration configuration, Boolean bool) {
        return bool.toString();
    }

    protected String serializeDate(ERCConfiguration configuration, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(configuration.getDateFormat());
        return sdf.format(date);
    }

    protected String serializeObject(ERCConfiguration configuration, Object object) {
        Gson gson = new GsonBuilder().setDateFormat(configuration.getDateFormat()).create();
        return gson.toJson(object);
    }
}
