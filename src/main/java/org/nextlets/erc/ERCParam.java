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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nextlets.erc.defaults.handler.ERCParameterSerializerJsonImpl;
import org.nextlets.erc.handler.ERCParameterSerializer;

/**
 * <h3>Annotation to define request parameter.</h3>
 * You have two ways to send your parameters by HTTP:
 * <ol>
 * <li>Using annotated parameters in method declaration.</li>
 * <li>Using map of parameters.</li>
 * </ol>
 * <p>
 * This annotation is used for way #1. So, if you wish to use this way,
 * you need to add annotation {@literal @}ERCParam for all method parameters
 * which are considered as HTTP params, for each annotated parameter you must
 * specify it's name.
 * </p>
 * <p>
 * Also you can specify is this parameter required or not (can be null or not):
 * <ul>
 * <li>If <code>required</code> flag is set to <code>false</code> and value of parameter is <code>null</code>,
 * this parameter won't be sent.</li>
 * <li>If <code>required</code> flag is set to <code>true</code> and value of parameter is <code>null</code>,
 * <code>ERCClientException</code> will be thrown.</li>
 * </ul>
 * Probably you'll wish to use your custom serialization of parameter, you can always do it using <code>serializer</code>
 * method of this annotation.
 * </p>
 *
 * @author Y.Sh.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ERCParam {

	public static final String REQUEST_BODY = "REQUEST$BODY";
    public static final String SERIALIZER_METHOD = "serializer";

    /**
     * Defines parameter name
     */
    public String name();

    /**
     * Flag of mandatory (if set to false, null values are acceptable)
     */
    public boolean required() default true;

    /**
     * Class of parameter serializer, if not specified, DefaultERCParameterSerializer.class is used by default.
     */
    public Class<? extends ERCParameterSerializer<?>> serializer() default ERCParameterSerializerJsonImpl.class;

}
