package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class SummaryPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		if (object == null) {
		    return new HtmlText();
		}

		Summary summary = (Summary) object;
		StringBuilder builder = new StringBuilder();
		Lesson lesson = null;
		builder.append(summary.getSummaryDateYearMonthDay().getDayOfMonth()).append("/").append(
			summary.getSummaryDateYearMonthDay().getMonthOfYear()).append("/").append(
			summary.getSummaryDateYearMonthDay().getYear()).append(" - ").append(
			RenderUtils.getResourceString("DEFAULT", "label.lesson") + ": ");

		if (summary.getIsExtraLesson()) {
		    builder.append(RenderUtils.getEnumString(SummaryType.EXTRA_SUMMARY, null)).append(
			    " ");
		    builder.append(" (").append(summary.getSummaryHourHourMinuteSecond().getHour())
			    .append(":").append(
				    summary.getSummaryHourHourMinuteSecond().getMinuteOfHour()).append(
				    ") ");
		} else {
		    lesson = summary.getLesson();
		    if (lesson != null) {
			builder.append(lesson.getDiaSemana().toString()).append(" (").append(
			DateFormatUtil.format("HH:mm", lesson.getInicio().getTime())).append("-")
				.append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime()))
				.append(") ");
		    }
		}
		if (lesson != null && lesson.getSala() != null) {
		    builder.append(((OldRoom)lesson.getSala()).getName().toString());
		}

		return new HtmlText(builder.toString());
	    }
	};
    }

}