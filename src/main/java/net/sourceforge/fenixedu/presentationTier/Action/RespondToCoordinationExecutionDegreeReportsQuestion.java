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
package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/respondToCoordinationExecutionDegreeReportsQuestion")
public class RespondToCoordinationExecutionDegreeReportsQuestion extends FenixDispatchAction {

    public final ActionForward showQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final InquiryResponsePeriod lastPeriod = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.COORDINATOR);
        request.setAttribute("executionPeriod", lastPeriod == null ? null : lastPeriod.getExecutionPeriod());
        request.setAttribute("executionDegrees", AccessControl.getPerson().getCoordinationExecutionDegreeReportsToAnswer());
        return new ActionForward("/respondToCoordinationExecutionDegreeReportsQuestion.jsp");
    }

    private ActionForward forward(final String path) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setPath(path);
        actionForward.setRedirect(true);
        return actionForward;
    }

    public final ActionForward respondLater(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return forward("/home.do");
    }

}