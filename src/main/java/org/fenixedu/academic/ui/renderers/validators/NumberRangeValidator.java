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
package org.fenixedu.academic.ui.renderers.validators;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class NumberRangeValidator extends HtmlValidator {

    private boolean isNumber;

    private Integer upperBound;

    private Integer lowerBound;

    public NumberRangeValidator() {
        super();
        upperBound = null;
        lowerBound = null;
    }

    public NumberRangeValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);

        upperBound = null;
        lowerBound = null;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public void performValidation() {

        String numberText = getComponent().getValue();

        if (!StringUtils.isEmpty(numberText)) {
            try {
                int number = Integer.parseInt(numberText.trim());

                boolean inRange = true;
                isNumber = true;

                if (lowerBound != null) {
                    inRange &= lowerBound <= number;
                }

                if (upperBound != null) {
                    inRange &= upperBound >= number;
                }

                this.setValid(inRange);
            } catch (NumberFormatException e) {
                isNumber = false;
                setValid(false);
            }
        }
    }

    @Override
    public String getErrorMessage() {
        if (!isNumber) {
            return BundleUtil.getString(Bundle.RENDERER, "renderers.validator.number.integer");
        }

        if (lowerBound != null && upperBound != null) {
            return RenderUtils.getFormatedResourceString("renderers.validator.number.range.both", lowerBound, upperBound);
        }

        if (lowerBound != null) {
            return RenderUtils.getFormatedResourceString("renderers.validator.number.range.lower", lowerBound);
        }

        return RenderUtils.getFormatedResourceString("renderers.validator.number.range.upper", upperBound);
    }

}
