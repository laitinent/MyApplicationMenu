
package fi.hamk.riksu.myapplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reservation {

    private String id;
    private String subject;
    private String modifiedDate;
    private String startDate;
    private String endDate;
    private List<ReservationResource> resources = null;
    private String description;
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Reservation() {
    }

    /**
     * 
     * @param id
     * @param resources
     * @param startDate
     * @param description
     * @param subject
     * @param endDate
     * @param modifiedDate
     */
    public Reservation(String id, String subject, String modifiedDate, String startDate, String endDate, List<ReservationResource> resources, String description) {
        super();
        this.id = id;
        this.subject = subject;
        this.modifiedDate = modifiedDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resources = resources;
        this.description = description;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 
     * @param subject
     *     The subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 
     * @return
     *     The modifiedDate
     */
    public String getModifiedDate() {
        return modifiedDate;
    }

    /**
     * 
     * @param modifiedDate
     *     The modifiedDate
     */
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     *     The resources
     */
    public List<ReservationResource> getResources() {
        return resources;
    }

    /**
     * 
     * @param resources
     *     The resources
     */
    public void setResources(List<ReservationResource> resources) {
        this.resources = resources;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
