package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class TestGradeRenderer extends OutputRenderer {

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				if(object == null) {
					return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES", "msg.enrolled"));
				}
				
				IGrade grade = (IGrade) object;
				switch (grade.getGradeType()) {
				case GRADENA:
				case GRADERE:
				case GRADEAP: return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES", grade.getGradeType().toString()));
				case GRADEFIVE:
				case GRADETWENTY: return new HtmlText(((Integer)grade.getGradeValue()).toString());
				}

				return new HtmlText("");
			}
			
		};
	}

}
