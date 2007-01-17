package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlLabel;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonList;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;

/**
 * The <code>EnumRadioInputRenderer</code> provides a way of doing the
 * input of an enum value by using a list of radio buttons. All the values
 * of that enum are presented as a radio button and the use can choose
 * one of the values.
 * 
 * <p>
 * Example: 
 * <ul>
 *  <li><input type="radio" name="same"/>Male</li>
 *  <li><input type="radio" name="same" checked="checked"/>Female</li>
 * </ul>
 * 
 * @author cfgi
 */
public class EnumRadioInputRenderer extends EnumInputRenderer {

    @Override
    protected void addEnumElement(Enum enumerate, HtmlSimpleValueComponent holder, Enum oneEnum, String description) {
	HtmlRadioButtonList radioList = (HtmlRadioButtonList) holder;
	
	HtmlText descriptionComponent = new HtmlText(description);
	HtmlLabel label = new HtmlLabel();
	label.setBody(descriptionComponent);
	
	HtmlRadioButton radioButton = radioList.addOption(label, oneEnum.toString());
	label.setFor(radioButton);
	
	if (oneEnum.equals(enumerate)) {
            radioButton.setChecked(true);
        }
    }

    @Override
    protected HtmlSimpleValueComponent createInputContainerComponent(Enum enumerate) {
	return new HtmlRadioButtonList();
    }
    
}
