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
/**
 * Jan 16, 2006
 */
package org.fenixedu.academic.ui.struts.action.credits;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.credits.ReadAllTeacherCredits;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.CreditLine;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCreditsResumeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherCreditsResume");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        User userView = Authenticate.getUser();

        DynaActionForm dynaForm = (DynaActionForm) form;
        String teacherId = dynaForm.getString("teacherId");

        Teacher teacher = Teacher.readByIstId(teacherId);
        dynaForm.set("teacherId", teacher.getExternalId());
        request.setAttribute("teacher", teacher);

        List<CreditLine> creditsLines = (List) ReadAllTeacherCredits.run(teacher.getExternalId());
        request.setAttribute("creditsLinesSize", creditsLines.size());

        BeanComparator dateComparator = new BeanComparator("executionPeriod.beginDate");
        Iterator orderedCreditsLines = new OrderedIterator(creditsLines.iterator(), dateComparator);

        request.setAttribute("creditsLines", orderedCreditsLines);
        return mapping.findForward("show-all-credits-resume");
    }

}