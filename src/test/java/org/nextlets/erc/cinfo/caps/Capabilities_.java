
package org.nextlets.erc.cinfo.caps;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Capabilities_ {

    private String today;
    private String lastWeek;
    private String lastMonth;
    private String last3Month;
    private String last6Month;
    private String lastYear;
    private String anyDateRange;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The today
     */
    public String getToday() {
        return today;
    }

    /**
     * 
     * @param today
     *     The today
     */
    public void setToday(String today) {
        this.today = today;
    }

    /**
     * 
     * @return
     *     The lastWeek
     */
    public String getLastWeek() {
        return lastWeek;
    }

    /**
     * 
     * @param lastWeek
     *     The lastWeek
     */
    public void setLastWeek(String lastWeek) {
        this.lastWeek = lastWeek;
    }

    /**
     * 
     * @return
     *     The lastMonth
     */
    public String getLastMonth() {
        return lastMonth;
    }

    /**
     * 
     * @param lastMonth
     *     The lastMonth
     */
    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }

    /**
     * 
     * @return
     *     The last3Month
     */
    public String getLast3Month() {
        return last3Month;
    }

    /**
     * 
     * @param last3Month
     *     The last3Month
     */
    public void setLast3Month(String last3Month) {
        this.last3Month = last3Month;
    }

    /**
     * 
     * @return
     *     The last6Month
     */
    public String getLast6Month() {
        return last6Month;
    }

    /**
     * 
     * @param last6Month
     *     The last6Month
     */
    public void setLast6Month(String last6Month) {
        this.last6Month = last6Month;
    }

    /**
     * 
     * @return
     *     The lastYear
     */
    public String getLastYear() {
        return lastYear;
    }

    /**
     * 
     * @param lastYear
     *     The lastYear
     */
    public void setLastYear(String lastYear) {
        this.lastYear = lastYear;
    }

    /**
     * 
     * @return
     *     The anyDateRange
     */
    public String getAnyDateRange() {
        return anyDateRange;
    }

    /**
     * 
     * @param anyDateRange
     *     The anyDateRange
     */
    public void setAnyDateRange(String anyDateRange) {
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
