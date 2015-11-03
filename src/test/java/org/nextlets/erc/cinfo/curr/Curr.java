
package org.nextlets.erc.cinfo.curr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Curr {

    private List<Currency> currencies = new ArrayList<Currency>();
    private Provider provider;
    private Date date;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The currencies
     */
    public List<Currency> getCurrencies() {
        return currencies;
    }

    /**
     *
     * @param currencies
     *     The currencies
     */
    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    /**
     *
     * @return
     *     The provider
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     *
     * @param provider
     *     The provider
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    /**
     *
     * @return
     *     The date
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     *     The date
     */
    public void setDate(Date date) {
        this.date = date;
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
