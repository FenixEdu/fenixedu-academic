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

import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * 
 * @author naat
 * 
 */
public class DateValidator implements Validator, StateHolder {
    private String format;

    private Boolean strict;

    private boolean _transient = false;

    private final String INVALID_DATE = "net.sourceforge.fenixedu.presentationTier.jsf.validators.dateValidator.INVALID_DATE";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value != null) {
            String val = value.toString();
            if (org.apache.commons.validator.DateValidator.getInstance().isValid(val, this.getFormat(), this.getStrict()) == false) {
                String errorMessage =
                        MessageFormat.format(BundleUtil.getString(Bundle.APPLICATION, INVALID_DATE),
                                new Object[] { this.getFormat() });

                throw new ValidatorException(new FacesMessage(errorMessage));
            }
        }
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

    @Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[2];
        state[0] = this.getFormat();
        state[1] = this.getStrict();
        return state;
    }

    @Override
    public void restoreState(FacesContext context, Object storedState) {
        Object[] state = (Object[]) storedState;
        setFormat((String) state[0]);
        setStrict((Boolean) state[1]);
    }

    @Override
    public boolean isTransient() {
        return _transient;
    }

    @Override
    public void setTransient(boolean arg0) {
        _transient = arg0;
    }
}
