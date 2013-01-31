package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.LocalDate;

public class PhdProgramCalendarUtil {

	static public int countWorkDaysBetween(final LocalDate startDate, final LocalDate endDate) {

		int result = 0;
		LocalDate current = startDate.plusDays(1);

		while (!current.isAfter(endDate)) {
			if (isWorkDay(current)) {
				result++;
			}

			current = current.plusDays(1);
		}

		return result;
	}

	static public LocalDate addWorkDaysTo(final LocalDate date, final int workDays) {
		int current = workDays;
		LocalDate result = date;

		while (current > 0) {
			result = result.plusDays(1);

			if (isWorkDay(result)) {
				current--;
			}
		}

		return result;
	}

	static private boolean isWorkDay(LocalDate current) {
		final WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(current.toDateTimeAtStartOfDay());

		return dayOfWeek != WeekDay.SATURDAY && dayOfWeek != WeekDay.SUNDAY && !Holiday.isHoliday(current);
	}
}
