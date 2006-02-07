package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMultipleHiddenField extends HtmlMultipleValueComponent {

    public HtmlMultipleHiddenField(String name) {
        setName(name);
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);

        tag.setName(null);
        
        String[] values = getValues();
        for (int i = 0; i < values.length; i++) {
            HtmlHiddenField hidden = new HtmlHiddenField(getName(), values[i]);
            hidden.setTargetSlot(getTargetSlot());
            
            tag.addChild(hidden.getOwnTag(context));
        }
        
        return tag;
    }
}
