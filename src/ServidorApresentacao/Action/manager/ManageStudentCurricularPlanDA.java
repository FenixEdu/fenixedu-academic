/*
 * Created on 2005/02/18
 * 
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author Luis Crus
 */
public class ManageStudentCurricularPlanDA extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        putDegreeTypeLabelListInRequest(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String studentNumberString = (String) dynaActionForm.get("number");
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");

        if (isPresent(studentNumberString) && isPresent(degreeTypeString)) {
            putStudentCurricularPlanStateLabelListInRequest(request);

            final Integer studentNumber = new Integer(studentNumberString);
            final TipoCurso degreeType = new TipoCurso(new Integer(degreeTypeString));
            putStudentCurricularInformationInRequest(request, studentNumber, degreeType);
        }

        return mapping.findForward("show");
    }

    public ActionForward deleteStudentCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");
        final Integer studentCurricularPlanId = new Integer(studentCurricularPlanIdString);

        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { studentCurricularPlanId };
        ServiceUtils.executeService(userView, "DeleteStudentCurricularPlan", args);

        return show(mapping, form, request, response);
    }

    public ActionForward changeStudentCurricularPlanState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String selectedStudentCurricularPlanIdString = (String) dynaActionForm.get("selectedStudentCurricularPlanId");
        final String studentCurricularPlanStateString = (String) dynaActionForm.get("studentCurricularPlanState");

        final Integer selectedStudentCurricularPlanId = new Integer(selectedStudentCurricularPlanIdString);
        final StudentCurricularPlanState studentCurricularPlanState = new StudentCurricularPlanState(studentCurricularPlanStateString);

        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { selectedStudentCurricularPlanId, studentCurricularPlanState };
        ServiceUtils.executeService(userView, "ChangeStudentCurricularPlanState", args);

        return show(mapping, form, request, response);
    }

    public ActionForward deleteEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String enrollmentIdString = request.getParameter("enrollmentId");
        final Integer enrollmentId = new Integer(enrollmentIdString);

        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { enrollmentId };
        ServiceUtils.executeService(userView, "DeleteEnrollment", args);

        return show(mapping, form, request, response);
    }

    protected void putStudentCurricularInformationInRequest(final HttpServletRequest request,
            final Integer studentNumber, final TipoCurso degreeType) throws FenixFilterException,
            FenixServiceException {
        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { studentNumber, degreeType };
        final List infoStudentCurricularPlans = (List) ServiceUtils.executeService(userView,
                "ReadStudentCurricularInformation", args);
        sortInformation(infoStudentCurricularPlans);
        request.setAttribute("infoStudentCurricularPlans", infoStudentCurricularPlans);
    }

    protected void sortInformation(final List infoStudentCurricularPlans) {
        for (final Iterator iterator = infoStudentCurricularPlans.iterator(); iterator.hasNext(); ) {
            final InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) iterator.next();
            final List infoEnrollmentGrades = infoStudentCurricularPlan.getInfoEnrolments();
            Collections.sort(infoEnrollmentGrades, new BeanComparator("infoEnrollment.infoCurricularCourse.name"));
        }
    }

    protected void putDegreeTypeLabelListInRequest(final HttpServletRequest request) {
        request.setAttribute("degreeTypes", TipoCurso.toLabelValueBeanList());
    }

    protected void putStudentCurricularPlanStateLabelListInRequest(final HttpServletRequest request) {
        request.setAttribute("studentCurricularPlanStates", StudentCurricularPlanState.toArrayList());
    }

    protected boolean isPresent(final String string) {
        return string != null && string.length() > 0;
    }

}