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
package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ExternalSupervisionResources", path = "external-supervision", titleKey = "externalSupervision",
        hint = "External Supervision", accessGroup = "role(EXTERNAL_SUPERVISOR)")
@Mapping(path = "/welcome", module = "externalSupervision")
@Forwards({ @Forward(name = "welcome", path = "/externalSupervision/externalSupervisionGreetings.jsp"),
        @Forward(name = "welcome_AFA", path = "/externalSupervision/externalSupervisionGreetingsAFA.jsp"),
        @Forward(name = "welcome_MA", path = "/externalSupervision/externalSupervisionGreetingsMA.jsp"),
        @Forward(name = "selectProtocol", path = "/externalSupervision/selectProtocol.jsp") })
public class ExternalSupervisionApplication extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        RegistrationProtocol registrationProtocol = AccessControl.getPerson().getOnlyRegistrationProtocol();
        if (registrationProtocol == null) {
            return mapping.findForward("welcome");
        }

        switch (registrationProtocol.getRegistrationAgreement()) {
        case AFA:
            return mapping.findForward("welcome_AFA");
        case MA:
            return mapping.findForward("welcome_MA");
        default:
            return mapping.findForward("welcome");
        }
    }

    @StrutsApplication(bundle = "ExternalSupervisionResources", path = "consult", titleKey = "button.consult",
            hint = "External Supervision", accessGroup = "role(EXTERNAL_SUPERVISOR)")
    public static class ExternalSupervisionConsultApp {

    }

}
