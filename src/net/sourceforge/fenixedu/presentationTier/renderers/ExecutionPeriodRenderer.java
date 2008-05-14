package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ExecutionPeriodRenderer extends OutputRenderer {

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				ExecutionSemester executionSemester = (ExecutionSemester) object;
				StringBuilder text = new StringBuilder();
				text.append(executionSemester.getExecutionYear().getYear()).append(", "); 
				text.append(executionSemester.getSemester()).append(RenderUtils.getResourceString("label.semester.short"));
				return new HtmlText(text.toString());
			}
		};
	}

}
