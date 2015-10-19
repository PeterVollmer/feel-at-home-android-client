package club.frickel.feelathome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * User: Peter Vollmer
 * Date: 2/26/13
 * Time: 1:32 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Effect implements Serializable{

    @JsonProperty("Description")
    private String description;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Config")
    private Map<String,Object> config;

    /**
     *
     */
    public Effect() {

    }

    /**
     * @param name
     * @param description
     * @param config
     */
    public Effect(String name, String description, Map<String,Object> config) {
        this.name = name;
        this.description = description;
        this.config = config;
    }

    /**
     * @return
     */
    @JsonProperty("Config")
    public Map<String,Object> getConfig() {
        return config;
    }

    /**
     * @param config
     */
    @JsonProperty("Config")
    public void setConfig(Map<String,Object> config) {
        this.config = config;
    }

    /**
     * @return
     */
    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
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
     * @return
     */
    @Override
    public String toString() {
        return this.name;

    }
}
