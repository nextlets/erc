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
package org.nextlets.erc.utils;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.nextlets.erc.ERCConfiguration;
import org.nextlets.erc.ERCConstants;
import org.nextlets.erc.ERCParam;

/**
 * Utilities.
 *
 * @author Y.Sh.
 */
public class ERCUtils {

    /**
     * Checks is the given method from java.lang.Object
     * @param method - method to check
     * @return true if method from java.lang.Object
     */
    public static boolean isJavaLangObjectMethod(Method method) {
        Method methods[] = Object.class.getDeclaredMethods();
        for (Method objMethod : methods) {
            if (objMethod.getName().equals(method.getName())) {
                Class<?> methodParameterTypes[] = method.getParameterTypes();
                Class<?> objectMethodParameterTypes[] = objMethod.getParameterTypes();
                if (objectMethodParameterTypes.length == methodParameterTypes.length) {
                    boolean matched = true;
                    for (int i = 0; i < methodParameterTypes.length; i++) {
                        Class<?> class1 = methodParameterTypes[i];
                        Class<?> class2 = objectMethodParameterTypes[i];
                        if (!class1.equals(class2)) {
                            matched = false;
                            break;
                        }
                    }
                    if (matched) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Encodes the given string.
     * Implementation can be used on Android platform also.
     * @param src - string to encode.
     * @return encoded string.
     */
    public static String encodeString(String src) {
        try {
            try {
                Class<?>base64class = Class.forName("android.util.Base64");
                Method method = base64class.getMethod("encodeToString", new Class[]{byte[].class, int.class});
                return (String)method.invoke(null, new Object[]{src.getBytes(), 2});
            } catch (ClassNotFoundException e0) {
                try {
                    Class<?>base64class = Class.forName("org.apache.commons.codec.binary.Base64");
                    Method method = base64class.getMethod("encodeBase64String", new Class[]{byte[].class});
                    return (String)method.invoke(null, new Object[]{src.getBytes()});
                } catch (ClassNotFoundException e1) {
                    throw new IllegalStateException("Unable to find class for Base64 encoding.");
                }
            }
        } catch(NoSuchMethodException ex) {
            throw new IllegalStateException("Unable to find method for Base64 encoding.", ex);
        } catch(Exception ex) {
            throw new IllegalStateException("Could not invoke method for Base64 encoding.", ex);
        }
    }

    /**
     * Makes result URL from initial URL and path
     * @param url - initial URL
     * @param path - path
     */
    public static String makeUrl(String url, String path) {
        url = trim(url, '/');
        path = trim(path, '/');
        return path.length() == 0 ? url : url + "/" + path;
    }

    /**
     * Trims string by the given character.
     * if char is a space character, then is more useful to use String.trim().
     * @param str - string to trim.
     * @param trimChar - char to check.
     * @return trimmed string.
     */
    public static String trim(String str, char trimChar) {
        char[] value = str.toCharArray();
        int start = 0, length = value.length;
        while ((start < length) && (value[start] == trimChar)) { start++; }
        while ((start < length) && (value[length - 1] == trimChar)) { length--; }
        return ((start > 0) || (length < value.length)) ? str.substring(start, length) : str;
    }

    public static <A extends Annotation> A getClassAnnotation(Class<?>clientInterface, Class<A> annotationClass) {
        A annotation = clientInterface.getAnnotation(annotationClass);
        if (annotation == null) {
            for(Class<?>parentClass : clientInterface.getInterfaces()) {
                annotation = getClassAnnotation(parentClass, annotationClass);
                if (annotation != null) {
                    System.out.println(parentClass);
                    return annotation;
                }
            }
        }
        return annotation;
    }

    public static String buildUrlAndParameters(ERCConfiguration configuration, String urlTemplate, HttpRequestBase req, String contentType, Map<String, String> params)
            throws UnsupportedEncodingException {

        if (urlTemplate == null || params == null) {
            return null;
        }

        String resUrl = ERCUtils.makeUrl(configuration.getServiceUrl(), urlTemplate);

        for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            String key = '{' + entry.getKey() + '}';
            if (resUrl.contains(key)) {
                resUrl = resUrl.replace(key, URLEncoder.encode(entry.getValue().toString(), configuration.getCharset()));
                it.remove();
            }
        }

        if (!params.isEmpty()) {
            if (req instanceof HttpEntityEnclosingRequest) {
                // PUT, POST
                HttpEntityEnclosingRequest eeReq = (HttpEntityEnclosingRequest) req;
                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                if (params.entrySet().size() == 1 &&
                        params.entrySet().iterator().next().getKey().equals(ERCParam.REQUEST_BODY)) {
                    eeReq.setEntity(new StringEntity(params.entrySet().iterator().next().getValue()));
                    req.addHeader(ERCConstants.HDR_CONTENT_TYPE, contentType);
                } else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                    eeReq.setEntity(new UrlEncodedFormEntity(urlParameters, Charset.forName(configuration.getCharset()).name()));
                }
            } else {
                // GET, DELETE
                boolean first = !resUrl.contains("?");
                StringBuilder sb = new StringBuilder(resUrl);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first) {
                        first = false;
                        sb.append('?');
                    } else {
                        sb.append('&');
                    }
                    sb.append(entry.getKey()).append('=')
                            .append(URLEncoder.encode(entry.getValue(), Charset.forName(configuration.getCharset()).name()));
                }
                resUrl = sb.toString();
            }
        }

        return resUrl;
    }

}
