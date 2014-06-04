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
package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import java.io.IOException;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.ExternalSupervisionApplication.ExternalSupervisionConsultApp;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = ExternalSupervisionConsultApp.class, path = "year", titleKey = "label.selectDegree.executionYear")
@Mapping(path = "/viewYear", module = "externalSupervision")
@Forwards({ @Forward(name = "selectYear", path = "/externalSupervision/consult/selectYear.jsp") })
public class ExternalSupervisorViewYearDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward beginTaskFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final User userView = Authenticate.getUser();
        final Person supervisor = userView.getPerson();

        RegistrationProtocol protocol = supervisor.getOnlyRegistrationProtocol();
        ExternalSupervisorViewsBean bean;

        if (protocol == null) {
            bean = new ExternalSupervisorViewsBean();
            bean.setMegavisor(true);
            boolean selectProtocol = true;
            request.setAttribute("selectProtocol", selectProtocol);

        } else {
            bean = new ExternalSupervisorViewsBean(protocol);
        }

        request.setAttribute("sessionBean", bean);
        return mapping.findForward("selectYear");
    }

    public ActionForward showStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
        RenderUtils.invalidateViewState();

        if (bean == null) {
            final String registrationProtocolId = request.getParameter("registrationProtocolId");
            RegistrationProtocol registrationProtocol = FenixFramework.getDomainObject(registrationProtocolId);

            final String executionYearId = request.getParameter("executionYearId");
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearId);

            Boolean megavisor = Boolean.valueOf(request.getParameter("megavisor"));

            bean = new ExternalSupervisorViewsBean(executionYear, registrationProtocol);
            bean.setMegavisor(megavisor);
        }

        if (bean.getMegavisor()) {
            boolean selectProtocol = true;
            request.setAttribute("selectProtocol", selectProtocol);
        }

        bean.generateStudentsFromYear();
        Boolean yearSelected = true;

        request.setAttribute("sessionBean", bean);
        request.setAttribute("hasChosenYear", yearSelected);

        return mapping.findForward("selectYear");
    }

    public ActionForward exportXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
        final Spreadsheet spreadsheet = generateSpreadsheet(bean);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getFilename(bean) + ".xls");
        spreadsheet.exportToXLSSheet(response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return null;
    }

    private String getFilename(ExternalSupervisorViewsBean bean) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(BundleUtil.getString(Bundle.APPLICATION, "label.students"));
        strBuilder.append("_");
        strBuilder.append(bean.getProtocol().getRegistrationAgreement().getName());
        strBuilder.append("_");
        strBuilder.append(bean.getExecutionYear().getName());
        return strBuilder.toString();
    }

    private Spreadsheet generateSpreadsheet(ExternalSupervisorViewsBean bean) {
        final Spreadsheet spreadsheet = createSpreadSheet();
        for (final StudentCurricularPlan studentCurricularPlan : bean.generateAllStudentCurricularPlans()) {
            final Row row = spreadsheet.addRow();

            row.setCell(studentCurricularPlan.getPerson().getUsername());
            row.setCell(studentCurricularPlan.getRegistration().getNumber());
            row.setCell(studentCurricularPlan.getPerson().getName());
            row.setCell(studentCurricularPlan.getPerson().getInstitutionalOrDefaultEmailAddressValue());
            row.setCell(studentCurricularPlan.getPerson().getIdDocumentType().getLocalizedName());
            row.setCell(studentCurricularPlan.getPerson().getDocumentIdNumber());
            row.setCell(studentCurricularPlan.getDegree().getSigla());
            row.setCell(studentCurricularPlan.getName());
            row.setCell(studentCurricularPlan.getStartDateYearMonthDay().toString());
            row.setCell(studentCurricularPlan.getEndDate() == null ? "" : studentCurricularPlan.getEndDate().toString());
            row.setCell(studentCurricularPlan.getRegistration().getActiveStateType().getDescription());
            row.setCell(studentCurricularPlan.getRegistration().getNumberOfCurriculumEntries());
            row.setCell(studentCurricularPlan.getRegistration().getEctsCredits());
            row.setCell(getAverageInformation(studentCurricularPlan));
            row.setCell(studentCurricularPlan.getRegistration().getCurricularYear());

        }

        return spreadsheet;
    }

    private String getAverageInformation(final StudentCurricularPlan studentCurricularPlan) {
        final Registration registration = studentCurricularPlan.getRegistration();

        if (registration.isConcluded()) {
            if (registration.isRegistrationConclusionProcessed()
                    && (!registration.isBolonha() || studentCurricularPlan.getInternalCycleCurriculumGroupsSize().intValue() == 1)) {
                return registration.getAverage().setScale(2, RoundingMode.HALF_EVEN).toPlainString();
            } else {
                return " - ";
            }
        } else {
            return registration.getAverage().setScale(2, RoundingMode.HALF_EVEN).toPlainString();
        }
    }

    private Spreadsheet createSpreadSheet() {
        final Spreadsheet spreadsheet = new Spreadsheet(BundleUtil.getString(Bundle.APPLICATION, "list.students"));

        spreadsheet.setHeaders(new String[] {

        BundleUtil.getString(Bundle.APPLICATION, "label.istid"),

        BundleUtil.getString(Bundle.APPLICATION, "label.number"),

        BundleUtil.getString(Bundle.APPLICATION, "label.name"),

        BundleUtil.getString(Bundle.APPLICATION, "label.email"),

        BundleUtil.getString(Bundle.APPLICATION, "label.identificationDocumentType"),

        BundleUtil.getString(Bundle.APPLICATION, "label.identificationDocumentNumber"),

        BundleUtil.getString(Bundle.APPLICATION, "label.Degree"),

        BundleUtil.getString(Bundle.APPLICATION, "label.curricularPlan"),

        BundleUtil.getString(Bundle.APPLICATION, "label.net.sourceforge.fenixedu.domain.student.Registration.startDate"),

        BundleUtil.getString(Bundle.APPLICATION, "label.net.sourceforge.fenixedu.domain.student.Registration.conclusionDate"),

        BundleUtil.getString(Bundle.APPLICATION, "label.student.curricular.plan.state"),

        BundleUtil.getString(Bundle.APPLICATION, "label.number.approved.curricular.courses"),

        BundleUtil.getString(Bundle.APPLICATION, "label.ects"),

        BundleUtil.getString(Bundle.APPLICATION, "label.average"),

        BundleUtil.getString(Bundle.APPLICATION, "label.student.curricular.year"),

        " ", " " });

        return spreadsheet;
    }

}
