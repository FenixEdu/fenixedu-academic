/*
 * Created on 2005/02/18
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
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
 * @author Luis Cruz
 */
public class ManageStudentCurricularPlanDA extends FenixDispatchAction {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String studentNumberString = (String) dynaActionForm.get("number");
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");

        if (isPresent(studentNumberString) && isPresent(degreeTypeString)) {
            final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);
            putStudentCurricularInformationInRequest(request, Integer.valueOf(studentNumberString), degreeType);
        }

        return mapping.findForward("show");
    }

    public ActionForward deleteStudentCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");
        final Integer studentCurricularPlanId = new Integer(studentCurricularPlanIdString);

        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { studentCurricularPlanId };
        ServiceUtils.executeService(userView, "DeleteStudentCurricularPlan", args);

        return show(mapping, form, request, response);
    }

    public ActionForward changeStudentCurricularPlanState(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String selectedStudentCurricularPlanIdString = (String) dynaActionForm
                .get("selectedStudentCurricularPlanId");
        final String studentCurricularPlanStateString = (String) dynaActionForm
                .get("studentCurricularPlanState");

        final Integer selectedStudentCurricularPlanId = new Integer(
                selectedStudentCurricularPlanIdString);
        final StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.valueOf(studentCurricularPlanStateString);

        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { selectedStudentCurricularPlanId, studentCurricularPlanState };
        try {
	    ServiceUtils.executeService(userView, "ChangeStudentCurricularPlanState", args);
	} catch (FenixFilterException e) {
	    e.printStackTrace();
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}

        return show(mapping, form, request, response);
    }

    public ActionForward deleteEnrollment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String enrollmentIdString = request.getParameter("enrollmentId");
        final String studentNumberString = request.getParameter("studentNumber");
        final String degreeTypeString = request.getParameter("degreeType");
        final Integer enrollmentId = Integer.valueOf(enrollmentIdString);
        final Integer studentNumber = Integer.valueOf(studentNumberString);
        final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);

        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { studentNumber, degreeType, enrollmentId };
        ServiceUtils.executeService(userView, "DeleteEnrollment", args);

        return show(mapping, form, request, response);
    }

    public ActionForward prepareCreateStudentCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");

        if (isPresent(degreeTypeString)) {
            //putStudentCurricularPlanStateLabelListInRequest(request);

            final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);

            final IUserView userView = SessionUtils.getUserView(request);

            final Object[] args = new Object[] { degreeType };
            final List infoDegreeCurricularPlans = (List) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlansByDegreeType", args);

            putDegreeCurricularPlansInRequest(request, infoDegreeCurricularPlans);
        }

        return mapping.findForward("createStudentCurricularPlan");
    }

    public ActionForward createStudentCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String studentNumberString = (String) dynaActionForm.get("number");
        final String degreeTypeString = (String) dynaActionForm.get("degreeType");
        final String studentCurricularPlanStateString = (String) dynaActionForm
                .get("studentCurricularPlanState");
        final String degreeCurricularPlanIdString = (String) dynaActionForm
                .get("degreeCurricularPlanId");
        final String startDateString = (String) dynaActionForm.get("startDate");

        if (isPresent(studentNumberString) && isPresent(degreeTypeString)
                && isPresent(studentCurricularPlanStateString)
                && isPresent(degreeCurricularPlanIdString) && isPresent(startDateString)) {

            final Integer studentNumber = new Integer(studentNumberString);
            final DegreeType degreeType = DegreeType.valueOf(degreeTypeString);
            final StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.valueOf(studentCurricularPlanStateString);
            final Integer degreeCurricularPlanId = new Integer(degreeCurricularPlanIdString);
            final Date startDate = simpleDateFormat.parse(startDateString);

            final IUserView userView = SessionUtils.getUserView(request);

            final Object[] args = new Object[] { studentNumber, degreeType, studentCurricularPlanState,
                    degreeCurricularPlanId, startDate };
            ServiceUtils.executeService(userView, "CreateStudentCurricularPlan", args);
        }

        return show(mapping, form, request, response);
    }

    public ActionForward prepareTransferEnrollments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        show(mapping, form, request, response);
        return mapping.findForward("transferEnrollments");
    }

    public ActionForward transferEnrollments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String selectedStudentCurricularPlanIdString = (String) dynaActionForm
                .get("selectedStudentCurricularPlanId");
        final String[] enrollmentStringIDsToTransfer = (String[]) dynaActionForm
                .get("enrollmentIDsToTransfer");

        if (isPresent(selectedStudentCurricularPlanIdString) && enrollmentStringIDsToTransfer != null
				&& enrollmentStringIDsToTransfer.length > 0) {

            final Integer selectedStudentCurricularPlanId = new Integer(selectedStudentCurricularPlanIdString);
			final Integer[] enrollmentIDsToTransfer = new Integer[enrollmentStringIDsToTransfer.length];
			for (int i = 0; i < enrollmentStringIDsToTransfer.length; i++) {
				final String enrollmentStringIDToTransfer = enrollmentStringIDsToTransfer[i];
				enrollmentIDsToTransfer[i] = new Integer(enrollmentStringIDToTransfer);
			}

            final IUserView userView = SessionUtils.getUserView(request);

            final Object[] args = new Object[] { selectedStudentCurricularPlanId, enrollmentIDsToTransfer };
            ServiceUtils.executeService(userView, "TransferEnrollments", args);
        }
		
        return show(mapping, form, request, response);
    }

    protected void putStudentCurricularInformationInRequest(final HttpServletRequest request,
            final Integer studentNumber, final DegreeType degreeType) throws FenixFilterException,
            FenixServiceException {
        final IUserView userView = SessionUtils.getUserView(request);
        final Object[] args = new Object[] { studentNumber, degreeType };
        final List infoStudentCurricularPlans = (List) ServiceUtils.executeService(userView,
                "ReadStudentCurricularInformation", args);
        request.setAttribute("infoStudentCurricularPlans", infoStudentCurricularPlans);
    }

    protected boolean isPresent(final String string) {
        return string != null && string.length() > 0;
    }

    protected void putDegreeCurricularPlansInRequest(final HttpServletRequest request,
            final List infoDegreeCurricularPlans) {
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
        comparatorChain.addComparator(new BeanComparator("initialDate"));
        Collections.sort(infoDegreeCurricularPlans, comparatorChain);
        request.setAttribute("degreeCurricularPlans", infoDegreeCurricularPlans);
    }

}