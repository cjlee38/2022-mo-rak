package com.morak.back.team.domain;

import com.morak.back.core.domain.Code;
import com.morak.back.poll.domain.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class TeamInvitation extends BaseEntity {

    private static final long EXPIRED_MINUTES = 30L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Embedded
    @Valid
    private Code code;

    @Embedded
    @Valid
    private ExpiredTime expiredAt;

    protected TeamInvitation() {
    }

    @Builder
    public TeamInvitation(Long id, Team team, Code code, ExpiredTime expiredAt) {
        this.id = id;
        this.team = team;
        this.code = code;
        this.expiredAt = expiredAt;
        if (expiredAt == null) {
            this.expiredAt = ExpiredTime.withMinute(EXPIRED_MINUTES);
        }
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    public String getCode() {
        return code.getCode();
    }
}
