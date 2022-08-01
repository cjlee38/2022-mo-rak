package com.morak.back.appointment.domain;

import com.morak.back.auth.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class AppointmentRecommender {

    private List<RecommendationCell> cells;

    private AppointmentRecommender(List<RecommendationCell> cells) {
        this.cells = cells;
    }

    public static AppointmentRecommender of(Appointment appointment, List<Member> members) {
        DatePeriod datePeriod = appointment.getDatePeriod();
        TimePeriod timePeriod = appointment.getTimePeriod();
        DurationMinutes durationMinutes = appointment.getDurationMinutes();

        LocalDateTime startDateTime = LocalDateTime.of(datePeriod.getStartDate(), timePeriod.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(datePeriod.getEndDate(), timePeriod.getEndTime());

        return new AppointmentRecommender(Stream.iterate(
                        startDateTime,
                        isStartTimeWithDurationNotOverEndTime(durationMinutes, endDateTime),
                        s -> s.plusMinutes(TimePeriod.MINUTES_UNIT))
                .map(s -> toRecommendationCell(members, durationMinutes, s))
                .collect(Collectors.toList()));
//        List<RecommendationCell> cells = new ArrayList<>();
//        while (!startDateTime.plusMinutes(durationMinutes.getDurationMinutes()).isAfter(endDateTime)) {
//            LocalDateTime cellEndDateTime = startDateTime.plusMinutes(durationMinutes.getDurationMinutes());
//            RecommendationCell recommendationCell = new RecommendationCell(startDateTime, cellEndDateTime, members);
//            cells.add(recommendationCell);
//            startDateTime = startDateTime.plusMinutes(30);
//        }
//
//        return new AppointmentRecommender(cells);
    }

    private static Predicate<LocalDateTime> isStartTimeWithDurationNotOverEndTime(DurationMinutes durationMinutes,
                                                                                  LocalDateTime endDateTime) {
        return s -> !s.plusMinutes(durationMinutes.getDurationMinutes()).isAfter(endDateTime);
    }

    private static RecommendationCell toRecommendationCell(List<Member> members, DurationMinutes durationMinutes,
                                                           LocalDateTime s) {
        return new RecommendationCell(s, s.plusMinutes(durationMinutes.getDurationMinutes()), members);
    }

    public List<RankRecommendation> recommend(List<AvailableTime> availableTimes) {
        List<RecommendationCell> recommendationCells = calculate(availableTimes);
        int rank = 0;
        int index = 0;
        int currentScore = Integer.MAX_VALUE;

        List<RankRecommendation> rankRecommendations = new ArrayList<>();
        for (RecommendationCell recommendationCell : recommendationCells) {
            int cellScore = recommendationCell.sumScore();
            ++index;
            if (cellScore < currentScore) {
                rank = index;
                currentScore = cellScore;
            }
            rankRecommendations.add(RankRecommendation.from(rank, recommendationCell));
        }
        return rankRecommendations;
    }

    private List<RecommendationCell> calculate(List<AvailableTime> availableTimes) {
        for (RecommendationCell cell : cells) {
            cell.calculate(availableTimes);
        }
        return cells.stream()
                .filter(RecommendationCell::hasAnyMembers)
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }


}
