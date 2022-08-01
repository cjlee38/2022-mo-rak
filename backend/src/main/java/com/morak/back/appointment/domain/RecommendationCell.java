package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class RecommendationCell implements Comparable<RecommendationCell> {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Map<Member, Integer> memberScores;

    public RecommendationCell(LocalDateTime startDateTime, LocalDateTime endDateTime, List<Member> members) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memberScores = members.stream()
                .collect(Collectors.toMap(Function.identity(), member -> 0));
    }

    public void calculate(List<AvailableTime> availableTimes) {
        for (AvailableTime availableTime : availableTimes) {
            increaseScoreIfBetween(availableTime);
        }
    }

    private void increaseScoreIfBetween(AvailableTime availableTime) {
        if (isBetween(availableTime.getDateTimePeriod())) {
            memberScores.computeIfPresent(availableTime.getMember(), (member, score) -> score + 1);
        }
    }

    private boolean isBetween(DateTimePeriod period) {
        if (period.getStartDateTime().isBefore(startDateTime)) {
            return false;
        }
        return !period.getEndDateTime().isAfter(endDateTime);
    }

    @Override
    public String toString() {
        return "RecommendationCell{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", memberScores=" + memberScores +
                "}\n";
    }

    public int sumScore() {
        return this.memberScores.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    @Override
    public int compareTo(RecommendationCell o) {
        return Integer.compare(o.sumScore(), this.sumScore());
    }

    public boolean hasAnyMembers() {
        return sumScore() != 0;
    }

    public long getDurationUnitCount() {
        return Duration.between(startDateTime, endDateTime).toMinutes() % TimePeriod.MINUTES_UNIT;
    }
}
