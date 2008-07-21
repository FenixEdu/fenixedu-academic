/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionChoice;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonGroup;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.validators.RequiredValidator;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesRadioGroupQuestionRenderer extends InputRenderer {

    private List<QuestionChoice> radioGroupChoices;

    public List<QuestionChoice> getRadioGroupChoices() {
	return radioGroupChoices;
    }

    public void setRadioGroupChoices(List<QuestionChoice> radioGroupChoices) {
	this.radioGroupChoices = radioGroupChoices;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		List<QuestionChoice> choices = (List<QuestionChoice>) getContext().getProperties().get("radioGroupChoices");

		HtmlRadioButtonGroup group = new HtmlRadioButtonGroup();
		for (int i = 0; i < choices.size(); i++) {
		    HtmlRadioButton button = group.createRadioButton();
		    final QuestionChoice choice = choices.get(i);
		    button.setUserValue(choice.getValue());
		    if (choice.isShowLabel()) {
			button.setText(choice.getLabel());
		    }
		    if (object != null && object.equals(choice.getValue())) {
			button.setChecked(true);
		    }
		}
		
		return group;
	    }

	};
    }
}
