package DisksTests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {

    public String href;

    public String method;

    public boolean templated;
}

