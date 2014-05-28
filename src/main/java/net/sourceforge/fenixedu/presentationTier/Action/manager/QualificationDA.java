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
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonInformationLog;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.FindPersonAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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

        Collection<PersonInformationLog> logsList = person.getPersonInformationLogs();
        request.setAttribute("person", person);
        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewStudentLogChanges");
    }
}