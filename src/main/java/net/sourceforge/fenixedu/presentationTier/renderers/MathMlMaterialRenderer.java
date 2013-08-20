package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial;
import pt.ist.fenixWebFramework.renderers.StringRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;

public class MathMlMaterialRenderer extends StringRenderer {

    private String classes;

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
        NewMathMlMaterial mathMlMaterial = (NewMathMlMaterial) object;

        HtmlImage image = new HtmlImage();

        image.setSource(getContext().getViewState().getRequest().getContextPath() + "/mathml/MathMlServlet?oid="
                + mathMlMaterial.getExternalId());
        image.setTitle("Equation");

        image.setClasses(classes);

        return image;
    }

}
