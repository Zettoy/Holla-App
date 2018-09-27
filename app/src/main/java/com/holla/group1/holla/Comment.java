package com.holla.group1.holla;

import org.joda.time.DateTime;

public class Comment {
    private String content;
    private String username;
    private String creationTime; // Can't explicitly store as DateTime as we need this class to be serializable
    // Stored as ISO-8601 formatted string (DateTime can take this as argument)

    public Comment(String content, String username, String creationTime) {
        this.content = content;
        this.username = username;
        this.creationTime = creationTime;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestampAgo() {
        return DateTimeFormatter.getTimestampAgo(getCreationTime());
    }

    public DateTime getCreationTime() {
        return new DateTime(creationTime);
    }
}
