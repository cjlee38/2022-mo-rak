//package com.morak.back.appointment.domain;
//
//import com.morak.back.auth.domain.Member;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Locale;
//import org.junit.jupiter.api.Test;
//
//class AppointmentRecommenderTest {
//
//    @Test
//    void 생성() {
//        // given
//        DatePeriod datePeriod = new DatePeriod(
//                LocalDate.of(2122, 7, 30),
//                LocalDate.of(2122, 8, 2)
//        );
//        TimePeriod timePeriod = new TimePeriod(
//                LocalTime.of(0, 0),
//                LocalTime.of(0, 0)
//        );
//        DurationMinutes durationMinutes = new DurationMinutes(3, 0);
//        List<Member> members = List.of(
//                new Member(1L, "A", "A", "https://test.com"),
//                new Member(2L, "B", "B", "https://test.com"),
//                new Member(3L, "C", "C", "https://test.com")
//        );
//        // when
////        AppointmentRecommender recommender = AppointmentRecommender.of(datePeriod, timePeriod, durationMinutes,
////                members);
//
//        // then
////        System.out.println(recommender.getCells());
//    }
//
//    @Test
//    void 점수_계산_하루종일() {
//        // given
//        Appointment appointment = Appointment.builder()
//                .id(1L)
//                .startDate(LocalDate.of(2122, 7, 30))
//                .endDate(LocalDate.of(2122, 8, 3))
//                .startTime(LocalTime.of(0, 0))
//                .endTime(LocalTime.of(0, 0))
//                .durationHours(3)
//                .durationMinutes(0)
//                .build();
//
//        Member memberA = new Member(1L, "A", "A", "https://test.com");
//        Member memberB = new Member(2L, "B", "B", "https://test.com");
//        Member memberC = new Member(3L, "C", "C", "https://test.com");
//        List<Member> members = List.of(memberA, memberB, memberC);
//        // when
//        AppointmentRecommender recommender = AppointmentRecommender.of(
//                appointment.getDatePeriod(),
//                appointment.getTimePeriod(),
//                appointment.getDurationMinutes(),
//                members
//        );
//
//        System.out.println(recommender.getCells());
//
//        System.out.println("============================");
//
//        // then
//        List<AvailableTime> availableTimes = List.of(
//                AvailableTime.builder().id(1L).member(memberA).appointment(appointment).startDateTime(LocalDateTime.of(2122, 7, 30, 10, 0))
//                        .endDateTime(LocalDateTime.of(2122, 7, 30, 10, 30)).build(),
//                AvailableTime.builder().id(1L).member(memberA).appointment(appointment).startDateTime(LocalDateTime.of(2122, 7, 30, 10, 30))
//                        .endDateTime(LocalDateTime.of(2122, 7, 30, 11, 0)).build()
//        );
//
//        final List<RecommendationCell> recommend = recommender.recommend(availableTimes);
//        for (RecommendationCell cell : recommend) {
//            System.out.println(cell + " =====" + cell.sumScore());
//        }
//    }
//
//    @Test
//    void asd() {
//        // given
//        String pattern = "hh:mm:ss a";
//
//        //1. LocalTime
//        LocalTime parse = LocalTime.parse("01:23:45PM", DateTimeFormatter.ofPattern(pattern, Locale.US));
//        System.out.println(parse);
//        LocalTime now = LocalTime.now();
//        System.out.println(now.format(DateTimeFormatter.ofPattern(pattern)));
//
//        //2. LocalDateTime
//        LocalDateTime nowTime = LocalDateTime.now();
//        System.out.println(nowTime.format(DateTimeFormatter.ofPattern(pattern)));
//        // when
//
//        // then
//    }
//}
