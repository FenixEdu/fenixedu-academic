package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMultipleHiddenField extends HtmlMultipleValueComponent {

    public HtmlMultipleHiddenField(String name) {
        super();
        
        setName(name);
    }

    public HtmlMultipleHiddenField() {
        super();
    }

    public void addValue(String value) {
        String[] values = getValues();
        
        if (values == null) {
            setValues(new String[] { value });
        }
        else {
            String[] newValues = new String[values.length + 1];
            
            for (int i = 0; i < values.length; i++) {
                newValues[i] = values[i];
            }
            
            newValues[values.length] = value;
            setValues(newValues);
        }
    }
    
    public void removeValue(int i) {
        String[] values = getValues();
        
        if (values == null) {
            return;
        }
        
        if (i < 0 || i >= values.length) {
            return;
        }
        
        if (values.length == 1) {
            setValues((String[]) null);
            return;
        }
        
        String[] newValues = new String[values.length - 1];
        for (int j = 0; j < newValues.length; j++) {
            if (j < i) {
                newValues[j] = values[j];
            }
            else {
                newValues[j] = values[j + 1];
            }
        }
        
        setValues(newValues);
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
