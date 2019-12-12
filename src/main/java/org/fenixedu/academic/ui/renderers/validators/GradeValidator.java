/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on May 3, 2006
 */
package org.fenixedu.academic.ui.renderers.validators;


import org.fenixedu.academic.domain.curriculum.grade.GradeScale;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class GradeValidator extends HtmlValidator {

    private boolean required = false;

    public GradeValidator() {
        super();
        setMessage("renderers.validator.grade");
        setKey(true);
    }

    public GradeValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
        setMessage("renderers.validator.grade");
        setKey(true);
    }

    @Override
    public void performValidation() {
        final String grade = getComponent().getValue();

        if (isRequired() && (grade == null || grade.length() == 0)) {
            setValid(false);
            setKey(true);
            setMessage("renderers.validator.required");
        } else {
            if (grade == null || grade.length() == 0) {
                setValid(true);
            } else {
                setValid(false);
                
                GradeScale.findAll().forEach(e -> {
                    if(e.belongsTo(grade)) {
                        setValid(true);
                    }
                });
            }
        }
    }

    @Override
    protected String getResourceMessage(String message) {
        return RenderUtils.getFormatedResourceString(message, new Object[] { getComponent().getValue() });
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
