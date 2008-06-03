package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
