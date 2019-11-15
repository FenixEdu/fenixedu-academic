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
package org.fenixedu.academic.dto.accounting.postingRule;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.joda.time.LocalDate;

public class CreateGratuityPostingRuleBean extends CreatePostingRuleBean {

    private static final long serialVersionUID = 1L;

    private List<DegreeCurricularPlan> degreeCurricularPlans;

    public CreateGratuityPostingRuleBean() {
        this.degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>();
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlan each : this.degreeCurricularPlans) {
            result.add(each);
        }

        return result;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlan> variable) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlan each : variable) {
            result.add(each);
        }

        this.degreeCurricularPlans = result;
    }

}
