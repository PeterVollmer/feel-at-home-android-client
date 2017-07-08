package club.frickel.feelathome

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Device : Serializable {
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
     * @param id
     */
    @JsonProperty("Id")
    @get:JsonProperty("Id")
    @set:JsonProperty("Id")
    var id: String = ""
    /**

     * @return
     */
    /**

     * @param active
     */
    @JsonProperty("Active")
    @get:JsonProperty("Active")
    @set:JsonProperty("Active")
    var isActive: Boolean = false

    /**

     */
    constructor() {

    }

    /**
     * @param id
     * *
     * @param name
     * *
     * @param active
     */
    constructor(id: String, name: String, active: Boolean) {
        this.name = name
        this.id = id
        this.isActive = active
    }

    override fun toString(): String {
        return this.name
    }


}
