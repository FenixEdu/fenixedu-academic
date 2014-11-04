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
package org.fenixedu.academic.ui.struts.action.manager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonInformationLog;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.personManagement.FindPersonAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/qualification", module = "manager", functionality = FindPersonAction.class)
@Forwards({ @Forward(name = "showQualifications", path = "/manager/qualifications/showQualifications.jsp"),
        @Forward(name = "qualification", path = "/manager/qualifications/qualification.jsp"),
        @Forward(name = "viewPerson", path = "/manager/personManagement/viewPerson.jsp"),
        @Forward(name = "viewStudentLogChanges", path = "/manager/personManagement/viewStudentLogChanges.jsp") })
public class QualificationDA extends FenixDispatchAction {

    public ActionForward showQualifications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setAttributePerson(request);
        return mapping.findForward("showQualifications");
    }

    public ActionForward prepareCreateQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setAttributePerson(request);
        return mapping.findForward("qualification");
    }

    public ActionForward prepareEditQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Qualification qualification = getQualificationFromParameter(request);
        request.setAttribute("qualification", qualification);
        setAttributePerson(request);
        return mapping.findForward("qualification");
    }

    @Atomic
    public ActionForward deleteQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Qualification qualification = getQualificationFromParameter(request);
        qualification.delete();
        return backToShowQualifications(mapping, actionForm, request, response);
    }

    public ActionForward backToShowQualifications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setAttributePerson(request);
        return mapping.findForward("showQualifications");
    }

    public ActionForward backToViewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setAttributePerson(request);
        return mapping.findForward("viewPerson");
    }

    protected Person getPersonSelectedFromParameter(HttpServletRequest request) {
        String personIDString = request.getParameter("personID");
        return FenixFramework.getDomainObject(personIDString);

    }

    protected Qualification getQualificationFromParameter(HttpServletRequest request) {
        String qualificationIDString = request.getParameter("qualificationId");
        return FenixFramework.getDomainObject(qualificationIDString);
    }

    private void setAttributePerson(HttpServletRequest request) {
        Person person = getPersonSelectedFromParameter(request);
        request.setAttribute("person", person);
    }

    public ActionForward viewStudentLog(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = getPersonSelectedFromParameter(request);

        Collection<PersonInformationLog> logsList = person.getPersonInformationLogsSet();
        request.setAttribute("person", person);
        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewStudentLogChanges");
    }
}