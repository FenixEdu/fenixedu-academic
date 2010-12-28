/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryCheckBoxQuestionRenderer extends InputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		//		InquiryQuestionDTO boxQuestion = (InquiryQuestionDTO) getContext().getProperties().get("checkBoxQuestion");
		////		HtmlCheckBoxList checkBoxList = new HtmlCheckBoxList();
		//		InquiryCheckBoxQuestion inquiryCheckBoxQuestion = (InquiryCheckBoxQuestion) boxQuestion.getInquiryQuestion();
		////		for (InquiryQuestionChoice questionChoice : inquiryCheckBoxQuestion.getOrderedInquiryQuestionChoices()) {
		//		    HtmlCheckBox option = new HtmlCheckBox(boxQuestion.getInquiryQuestion().getLabel().toString());
		//		    option.setName(boxQuestion.getInquiryQuestion().getLabel().toString() + ":" + boxQuestion.getInquiryQuestion().getExternalId());
		//		    option.setChecked(boxQuestion.getCheckboxResponses().contains(questionChoice.getLabel().toString()));
		////		}
		//		//checkBoxList.setConverter(new CheckboxConverter());
		//		return checkBoxList;
		//		final HtmlCheckBox htmlCheckBox = new HtmlCheckBox();
		//		if (object != null && (Boolean.valueOf(object.toString()) || object.toString().equalsIgnoreCase("on"))) {
		//		    htmlCheckBox.setChecked(true);
		//		}
		//		return htmlCheckBox;

		final HtmlCheckBox htmlCheckBox = new HtmlCheckBox();
		htmlCheckBox.setName("1");
		if (object != null && (Boolean.valueOf(object.toString()) || object.toString().equalsIgnoreCase("on"))) {
		    htmlCheckBox.setChecked(true);
		}
		return htmlCheckBox;
	    }
	};
    }
}
