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
package org.fenixedu.academic.ui.struts.action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionPR;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "prices", titleKey = "label.prices")
@Mapping(module = "student", path = "/prices")
@Forwards(@Forward(name = "viewPrices", path = "/student/administrativeOfficeServices/prices/viewPrices.jsp"))
public class StudentPricesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("postingRulesByAdminOfficeType", getPostingRulesByAdminOfficeType());
        request.setAttribute("insurancePostingRule", getInsurancePR());

        return mapping.findForward("viewPrices");
    }

    private PostingRule getInsurancePR() {
        return Bennu.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate()
                .findPostingRuleByEventType(EventType.INSURANCE);
    }

    private Map<AdministrativeOfficeType, List<PostingRule>> getPostingRulesByAdminOfficeType() {

        final Map<AdministrativeOfficeType, List<PostingRule>> postingRulesByAdminOfficeType =
                new HashMap<AdministrativeOfficeType, List<PostingRule>>();

        for (final AdministrativeOfficeType officeType : AdministrativeOfficeType.values()) {
            final AdministrativeOffice office = AdministrativeOffice.readByAdministrativeOfficeType(officeType);
            if (office != null) {
                final List<PostingRule> postingRules = new ArrayList<PostingRule>();
                for (PostingRule postingRule : office.getServiceAgreementTemplate().getActiveVisiblePostingRules()) {
                    if (!ExternalScholarshipPhdGratuityContribuitionPR.class.isAssignableFrom(postingRule.getClass())) {
                        postingRules.add(postingRule);
                    }

                }
                Collections.sort(postingRules, PostingRule.COMPARATOR_BY_EVENT_TYPE);
                postingRulesByAdminOfficeType.put(officeType, postingRules);
            }
        }
        return postingRulesByAdminOfficeType;
    }
}
