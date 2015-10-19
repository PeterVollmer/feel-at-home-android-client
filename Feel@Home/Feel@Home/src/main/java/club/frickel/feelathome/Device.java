package club.frickel.feelathome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * User: Peter Vollmer
 * Date: 2/26/13
 * Time: 12:33 PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device implements Serializable {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Active")
    private boolean active;

    /**
     *
     */
    public Device() {

    }

    /**
     * @param id
     * @param name
     * @param active
     */
    public Device(String id, String name, boolean active) {
        this.name = name;
        this.id = id;
        this.active = active;
    }

    /**
     * @return
     */
    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @JsonProperty("Active")
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    @JsonProperty("Active")
    public void setActive(boolean active) {
        this.active = active;
    }

    public String toString() {
        return this.name;
    }


}
