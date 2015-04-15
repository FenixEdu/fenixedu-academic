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
/**
 * 
 */
package org.fenixedu.academic.ui.renderers.validators;

import java.text.ParseException;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.util.DateFormatUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.validators.DateValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DateInExecutionPeriodValidator extends DateValidator {

    private static final Logger logger = LoggerFactory.getLogger(DateInExecutionPeriodValidator.class);

    public DateInExecutionPeriodValidator() {
        super();
    }

    public DateInExecutionPeriodValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
    }

    public DateInExecutionPeriodValidator(HtmlChainValidator htmlChainValidator, String dateFormat) {
        super(htmlChainValidator, dateFormat);
    }

    @Override
    public void performValidation() {
        super.performValidation();

        if (isValid()) {
            try {
                DateTime dateTime = new DateTime(DateFormatUtil.parse(getDateFormat(), getComponent().getValue()).getTime());
                setValid(ExecutionSemester.readByDateTime(dateTime) != null);
                if (!isValid()) {
                    setMessage("renderers.validator.dateInExecutionPeriod.notInExecutionPeriod");
                }
            } catch (ParseException e) {
                setValid(false);
                logger.error(e.getMessage(), e);
            }
        }
    }

}
