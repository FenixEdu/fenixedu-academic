package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlRadioButtonGroup extends HtmlSimpleValueComponent {

    private List<HtmlRadioButton> radioButtons;

    public HtmlRadioButtonGroup() {
        super();
        
        this.radioButtons = new ArrayList<HtmlRadioButton>();
    }

    public List<HtmlRadioButton> getRadioButtons() {
        return this.radioButtons;
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        
        for (HtmlRadioButton radio : getRadioButtons()) {
            if (value != null && value.equals(radio.getValue())) {
                radio.setChecked(true);
            }
            else {
                radio.setChecked(false);
            }
        }
    }

    public HtmlRadioButton createRadioButton() {
        HtmlRadioButton radio = new HtmlRadioButton() {

            @Override
            public void setChecked(boolean checked) {
                HtmlRadioButtonGroup.this.setChecked(this, checked);
                
                super.setChecked(checked);
            }
            
        };
        
        getRadioButtons().add(radio);
        radio.setName(getName());
        
        return radio;
    }

    protected void setChecked(HtmlRadioButton button, boolean checked) {
        if (! checked) {
            return;
        }

        for (HtmlRadioButton radio : getRadioButtons()) {
            if (radio.equals(button)) {
                continue;
            }
            
            radio.setChecked(false);
        }
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        for (HtmlRadioButton radio : getRadioButtons()) {
            radio.setName(getName());
            
            if (getTargetSlot() != null) {
                radio.setTargetSlot(getTargetSlot());
            }
        }

        return new HtmlText().getOwnTag(context);
    }
}