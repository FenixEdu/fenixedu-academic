package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Grade;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class GradeRenderer extends OutputRenderer {

    private boolean showGradeScale = true;

    private String gradeClasses;

    private String gradeScaleClasses;

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Grade grade = (Grade) object;
		if (grade == null || grade.isEmpty()) {
		    return new HtmlText();
		}

		HtmlInlineContainer container = new HtmlInlineContainer();
		HtmlText gradeValue = new HtmlText(grade.getValue());
		gradeValue.setClasses(getGradeClasses());
		container.addChild(gradeValue);
		if (isShowGradeScale()) {
		    HtmlText gradeScale = new HtmlText("(" + RenderUtils.getEnumString(grade.getGradeScale()) + ")");
		    gradeScale.setClasses(getGradeScaleClasses());
		    container.addChild(gradeScale);
		}

		return container;
	    }

	};
    }

    public boolean isShowGradeScale() {
	return showGradeScale;
    }

    public void setShowGradeScale(boolean showGradeScale) {
	this.showGradeScale = showGradeScale;
    }

    public String getGradeClasses() {
	return gradeClasses;
    }

    public void setGradeClasses(String gradeClasses) {
	this.gradeClasses = gradeClasses;
    }

    public String getGradeScaleClasses() {
	return gradeScaleClasses;
    }

    public void setGradeScaleClasses(String gradeScaleClasses) {
	this.gradeScaleClasses = gradeScaleClasses;
    }

}
