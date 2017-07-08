package club.frickel.feelathome

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Effect : Serializable {

    /**
     * @return
     */
    /**
     * @param description
     */
    @JsonProperty("Description")
    @get:JsonProperty("Description")
    @set:JsonProperty("Description")
    var description: String = ""
    /**
     * @return
     */
    /**
     * @param name
     */
    @JsonProperty("Name")
    @get:JsonProperty("Name")
    @set:JsonProperty("Name")
    var name: String = ""
    /**
     * @return
     */
    /**
     * @param config
     */
    @JsonProperty("Config")
    @get:JsonProperty("Config")
    @set:JsonProperty("Config")
    var config: Map<String, Any> = emptyMap()

    /**

     */
    constructor() {

    }

    /**
     * @param name
     * *
     * @param description
     * *
     * @param config
     */
    constructor(name: String, description: String, config: Map<String, Any>) {
        this.name = name
        this.description = description
        this.config = config
    }

    /**
     * @return
     */
    override fun toString(): String {
        return this.name
    }
}