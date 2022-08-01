package com.morak.back.appointment.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimePeriodTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_시작_시간이_30분_단위가_아닐_경우_생성시_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> new TimePeriod(LocalTime.of(10, minutes), LocalTime.of(14, 0)))
                .isInstanceOf(InvalidRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 29, 31, 59})
    void 약속잡기_마지막_시간이_30분_단위가_아닐_경우_생성시_예외를_던진다(int minutes) {
        // when & then
        assertThatThrownBy(() -> new TimePeriod(LocalTime.of(10, 30), LocalTime.of(14, minutes)))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void 약속잡기_마지막_시간이_자정일_경우_생성시_시간순_검증을_하지_않는다() {
        // given
        LocalTime midnight = LocalTime.of(0, 0);

        // when & then
        assertThatNoException().isThrownBy(
                () -> new TimePeriod(LocalTime.of(0, 0), midnight)
        );
    }

    @Test
    void 약속잡기_가능시간이_10시부터_20시이고_선택시간이_23시_30분부터_24시이면_예외를_던진다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(20, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 1, 23, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 2, 0, 0);

        // then
        assertThatThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 약속잡기_가능시간이_10시부터_20시이고_선택시간이_24시_0분부터_24시_30분이면_예외를_던진다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(20, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 2, 0, 30);

        // then
        assertThatThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 약속잡기_가능시간이_10시부터_20시이고_선택시간이_19시_0분부터_19시_30분이면_예외를_던진다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(20, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 1, 19, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 1, 19, 30);

        // then
        assertThatNoException()
                .isThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime));
    }

    @Test
    void 약속잡기_가능시간이_10시부터_24시이고_선택시간이_23시_30분부터_24시이면_통과한다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(0, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 1, 23, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 2, 0, 0);

        // then
        assertThatNoException()
                .isThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime));
    }

    @Test
    void 약속잡기_가능시간이_10시부터_24시이고_선택시간이_24시_0분부터_24시_30분이면_예외를_던진다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(10, 0), LocalTime.of(0, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 2, 0, 30);

        // then
        assertThatThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 약속잡기_가능시간이_0시부터_24시이고_선택시간이_23시_30분부터_24시이면_통과한다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(0, 0), LocalTime.of(0, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 1, 23, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 2, 0, 0);

        // then
        assertThatNoException()
                .isThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime));
    }

    @Test
    void 약속잡기_가능시간이_0시부터_24시이고_선택시간이_24시_0분부터_24시_30분이면_통과한다() {
        // given
        TimePeriod timePeriod = new TimePeriod(LocalTime.of(0, 0), LocalTime.of(0, 0));

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 9, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 9, 2, 0, 30);

        // then
        assertThatNoException()
                .isThrownBy(() -> timePeriod.validateAvailableTimeRange(startDateTime, endDateTime));
    }
}
