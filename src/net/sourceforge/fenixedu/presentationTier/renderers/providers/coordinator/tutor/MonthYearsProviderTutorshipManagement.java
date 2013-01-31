package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.domain.Tutorship;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.Period;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MonthYearsProviderTutorshipManagement implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		List<String> result = new ArrayList<String>();

		ChangeTutorshipBean tutorshipBean = (ChangeTutorshipBean) source;

		Partial startMonthYear = tutorshipBean.getTutorship().getStartDate();
		startMonthYear = startMonthYear.plus(Period.years(2));

		Partial endMonthYear = startMonthYear.plus(Period.years(Tutorship.TUTORSHIP_MAX_PERIOD));

		while (startMonthYear.compareTo(endMonthYear) < 0) {
			String line =
					tutorshipBean.generateMonthYearOption(startMonthYear.get(DateTimeFieldType.monthOfYear()),
							startMonthYear.get(DateTimeFieldType.year()));
			result.add(line);

			startMonthYear = startMonthYear.plus(Period.months(1));
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return null;
	}
}
