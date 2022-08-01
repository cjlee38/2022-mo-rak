package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RankRecommendation {

    private final int rank;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final List<Member> availableMembers;
    private final List<Member> unavailableMembers;

    public static RankRecommendation from(int rank, RecommendationCell recommendationCell) {
        Map<Member, Integer> memberScores = recommendationCell.getMemberScores();
        long durationUnitCount = recommendationCell.getDurationUnitCount();
        List<Member> availableMembers = getAvailableMembers(memberScores, durationUnitCount);
        List<Member> unavailableMembers = getUnavailableMembers(memberScores, availableMembers);

        return new RankRecommendation(
                rank,
                recommendationCell.getStartDateTime(),
                recommendationCell.getEndDateTime(),
                availableMembers,
                unavailableMembers
        );
    }

    private static List<Member> getAvailableMembers(Map<Member, Integer> memberScores, long durationUnitCount) {
        return memberScores.entrySet().stream()
                .filter(entry -> entry.getValue() == durationUnitCount)
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    private static List<Member> getUnavailableMembers(Map<Member, Integer> memberScores, List<Member> availableMembers) {
        Set<Member> members = memberScores.keySet();
        members.removeAll(availableMembers);
        return new ArrayList<>(members);
    }
}
