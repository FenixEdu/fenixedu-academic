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

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class InquiriesQuestion implements Serializable {

    private String label;

    private String value;

    private QuestionHeader header;

    private String toolTip;

    private Boolean required;

    private Boolean showRequiredMark = true;

    private Boolean newRow = true;

    private Boolean autofit = true;

    public InquiriesQuestion(final String label) {
        super();
        this.label = label;
    }

    public InquiriesQuestion(final String label, QuestionHeader header) {
        this(label);
        this.header = header;
    }

    public String getValue() {
        return value;
    }

    public Boolean getValueAsBoolean() {
        return getValue() != null && (Boolean.valueOf(getValue()) || getValue().equalsIgnoreCase("on"));
    }

    public Integer getValueAsInteger() {
        throw new DomainException("Value not convertible do Integer");
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InquiriesQuestion setInitialValue(final Object value) {
        setValue(value != null ? value.toString() : null);
        return this;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public QuestionHeader getHeader() {
        return header;
    }

    public InquiriesQuestion setHeader(QuestionHeader header) {
        this.header = header;
        return this;
    }

    public boolean hasHeader() {
        return this.header != null;
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(getValue());
    }

    public String getToolTip() {
        return toolTip;
    }

    public InquiriesQuestion setToolTip(String toolTip) {
        this.toolTip = toolTip;
        return this;
    }

    public boolean hasToolTip() {
        return !StringUtils.isEmpty(getToolTip());
    }

    public Boolean getRequired() {
        return required;
    }

    public InquiriesQuestion setRequired(Boolean required) {
        this.required = required;
        return this;
    }

    public Boolean getShowRequiredMark() {
        return showRequiredMark != null && showRequiredMark;
    }

    public InquiriesQuestion setShowRequiredMark(Boolean showRequiredMark) {
        this.showRequiredMark = showRequiredMark;
        return this;
    }

    public Boolean getNewRow() {
        return newRow;
    }

    public InquiriesQuestion setNewRow(Boolean newRow) {
        this.newRow = newRow;
        return this;
    }

    public Boolean getAutofit() {
        return autofit;
    }

    public InquiriesQuestion setAutofit(Boolean autofit) {
        this.autofit = autofit;
        return this;
    }

}
