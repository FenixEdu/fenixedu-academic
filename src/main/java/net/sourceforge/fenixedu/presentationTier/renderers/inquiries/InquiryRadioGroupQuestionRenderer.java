/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
