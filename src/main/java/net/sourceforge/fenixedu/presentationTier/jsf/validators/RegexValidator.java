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
/*
 * Created on Oct 11, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.jsf.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class RegexValidator implements Validator, StateHolder {
    private String regex;

    private boolean _transient = false;

    private final String INVALID_INPUT = "net.sourceforge.fenixedu.presentationTier.jsf.validators.INVALID_INPUT";

    @Override
    public void validate(FacesContext _context, UIComponent _component, Object _value) throws ValidatorException {

        String val = "";
        if (_value != null) {
            val = _value.toString();
        }
        if (!val.matches(regex)) {
            throw new ValidatorException(new FacesMessage(BundleUtil.getString(Bundle.APPLICATION, INVALID_INPUT)));        }
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public Object saveState(FacesContext context) {
        return getRegex();
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        setRegex((String) state);
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
