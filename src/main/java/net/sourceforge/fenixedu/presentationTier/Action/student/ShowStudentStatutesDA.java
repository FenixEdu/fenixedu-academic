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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@StrutsFunctionality(app = StudentViewApp.class, descriptionKey = "label.student.statutes", path = "statutes",
        titleKey = "link.title.statutes")
@Mapping(path = "/ShowStudentStatutes", module = "student")
@Forwards({ @Forward(name = "studentStatutes", path = "/student/showStudentStatutes.jsp", tileProperties = @Tile(
        title = "private.student.view.statutes")) })
public class ShowStudentStatutesDA extends FenixDispatchAction {

    @EntryPoint
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        User userView = getUserView(request);
        Student student = userView.getPerson().getStudent();
        ArrayList<StudentStatute> studentStatutes = new ArrayList<StudentStatute>(student.getStudentStatutes());
        Collections.sort(studentStatutes, new BeanComparator("beginExecutionPeriod.beginDateYearMonthDay"));
        request.setAttribute("studentStatutes", studentStatutes);
        return mapping.findForward("studentStatutes");
    }

}
