/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryQuestionDTO;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestionHeader;
import net.sourceforge.fenixedu.domain.inquiries.InquiryRadioGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.QuestionScale;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonGroup;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryRadioGroupQuestionRenderer extends InputRenderer {

    private QuestionScale questionScale;

    public QuestionScale getQuestionScale() {
        return questionScale;
    }

    public void setQuestionScale(QuestionScale questionScale) {
        this.questionScale = questionScale;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                final InquiryQuestionDTO radioQuestion = (InquiryQuestionDTO) getContext().getProperties().get("radioQuestion");
                final InquiryQuestionHeader questionHeader =
                        (InquiryQuestionHeader) getContext().getProperties().get("questionHeader");
                final Boolean readOnly = (Boolean) getContext().getProperties().get("readOnly");
                QuestionScale choices = questionHeader.getScaleHeaders();
                HtmlRadioButtonGroup group = new HtmlRadioButtonGroup();
                for (int iter = 0; iter < choices.getScaleLength(); iter++) {
                    HtmlRadioButton button = group.createRadioButton();
                    button.setUserValue(choices.getScaleValues()[iter]);
                    if (!((InquiryRadioGroupQuestion) radioQuestion.getInquiryQuestion()).getIsMatrix()) {
                        button.setText(choices.getScale()[iter].toString());
                    }
                    if (!StringUtils.isEmpty(radioQuestion.getResponseValue())
                            && choices.getScaleValues()[iter].equals(radioQuestion.getResponseValue())) {
                        button.setChecked(true);
                    }
                    if (readOnly) {
                        button.setOnClick("return false;");
                    }
                }
                return group;
            }
        };
    }
}
