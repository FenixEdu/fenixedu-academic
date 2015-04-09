package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

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

        public RegistrationDataByYearBean(RegistrationDataByExecutionYear dataByExecutionYear) {
            this.dataByExecutionYear = dataByExecutionYear;
            this.enrolmentDate = dataByExecutionYear.getEnrolmentDate();
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
        bean.getDataByExecutionYear().edit(bean.getEnrolmentDate());
    }

}
