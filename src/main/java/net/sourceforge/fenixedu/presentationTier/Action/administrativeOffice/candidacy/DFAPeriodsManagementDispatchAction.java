package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "masterDegreeAdministrativeOffice", path = "/dfaPeriodsManagement", attribute = "chooseExecutionYearForm",
        formBean = "chooseExecutionYearForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseExecutionYear", path = "dfa.periodsManagement.chooseExecutionYear"),
        @Forward(name = "editCandidacyPeriod", path = "dfa.periodsManagement.editCandidacyPeriod"),
        @Forward(name = "showExecutionDegrees", path = "dfa.periodsManagement.showExecutionDegrees"),
        @Forward(name = "editRegistrationPeriod", path = "dfa.periodsManagement.editRegistrationPeriod") })
public class DFAPeriodsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ((DynaActionForm) form).set("executionYear", ExecutionYear.readCurrentExecutionYear().getExternalId().toString());
        request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());

        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward showExecutionDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final List<ExecutionDegree> executionDegrees =
                getExecutionYear(request, (DynaActionForm) form).getExecutionDegreesFor(
                        DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("showExecutionDegrees");
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest request, final DynaActionForm dynaActionForm) {
        if (!StringUtils.isEmpty(dynaActionForm.getString("executionYear"))) {
            return FenixFramework.getDomainObject(dynaActionForm.getString("executionYear"));
        } else if (request.getParameter("executionYearId") != null) {
            return getDomainObject(request, "executionYearId");
        } else {
            return ExecutionYear.readCurrentExecutionYear();
        }
    }

    public ActionForward prepareEditCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionDegree executionDegree = getExecutionDegree(request);

        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("candidacyPeriod",
                executionDegree.getDegreeCurricularPlan().getCandidacyPeriod(executionDegree.getExecutionYear()));

        return mapping.findForward("editCandidacyPeriod");

    }

    public ActionForward prepareEditRegistrationPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionDegree executionDegree = getExecutionDegree(request);

        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("registrationPeriod",
                executionDegree.getDegreeCurricularPlan().getRegistrationPeriod(executionDegree.getExecutionYear()));

        return mapping.findForward("editRegistrationPeriod");

    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        return getDomainObject(request, "executionDegreeId");
    }

}
