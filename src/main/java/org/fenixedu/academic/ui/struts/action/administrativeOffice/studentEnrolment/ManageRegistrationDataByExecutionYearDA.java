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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.EventTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Mapping(path = "/manageRegistrationDataByExecutionYear", module = "academicAdministration",
        functionality = SearchForStudentsDA.class)
@Forwards({

@Forward(
        name = "edit",
        path = "/academicAdminOffice/student/registration/manageRegistrationDataByExecutionYear/editRegistrationDataByExecutionYear.jsp"), })
public class ManageRegistrationDataByExecutionYearDA extends FenixDispatchAction {

    public static class RegistrationDataByYearBean implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        private final RegistrationDataByExecutionYear dataByExecutionYear;

        private LocalDate enrolmentDate;

        private EventTemplate eventTemplate;

        public RegistrationDataByYearBean(RegistrationDataByExecutionYear dataByExecutionYear) {
            this.dataByExecutionYear = dataByExecutionYear;
            this.enrolmentDate = dataByExecutionYear.getEnrolmentDate();
            this.eventTemplate = dataByExecutionYear.getEventTemplate();
            if (eventTemplate == null) {
               eventTemplate = dataByExecutionYear.getRegistration().getEventTemplate();
            }
        }

        public LocalDate getEnrolmentDate() {
            return enrolmentDate;
        }

        public void setEnrolmentDate(LocalDate enrolmentDate) {
            this.enrolmentDate = enrolmentDate;
        }

        public RegistrationDataByExecutionYear getDataByExecutionYear() {
            return dataByExecutionYear;
        }

        public EventTemplate getEventTemplate() {
            return eventTemplate;
        }

        public void setEventTemplate(final EventTemplate eventTemplate) {
            this.eventTemplate = eventTemplate;
        }

    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final RegistrationDataByExecutionYear dataByExecutionYear = getDomainObject(request, "registrationDataByExecutionYearId");
        request.setAttribute("dataByExecutionYearBean", new RegistrationDataByYearBean(dataByExecutionYear));

        return mapping.findForward("edit");
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("dataByExecutionYearBean", getRenderedObject("dataByExecutionYearBean"));

        return mapping.findForward("edit");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final RegistrationDataByYearBean bean = getRenderedObject("dataByExecutionYearBean");

        try {
            editService(bean);
        } catch (DomainException e) {
            addErrorMessage(request, "error", e.getKey(), e.getArgs());
            request.setAttribute("dataByExecutionYearBean", bean);

            return mapping.findForward("edit");
        }

        return redirect("/student.do?method=visualizeRegistration&registrationID="
                + bean.getDataByExecutionYear().getRegistration().getExternalId(), request);
    }

    @Atomic
    private void editService(RegistrationDataByYearBean bean) {
        bean.getDataByExecutionYear().edit(bean.getEnrolmentDate(), bean.getEventTemplate());
    }

}
