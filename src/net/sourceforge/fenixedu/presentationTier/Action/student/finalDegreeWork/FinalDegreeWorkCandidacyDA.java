/*
 * Created 2004/04/13
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz
 */
public class FinalDegreeWorkCandidacyDA extends FenixDispatchAction {

    public class NoDegreeStudentCurricularPlanFoundException extends Exception {
    }

    public ActionForward prepareCandidacy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        InfoGroup infoGroup = fillOutFinalDegreeWorkCandidacyForm(form, request);

        List infoExecutionDegrees = placeListOfExecutionDegreesInRequest(request);

        setDefaultExecutionDegree(form, request, infoExecutionDegrees);

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
        if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
        	// the student's curricular plan may not have an executionDegree
        	IUserView userView = SessionUtils.getUserView(request);
	        checkCandidacyConditions(userView, executionDegreeOID);
	
	        request.setAttribute("infoGroup", infoGroup);
	
	        String idInternal = (String) dynaActionForm.get("idInternal");
	        if ((idInternal == null || idInternal.equals("")) && request.getAttribute("CalledFromSelect") == null) {
	            selectExecutionDegree(mapping, form, request, response);
	        }
        }

        return mapping.findForward("showCandidacyForm");
    }

    public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");

        if (executionDegreeOID != null && !executionDegreeOID.equals("")
                && StringUtils.isNumeric(executionDegreeOID)) {
            IUserView userView = SessionUtils.getUserView(request);

            Object[] args = { userView.getPerson(), new Integer(executionDegreeOID) };
            try {
                ServiceUtils.executeService(userView, "EstablishFinalDegreeWorkStudentGroup", args);
            } catch (FenixServiceException ex) {
            	request.setAttribute("CalledFromSelect", Boolean.TRUE);
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        request.setAttribute("CalledFromSelect", Boolean.TRUE);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward addStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String idInternal = (String) dynaActionForm.get("idInternal");
        String studentUsernameToAdd = (String) dynaActionForm.get("studentUsernameToAdd");

        IUserView userView = SessionUtils.getUserView(request);
        if (studentUsernameToAdd != null && !studentUsernameToAdd.equals("")
                && !studentUsernameToAdd.equalsIgnoreCase(userView.getUtilizador())
                && idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
            Object[] args = { new Integer(idInternal), studentUsernameToAdd };
            try {
                ServiceUtils.executeService(userView, "AddStudentToFinalDegreeWorkStudentGroup", args);
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        dynaActionForm.set("studentUsernameToAdd", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward removeStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String idInternal = (String) dynaActionForm.get("idInternal");
        String studentToRemove = (String) dynaActionForm.get("studentToRemove");

        IUserView userView = SessionUtils.getUserView(request);
        if (studentToRemove != null && !studentToRemove.equals("")
                && StringUtils.isNumeric(studentToRemove) && idInternal != null
                && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
            Object[] args = { userView.getUtilizador(), new Integer(idInternal),
                    new Integer(studentToRemove) };
            try {
                ServiceUtils.executeService(userView, "RemoveStudentFromFinalDegreeWorkStudentGroup",
                        args);
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        dynaActionForm.set("studentToRemove", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward selectProposals(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String groupOID = (String) dynaActionForm.get("idInternal");

        if (groupOID != null && !groupOID.equals("") && StringUtils.isNumeric(groupOID)) {
            IUserView userView = SessionUtils.getUserView(request);

            Object[] args = { new Integer(groupOID) };
            List finalDegreeWorkProposalHeaders = (List) ServiceUtils.executeService(userView,
                    "ReadAvailableFinalDegreeWorkProposalHeadersForGroup", args);
            request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
        }

        return mapping.findForward("showSelectProposalsForm");
    }

    public ActionForward addProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String groupOID = (String) dynaActionForm.get("idInternal");
        String selectedProposal = (String) dynaActionForm.get("selectedProposal");

        if (groupOID != null && !groupOID.equals("") && StringUtils.isNumeric(groupOID)
                && selectedProposal != null && !selectedProposal.equals("")
                && StringUtils.isNumeric(selectedProposal)) {
            IUserView userView = SessionUtils.getUserView(request);

            Object[] args = { new Integer(groupOID), new Integer(selectedProposal) };
            try {
                ServiceUtils.executeService(userView, "AddFinalDegreeWorkProposalCandidacyForGroup",
                        args);
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward removeProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String idInternal = (String) dynaActionForm.get("idInternal");
        String selectedGroupProposal = (String) dynaActionForm.get("selectedGroupProposal");

        IUserView userView = SessionUtils.getUserView(request);
        if (selectedGroupProposal != null && !selectedGroupProposal.equals("")
                && StringUtils.isNumeric(selectedGroupProposal) && idInternal != null
                && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
        	try {
            	Object[] args = { new Integer(idInternal), new Integer(selectedGroupProposal) };
            	ServiceUtils.executeService(userView, "RemoveProposalFromFinalDegreeWorkStudentGroup", args);
        	} catch (FenixServiceException ex) {
            	prepareCandidacy(mapping, form, request, response);
            	throw ex;
        	}
        }

        dynaActionForm.set("selectedGroupProposal", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward changePreferenceOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String idInternal = (String) dynaActionForm.get("idInternal");
        String selectedGroupProposal = (String) dynaActionForm.get("selectedGroupProposal");
        String orderOfProposalPreference = request.getParameter("orderOfProposalPreference"
                + selectedGroupProposal);

        IUserView userView = SessionUtils.getUserView(request);
        if (selectedGroupProposal != null && !selectedGroupProposal.equals("")
                && StringUtils.isNumeric(selectedGroupProposal) && idInternal != null
                && !idInternal.equals("") && StringUtils.isNumeric(idInternal)
                && orderOfProposalPreference != null && !orderOfProposalPreference.equals("")
                && StringUtils.isNumeric(orderOfProposalPreference)) {
            Object[] args = { new Integer(idInternal), new Integer(selectedGroupProposal),
                    new Integer(orderOfProposalPreference) };
            ServiceUtils.executeService(userView,
                    "ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy", args);
        }

        dynaActionForm.set("selectedGroupProposal", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    private boolean checkCandidacyConditions(IUserView userView, String executionDegreeOID)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { userView, new Integer(executionDegreeOID) };
        ServiceUtils.executeService(userView, "CheckCandidacyConditionsForFinalDegreeWork", args);
        return true;
    }

    private InfoGroup fillOutFinalDegreeWorkCandidacyForm(ActionForm form, HttpServletRequest request)
            throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        Object[] args = { userView.getPerson() };
        InfoGroup infoGroup = (InfoGroup) ServiceUtils.executeService(userView,
                "ReadFinalDegreeWorkStudentGroupByUsername", args);

        if (infoGroup != null) {
            if (infoGroup.getExecutionDegree() != null
                    && infoGroup.getExecutionDegree().getIdInternal() != null) {
                String executionDegreeOID = infoGroup.getExecutionDegree().getIdInternal().toString();
                dynaActionForm.set("executionDegreeOID", executionDegreeOID);
            }
            if (infoGroup.getGroupStudents() != null && !infoGroup.getGroupStudents().isEmpty()) {
                String[] students = new String[infoGroup.getGroupStudents().size()];
                for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
                    InfoGroupStudent infoGroupStudent = (InfoGroupStudent) infoGroup.getGroupStudents()
                            .get(i);
                    students[i] = infoGroupStudent.getStudent().getIdInternal().toString();
                }
                dynaActionForm.set("students", students);
            }
            if (infoGroup.getIdInternal() != null) {
                dynaActionForm.set("idInternal", infoGroup.getIdInternal().toString());
            }
            Collections.sort(infoGroup.getGroupProposals(), new BeanComparator("orderOfPreference"));
            request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        }
        return infoGroup;
    }

    private void setDefaultExecutionDegree(ActionForm form, HttpServletRequest request,
            List infoExecutionDegrees) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");

        if ((executionDegreeOID == null || executionDegreeOID.length() == 0 || executionDegreeOID
                .equals(""))
                && infoExecutionDegrees != null && !infoExecutionDegrees.isEmpty()) {
            IUserView userView = SessionUtils.getUserView(request);

            Object[] args = { userView, DegreeType.DEGREE };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceUtils
                    .executeService(userView, "ReadActiveStudentCurricularPlanByDegreeType", args);
            if (infoStudentCurricularPlan == null) {
                throw new NoDegreeStudentCurricularPlanFoundException();
            }

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) CollectionUtils.find(
                    infoExecutionDegrees, new PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB(
                            infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getIdInternal()));

            if (infoExecutionDegree != null && infoExecutionDegree.getIdInternal() != null) {
                executionDegreeOID = infoExecutionDegree.getIdInternal().toString();
                dynaActionForm.set("executionDegreeOID", executionDegreeOID);
                request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
            }
        }
    }

    /**
     * @param request
     */
    private List placeListOfExecutionDegreesInRequest(HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        Object[] args = {};
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear();
        if (executionYear == null) {
            return new ArrayList(0);
        }
        final InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

//        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(null,
//                "ReadCurrentExecutionYear", args);

        args = new Object[] { infoExecutionYear.getIdInternal(), DegreeType.DEGREE };
        List infoExecutionDegrees = (List) ServiceUtils.executeService(null,
                "ReadExecutionDegreesByExecutionYearAndType", args);
        Collections.sort(infoExecutionDegrees, new BeanComparator(
                "infoDegreeCurricularPlan.infoDegree.nome"));
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);

        return infoExecutionDegrees;
    }

    private class PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB implements Predicate {

        Integer degreeCurricularPlanID = null;

        public boolean evaluate(Object arg0) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
            if (degreeCurricularPlanID != null
                    && infoExecutionDegree != null
                    && infoExecutionDegree.getInfoDegreeCurricularPlan() != null
                    && degreeCurricularPlanID.equals(infoExecutionDegree.getInfoDegreeCurricularPlan()
                            .getIdInternal())) {
                return true;
            }

            return false;

        }

        public PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB(Integer degreeCurricularPlanID) {
            super();
            this.degreeCurricularPlanID = degreeCurricularPlanID;
        }
    }

}