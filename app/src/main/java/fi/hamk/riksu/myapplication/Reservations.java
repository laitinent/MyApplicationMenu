
package fi.hamk.riksu.myapplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reservations {

    private String status;
    private String message;
    private List<Reservation> reservations = null;
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Reservations() {
    }

    /**
     * 
     * @param message
     * @param status
     * @param reservations
     */
    public Reservations(String status, String message, List<Reservation> reservations) {
        super();
        this.status = status;
        this.message = message;
        this.reservations = reservations;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * 
     * @param reservations
     *     The reservations
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
