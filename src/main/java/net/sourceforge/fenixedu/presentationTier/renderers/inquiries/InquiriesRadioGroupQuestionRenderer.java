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

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.QuestionChoice;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonGroup;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

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
