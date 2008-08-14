package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
