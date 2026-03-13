package DisksTests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

    public String name;

    public String type;

    public String path;

    @JsonProperty("public_url")
    public String publicUrl;

    @JsonProperty("public_key")
    public String publicKey;
}
