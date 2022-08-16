package com.morak.external;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.morak.back.core.domain.slack.RestSlackClient;
import com.morak.back.core.domain.slack.SlackClient;
import com.morak.back.core.domain.slack.SlackWebhook;
import com.morak.back.team.domain.Team;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RestSlackClientTest {

    @Test
    @Disabled // deactivate this annotation if required
    void 실제_모락_슬랙에_마감_알림을_보낸다() {
        // given
        String url = System.getenv("SLACK_ERROR_URL");
        SlackClient client = new RestSlackClient();

        // when
        SlackWebhook webhook = new SlackWebhook(1L, new Team(), url);

        // then
        assertThatNoException().isThrownBy(() -> client.notifyClosed(webhook, "test-in-backend-test-code"));
    }
}
