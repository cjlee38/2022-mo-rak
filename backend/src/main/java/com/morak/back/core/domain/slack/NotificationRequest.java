package com.morak.back.core.domain.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationRequest {

    private String username;
    private String text;
    @JsonProperty("icon_emoji")
    private String iconEmoji;
}
