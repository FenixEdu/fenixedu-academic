package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial;
import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;

public class MathMlMaterialRenderer extends StringRenderer {

	private String classes;

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
	public HtmlComponent render(Object object, Class type) {
		NewMathMlMaterial mathMlMaterial = (NewMathMlMaterial) object;

		HtmlImage image = new HtmlImage();

		image.setSource(getContext().getViewState().getRequest().getContextPath()
				+ "/mathml/MathMlServlet?oid=" + mathMlMaterial.getIdInternal());
		image.setTitle("Equation");

		image.setClasses(classes);

		return image;
	}

}
