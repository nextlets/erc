
package org.nextlets.erc.cinfo.caps;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Meta {

    private String dateFormat;
    private Locales locales;
    private Capabilities_ capabilities;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * 
     * @param dateFormat
     *     The dateFormat
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * 
     * @return
     *     The locales
     */
    public Locales getLocales() {
        return locales;
    }

    /**
     * 
     * @param locales
     *     The locales
     */
    public void setLocales(Locales locales) {
        this.locales = locales;
    }

    /**
     * 
     * @return
     *     The capabilities
     */
    public Capabilities_ getCapabilities() {
        return capabilities;
    }

    /**
     * 
     * @param capabilities
     *     The capabilities
     */
    public void setCapabilities(Capabilities_ capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
