package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomOccupationWeekBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.WeekBean;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RoomOccupationWeekBeanProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		// return null;
		return new BiDirectionalConverter() {

			@Override
			public Object convert(Class type, Object value) {
				if (value == null) {
					return null;
				}

				return new WeekBean(DateTimeFormat.forPattern("yyyy.MM.dd").parseDateTime((String) value));
			}

			@Override
			public String deserialize(Object object) {

				WeekBean bean = (WeekBean) object;
				return bean.getWeek().toString("yyyy.MM.dd");
			}

		};
	}

	@Override
	public Object provide(Object source, Object current) {
		RoomOccupationWeekBean roomOccupationWeekBean = (RoomOccupationWeekBean) source;

		List<WeekBean> result = new ArrayList<WeekBean>();

		if (roomOccupationWeekBean.getAcademicInterval() == null) {
			return result;
		}

		DateTime start = roomOccupationWeekBean.getAcademicInterval().getStart();
		DateTime end = getMaximum(roomOccupationWeekBean);

		for (DateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
			if (date.getDayOfWeek() == DateTimeConstants.MONDAY) {
				result.add(new WeekBean(date));
			}
		}
		return result;
	}

	private DateTime getMaximum(RoomOccupationWeekBean roomOccupationWeekBean) {
		AcademicInterval nextAcademicInterval = roomOccupationWeekBean.getAcademicInterval().getNextAcademicInterval();
		if (nextAcademicInterval != null) {
			if (nextAcademicInterval.getStart().isAfter(roomOccupationWeekBean.getAcademicInterval().getEnd())) {
				return nextAcademicInterval.getStart();
			}
		}

		return roomOccupationWeekBean.getAcademicInterval().getEnd();
	}
}
