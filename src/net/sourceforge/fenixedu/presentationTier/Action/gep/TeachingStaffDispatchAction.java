/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TeachingStaffDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        request.setAttribute("executionYears", getExecutionYears(userView));

        return mapping.findForward("chooseExecutionYearAndDegreeCurricularPlan");
    }

    public ActionForward selectExecutionYear(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        Integer executionYearID = executionYear.getIdInternal();
        Object[] args = { executionYearID };

        List degreeCurricularPlans = (List) ServiceUtils.executeService(userView,
                "ReadActiveDegreeCurricularPlansByExecutionYear", args);
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoDegree.tipoCurso"));
        comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
        Collections.sort(degreeCurricularPlans, comparatorChain);

        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
        request.setAttribute("executionYears", getExecutionYears(userView));

        return mapping.findForward("chooseExecutionYearAndDegreeCurricularPlan");
    }

    public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Integer degreeCurricularPlanID = (Integer) dynaActionForm.get("degreeCurricularPlanID");
        Integer executionYearID = (Integer) dynaActionForm.get("executionYearID");

        Object[] argsScopes = { degreeCurricularPlanID, executionYearID };
        Set<CurricularCourseScope> scopes = (Set<CurricularCourseScope>) ServiceUtils
                .executeService(userView,
                        "ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID",
                        argsScopes);
        
        SortedSet<CurricularCourseScope> sortedScopes = new TreeSet<CurricularCourseScope>(CurricularCourseScope.CURRICULAR_COURSE_NAME_COMPARATOR);
        sortedScopes.addAll(scopes);

        Object[] argsExecutionYear = { executionYearID };
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
                "ReadExecutionYearByID", argsExecutionYear);

        request.setAttribute("curricularCourseScopes", sortedScopes);
        request.setAttribute("executionYear", infoExecutionYear.getYear());

        return mapping.findForward("chooseExecutionCourse");
    }

    public ActionForward viewTeachingStaff(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionCourseID = Integer.valueOf(request.getParameter("executionCourseID"));

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

        List institutions = (List) ServiceUtils.executeService(userView, "ReadAllInstitutions", null);
        Collections.sort(institutions, new BeanComparator("name"));

        request.setAttribute("professorships", executionCourse.getProfessorships());
        request.setAttribute("institutions", institutions);
        request.setAttribute("nonAffiliatedTeachers", executionCourse.getNonAffiliatedTeachers());

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        dynaActionForm.set("executionCourseID", executionCourseID);
        dynaActionForm.set("nonAffiliatedTeacherID", null);

        return mapping.findForward("viewTeachingStaff");
    }

    public ActionForward createNewNonAffiliatedTeacher(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        String nonAffiliatedTeacherName = (String) dynaActionForm.get("nonAffiliatedTeacherName");
        Integer nonAffiliatedTeacherID = (Integer) dynaActionForm.get("nonAffiliatedTeacherID");
        Integer nonAffiliatedTeacherInstitutionID = (Integer) dynaActionForm
                .get("nonAffiliatedTeacherInstitutionID");
        String nonAffiliatedTeacherInstitutionName = (String) dynaActionForm
                .get("nonAffiliatedTeacherInstitutionName");

        if (nonAffiliatedTeacherID == 0 && nonAffiliatedTeacherName.length() == 0) {
            // define a teacher name!

            return viewTeachingStaff(mapping, actionForm, request, response);
        }

        if (nonAffiliatedTeacherInstitutionID == 0 && nonAffiliatedTeacherInstitutionName.length() == 0) {
            // define an institution!

            return viewTeachingStaff(mapping, actionForm, request, response);
        }

        if (nonAffiliatedTeacherInstitutionID == 0) {
            // create institution
            Object[] args = { nonAffiliatedTeacherInstitutionName };
            try {
                Unit institution = (Unit) ServiceUtils.executeService(userView,
                        "InsertInstitution", args);
                nonAffiliatedTeacherInstitutionID = institution.getIdInternal();
            } catch (ExistingServiceException e) {
                // define message error...
                InfoInstitution infoInstitution = (InfoInstitution) ServiceUtils.executeService(
                        userView, "ReadInstitutionByName", args);
                nonAffiliatedTeacherInstitutionID = infoInstitution.getIdInternal();
            }
        }

        if (nonAffiliatedTeacherID == 0) {
            // create non affiliated teacher
            Object[] args = { nonAffiliatedTeacherName, nonAffiliatedTeacherInstitutionID };
            try {
                NonAffiliatedTeacher nonAffiliatedTeacher = (NonAffiliatedTeacher) ServiceUtils
                        .executeService(userView, "InsertNonAffiliatedTeacher", args);
                nonAffiliatedTeacherID = nonAffiliatedTeacher.getIdInternal();
            } catch (NotExistingServiceException e) {
                // define message error...
                return viewTeachingStaff(mapping, actionForm, request, response);
            }

        }

        Integer executionCourseID = (Integer) dynaActionForm.get("executionCourseID");
        request.setAttribute("executionCourseID", executionCourseID);
        request.setAttribute("nonAffiliatedTeacherID", nonAffiliatedTeacherID);

        return insertNonAffiliatedTeacher(mapping, actionForm, request, response);

    }

    public ActionForward insertNonAffiliatedTeacher(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionCourseID = Integer.valueOf(request.getParameter("executionCourseID"));
        request.setAttribute("executionCourseID", executionCourseID);

        Integer nonAffiliatedTeacherID = null;

        String nonAffiliatedTeacherIDString = request.getParameter("nonAffiliatedTeacherID");
        if (nonAffiliatedTeacherIDString != null && nonAffiliatedTeacherIDString.length() > 0
                && !nonAffiliatedTeacherIDString.equals("0")) {
            nonAffiliatedTeacherID = Integer.valueOf(nonAffiliatedTeacherIDString);
        } else {
            nonAffiliatedTeacherID = (Integer) request.getAttribute("nonAffiliatedTeacherID");
        }

        // InsertProfessorShipNonAffiliatedTeacher
        Object[] args = { nonAffiliatedTeacherID, executionCourseID };

        try {
            ServiceUtils.executeService(userView, "InsertProfessorShipNonAffiliatedTeacher", args);
        } catch (ExistingServiceException e) {
            // TODO Auto-generated catch block
            // error message!!
            e.printStackTrace();
        }

        return viewTeachingStaff(mapping, actionForm, request, response);
    }

    private List getExecutionYears(IUserView userView) throws FenixServiceException,
            FenixFilterException {
        List executionYears = (List) ServiceUtils.executeService(userView,
                "ReadNotClosedExecutionYears", null);
        return executionYears;
    }

}
