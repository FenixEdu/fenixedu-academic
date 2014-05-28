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

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TextBoxQuestion extends InquiriesQuestion {

    private Boolean textArea;

    private boolean integer = false;

    public TextBoxQuestion(String label, Boolean textArea) {
        super(label);
        this.textArea = textArea;
    }

    public TextBoxQuestion(String label, QuestionHeader header) {
        super(label, header);
    }

    public Boolean isTextArea() {
        return textArea != null && textArea;
    }

    @Override
    public Integer getValueAsInteger() {
        try {
            return getValue() != null ? Integer.valueOf(getValue()) : null;
        } catch (final NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public void setValue(String value) {
        if (integer) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return;
            }
        }
        super.setValue(value);
        return;
    }

    public boolean isInteger() {
        return integer;
    }

    public TextBoxQuestion setInteger(boolean integer) {
        this.integer = integer;
        setValue("0");
        return this;
    }

}
