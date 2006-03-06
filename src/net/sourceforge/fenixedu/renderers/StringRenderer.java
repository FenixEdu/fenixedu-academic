package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class StringRenderer extends OutputRenderer {

    private boolean isLink;

    private String linkText;
    
    public boolean isLink() {
        return this.isLink;
    }

    /**
     * @property
     */
    public void setLink(boolean makeLink) {
        this.isLink = makeLink;
    }

    public String getLinkText() {
        return this.linkText;
    }

    /**
     * @property
     */
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                String string = (String) object;
                
                if (! isLink() || string == null) {
                    return new HtmlText(string);
                }
                else {
                    HtmlLink link = new HtmlLink();
                    link.setContextRelative(false);
                    
                    if (getLinkText() == null) {
                        link.setText(string);
                    }
                    else {
                        link.setText(getLinkText());
                    }
                    
                    // heuristic to distinguish between email and other urls
                    if (string.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$")) {
                        link.setUrl("mailto:" + string);
                    }
                    else {
                        link.setUrl(string);
                    }
                    
                    return link;
                }
            }
            
        };
    }
}
