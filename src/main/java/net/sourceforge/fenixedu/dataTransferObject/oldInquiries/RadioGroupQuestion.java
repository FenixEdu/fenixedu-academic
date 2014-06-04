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
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RadioGroupQuestion extends InquiriesQuestion {

    private boolean showValues = false;

    private List<QuestionChoice> choices = new ArrayList<QuestionChoice>();

    private RadioGroupQuestion(String label, boolean showValues, QuestionHeader header) {
        super(label, header);
        this.showValues = showValues;
    }

    public RadioGroupQuestion(String label, int minValue, int maxValue, boolean showValues, QuestionHeader header) {
        this(label, showValues, header);
        for (int i = minValue; i <= maxValue; i++) {
            addChoice(String.valueOf(i), String.valueOf(i));
        }
    }

    public RadioGroupQuestion(String label, Class enumClass, boolean showValues, QuestionHeader header) {
        this(label, showValues, header);
        for (Enum enumConstant : (Enum[]) enumClass.getEnumConstants()) {
            addChoice(enumConstant.name(),
                    BundleUtil.getString(Bundle.ENUMERATION, enumConstant.name()));
        }
    }

    public RadioGroupQuestion(String label, boolean showValues, String... values) {
        this(label, showValues, (QuestionHeader) null);
        for (String value : values) {
            addChoice(value, value);
        }
    }

    public RadioGroupQuestion(String label, int minValue, int maxValue, boolean showValues) {
        this(label, minValue, maxValue, showValues, null);
    }

    public RadioGroupQuestion(String label, Class enumClass, boolean showValues) {
        this(label, enumClass, showValues, null);
    }

    public RadioGroupQuestion addChoice(String value, String label) {
        this.choices.add(new QuestionChoice(value, label, this.showValues));
        return this;
    }

    public boolean isShowValues() {
        return showValues;
    }

    public List<QuestionChoice> getChoices() {
        return choices;
    }

    @Override
    public Integer getValueAsInteger() {
        try {
            return getValue() != null ? Integer.valueOf(getValue()) : null;
        } catch (final NumberFormatException ex) {
            return null;
        }
    }

}
