package net.sourceforge.fenixedu.presentationTier.renderers.components;

import javax.servlet.jsp.PageContext;

import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.tags.HtmlTag;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class HtmlEditor extends HtmlSimpleValueComponent {

    private int width;
    private int heigth;
    
    public HtmlEditor() {
        super();
        
        setWidth(600);
        setHeigth(400);
    }

    public int getHeigth() {
        return this.heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private HtmlTag scriptTag(String script) {
        HtmlTag tag = new HtmlTag("script");
        
        tag.setAttribute("type", "text/javascript");
        tag.setAttribute("language", "Javascript");

        tag.setText("<!--\n" + script + "\n//-->");
        
        return tag;
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName(null);
        
        HtmlTag scriptTagInit  = scriptTag("initEditor();");
        HtmlTag scriptTagWrite = scriptTag(
                "writeRichText('" + getName() +"', '" + (getValue() == null ? "" : getValue().trim()) + "', " + 
                getWidth() + ", " + getHeigth() + ", true, false);" +
                "\n//-->");
        
        HtmlTag noscriptTag = new HtmlTag("noscript", RenderUtils.getResourceString("javascript.notSupported"));
        if (noscriptTag.getText() == null) {
            noscriptTag.setText("");
        }
        
        tag.addChild(noscriptTag);
        tag.addChild(scriptTagInit);
        tag.addChild(scriptTagWrite);
        
        return tag;
    }

}
