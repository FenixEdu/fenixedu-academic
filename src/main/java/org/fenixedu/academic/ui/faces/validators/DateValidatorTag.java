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
package net.sourceforge.fenixedu.presentationTier.jsf.validators;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * 
 * @author naat
 * 
 */
public class DateValidatorTag extends ValidatorTag {

    /**
     * 
     */
    private static final long serialVersionUID = 2570906320417113147L;

    private String format;

    private Boolean strict;

    public DateValidatorTag() {
        super();
        super.setValidatorId("dateValidator");
    }

    @Override
    protected Validator createValidator() throws JspException {
        DateValidator dateValidator = (DateValidator) super.createValidator();
        dateValidator.setFormat(getFormat());
        dateValidator.setStrict(getStrict());

        return dateValidator;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getStrict() {
        return strict;
    }

    public void setStrict(Boolean strict) {
        this.strict = strict;
    }

}
