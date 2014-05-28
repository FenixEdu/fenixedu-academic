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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/tutorshipSummaryPeriod", module = "pedagogicalCouncil", functionality = TutorshipSummaryDA.class)
@Forwards({ @Forward(name = "createPeriod", path = "/pedagogicalCouncil/tutorship/createPeriod.jsp"),
        @Forward(name = "confirmMessagePeriod", path = "/pedagogicalCouncil/tutorship/confirmCreatePeriod.jsp") })
public class TutorshipSummaryPeriodDA extends FenixDispatchAction {

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        RenderUtils.invalidateViewState();

        TutorshipSummaryPeriodBean bean = new TutorshipSummaryPeriodBean();
        setTutorshipSummaryPeriodBean(request, bean);

        return mapping.findForward("createPeriod");
    }

    public ActionForward prepareCreate2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TutorshipSummaryPeriodBean bean = getTutorshipSummaryPeriodBean();

        if (bean == null) {
            return prepareCreate(mapping, actionForm, request, response);
        }

        setTutorshipSummaryPeriodBean(request, bean);

        return mapping.findForward("createPeriod");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TutorshipSummaryPeriodBean bean = getTutorshipSummaryPeriodBean();

        if (bean != null && bean.isValid()) {
            bean.save();

            return mapping.findForward("confirmMessagePeriod");
        }

        return prepareCreate2(mapping, actionForm, request, response);
    }

    protected TutorshipSummaryPeriodBean getTutorshipSummaryPeriodBean() {
        return getRenderedObject("periodBean");
    }

    protected void setTutorshipSummaryPeriodBean(HttpServletRequest request, TutorshipSummaryPeriodBean bean) {
        request.setAttribute("periodBean", bean);
    }
}
