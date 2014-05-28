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
package net.sourceforge.fenixedu.domain.exceptions;

import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;

public class EnrollmentDomainException extends DomainException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private RuleResult falseResult;

    public EnrollmentDomainException(String key, String... args) {
        super(key, args);
    }

    public EnrollmentDomainException(String key, Throwable cause, String... args) {
        super(key, cause, args);
    }

    public EnrollmentDomainException(final RuleResult falseRuleResult) {
        super();
        this.falseResult = falseRuleResult;
    }

    public RuleResult getFalseResult() {
        return this.falseResult;
    }

}
