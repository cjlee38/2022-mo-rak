package com.morak.back.appointment.domain;

import com.morak.back.core.exception.InvalidRequestException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class TimePeriod {

    public static final LocalTime ZERO_TIME = LocalTime.of(0, 0);
    public static final int MINUTES_UNIT = 30;

    @NotNull(message = "약속잡기 시작 시간은 null일 수 없습니다.")
    private LocalTime startTime;

    @NotNull(message = "약속잡기 마지막 시간은 null일 수 없습니다.")
    private LocalTime endTime;

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        if (!isMidnight(endTime)) {
            validateChronology(startTime, endTime);
        }
        validateMinutes(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private boolean isMidnight(LocalTime endTime) {
        return endTime.equals(ZERO_TIME);
    }

    private void validateMinutes(LocalTime startTime, LocalTime endTime) {
        if (isNotDividedByUnit(startTime) || isNotDividedByUnit(endTime)) {
            throw new InvalidRequestException("약속잡기 시작/마지막 시간은 " + MINUTES_UNIT + "분 단위여야 합니다.");
        }
    }

    private boolean isNotDividedByUnit(LocalTime time) {
        return time.getMinute() % MINUTES_UNIT != 0;
    }

    private void validateChronology(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new InvalidRequestException("약속잡기 마지막 시간은 시작 시간 이후여야 합니다.");
        }
    }

    public void validateAvailableTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime newStartDateTime = LocalDateTime.of(startDateTime.toLocalDate(), this.startTime);
        LocalDateTime newEndDateTime = LocalDateTime.of(startDateTime.toLocalDate(), this.endTime);
        if (isMidnight(this.endTime)) {
            newEndDateTime = newEndDateTime.plusDays(1);
        }
        if (startDateTime.isBefore(newStartDateTime) || endDateTime.isAfter(newEndDateTime)) {
            throw new InvalidRequestException("약속잡기 가능 시간은 지정한 시간 이내여야 합니다.");
        }
//        if (this.endTime.equals(ZERO_TIME)) {
//            return;
//        }
//        if (startTime.isBefore(this.startTime) || endTime.isAfter(this.endTime)) {
//            throw new InvalidRequestException("약속잡기 가능 시간은 지정한 시간 이내여야 합니다.");
//        }
    }
}
