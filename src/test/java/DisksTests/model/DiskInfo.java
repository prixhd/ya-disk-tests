package DisksTests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiskInfo {

    @JsonProperty("trash_size")
    public Long trashSize;

    @JsonProperty("total_space")
    public Long totalSpace;

    @JsonProperty("used_space")
    public Long usedSpace;

    @JsonProperty("system_folders")
    public SystemFolder systemFolders;

    public User user;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SystemFolder {
        public String applications;
        public String downloads;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public String login;
    }
}