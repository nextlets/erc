
package org.nextlets.erc.cinfo.caps;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Capabilities {

    private Boolean today;
    private Boolean lastWeek;
    private Boolean lastMonth;
    private Boolean last3Month;
    private Boolean last6Month;
    private Boolean lastYear;
    private Boolean anyDateRange;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The today
     */
    public Boolean getToday() {
        return today;
    }

    /**
     * 
     * @param today
     *     The today
     */
    public void setToday(Boolean today) {
        this.today = today;
    }

    /**
     * 
     * @return
     *     The lastWeek
     */
    public Boolean getLastWeek() {
        return lastWeek;
    }

    /**
     * 
     * @param lastWeek
     *     The lastWeek
     */
    public void setLastWeek(Boolean lastWeek) {
        this.lastWeek = lastWeek;
    }

    /**
     * 
     * @return
     *     The lastMonth
     */
    public Boolean getLastMonth() {
        return lastMonth;
    }

    /**
     * 
     * @param lastMonth
     *     The lastMonth
     */
    public void setLastMonth(Boolean lastMonth) {
        this.lastMonth = lastMonth;
    }

    /**
     * 
     * @return
     *     The last3Month
     */
    public Boolean getLast3Month() {
        return last3Month;
    }

    /**
     * 
     * @param last3Month
     *     The last3Month
     */
    public void setLast3Month(Boolean last3Month) {
        this.last3Month = last3Month;
    }

    /**
     * 
     * @return
     *     The last6Month
     */
    public Boolean getLast6Month() {
        return last6Month;
    }

    /**
     * 
     * @param last6Month
     *     The last6Month
     */
    public void setLast6Month(Boolean last6Month) {
        this.last6Month = last6Month;
    }

    /**
     * 
     * @return
     *     The lastYear
     */
    public Boolean getLastYear() {
        return lastYear;
    }

    /**
     * 
     * @param lastYear
     *     The lastYear
     */
    public void setLastYear(Boolean lastYear) {
        this.lastYear = lastYear;
    }

    /**
     * 
     * @return
     *     The anyDateRange
     */
    public Boolean getAnyDateRange() {
        return anyDateRange;
    }

    /**
     * 
     * @param anyDateRange
     *     The anyDateRange
     */
    public void setAnyDateRange(Boolean anyDateRange) {
        this.anyDateRange = anyDateRange;
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
