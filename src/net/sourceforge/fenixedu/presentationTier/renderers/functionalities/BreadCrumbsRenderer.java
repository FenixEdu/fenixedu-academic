package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * This renderer renderes bread crumbs based on the current functionalities
 * context.
 * 
 * @author cfgi
 */
public class BreadCrumbsRenderer extends OutputRenderer {

    private String separator;
    
    public BreadCrumbsRenderer() {
        super();
        
        setSeparator("&gt;");
    }

    public String getSeparator() {
        return this.separator;
    }

    /**
     * The text separator used when separating the different links. By default
     * &gt; is used.
     * 
     * @param separator
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                FunctionalityContext context = (FunctionalityContext) object;

                if (context == null) {
                    return new HtmlText();
                }
                
                if (context.getSelectedFunctionality() == null) {
                    return new HtmlText();
                }

                HtmlContainer container = new HtmlInlineContainer();
                addCrumbs(context, container, context.getSelectedFunctionality());
                
                return container;
            }

            private void addCrumbs(FunctionalityContext context, HtmlContainer container, Functionality functionality) {
                if (functionality.getModule() != null) {
                    addCrumbs(context, container, functionality.getModule());
                    container.addChild(new HtmlText(getSeparator(), false));
                }
                
                boolean linkable = !context.getSelectedFunctionality().equals(functionality);
                HtmlComponent component = MenuRenderer.getFunctionalityNameComponent(context, functionality, linkable);
                container.addChild(component);
            }
            
        };
    }

}
