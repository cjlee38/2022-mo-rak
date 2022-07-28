package com.morak.back.poll.domain;

import com.morak.back.auth.domain.Member;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class PollResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PollItem pollItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @NotNull
    @Size(max = 255, message = "description은 최대 255자여야 합니다.")
    private String description;

    public PollResult fromAnonymous() {
        return PollResult.builder()
                .id(id)
                .pollItem(pollItem)
                .member(Member.getAnonymous())
                .description(description)
                .build();
    }

    public Boolean isSameMember(Member member) {
        return this.member.equals(member);
    }
}
