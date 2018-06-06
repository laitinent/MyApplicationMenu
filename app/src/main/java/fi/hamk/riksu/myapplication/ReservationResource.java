
package fi.hamk.riksu.myapplication;

import java.util.HashMap;
import java.util.Map;

public class ReservationResource {

    private String id;
    private String type;
    private String code;
    private ReservationResource parent;
    private String name;
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ReservationResource() {
    }

    /**
     * 
     * @param id
     * @param name
     * @param parent
     * @param code
     * @param type
     */
    public ReservationResource(String id, String type, String code, ReservationResource parent, String name) {
        super();
        this.id = id;
        this.type = type;
        this.code = code;
        this.parent = parent;
        this.name = name;
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
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The parent
     */
    public ReservationResource getParent() {
        return parent;
    }

    /**
     * 
     * @param parent
     *     The parent
     */
    public void setParent(ReservationResource parent) {
        this.parent = parent;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
