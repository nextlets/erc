
package org.nextlets.erc.cinfo.curr;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Currency {

    private Integer scale;
    private Double rate;
    private Currency_ currency;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The scale
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * 
     * @param scale
     *     The scale
     */
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    /**
     * 
     * @return
     *     The rate
     */
    public Double getRate() {
        return rate;
    }

    /**
     * 
     * @param rate
     *     The rate
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * 
     * @return
     *     The currency
     */
    public Currency_ getCurrency() {
        return currency;
    }

    /**
     * 
     * @param currency
     *     The currency
     */
    public void setCurrency(Currency_ currency) {
        this.currency = currency;
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
