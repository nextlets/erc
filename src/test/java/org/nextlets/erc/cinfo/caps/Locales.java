
package org.nextlets.erc.cinfo.caps;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Locales {

    private String enUS;
    private String ruRU;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The enUS
     */
    public String getEnUS() {
        return enUS;
    }

    /**
     * 
     * @param enUS
     *     The en_US
     */
    public void setEnUS(String enUS) {
        this.enUS = enUS;
    }

    /**
     * 
     * @return
     *     The ruRU
     */
    public String getRuRU() {
        return ruRU;
    }

    /**
     * 
     * @param ruRU
     *     The ru_RU
     */
    public void setRuRU(String ruRU) {
        this.ruRU = ruRU;
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
