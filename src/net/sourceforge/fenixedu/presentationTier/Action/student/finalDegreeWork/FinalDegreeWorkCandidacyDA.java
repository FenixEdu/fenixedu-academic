/*
 * Created 2004/04/13
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz
 */
public class FinalDegreeWorkCandidacyDA extends FenixDispatchAction {

    public class NoDegreeStudentCurricularPlanFoundException extends Exception {
    }

    public ActionForward prepareCandidacy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        final ExecutionYear executionYear;
        final String executionYearOID = (String) dynaActionForm.get("executionYearOID");
        if (executionYearOID == null || executionYearOID.equals("")) {
        	executionYear = ExecutionYear.readCurrentExecutionYear();
        	dynaActionForm.set("executionYearOID", executionYear.getIdInternal().toString());
        } else {
        	executionYear = rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearOID));
        }

        final Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
        executionYears.addAll(rootDomainObject.getExecutionYearsSet());
    	request.setAttribute("executionYears", executionYears);

        InfoGroup infoGroup =  fillOutFinalDegreeWorkCandidacyForm(form, request, executionYear);

        List infoExecutionDegrees = placeListOfExecutionDegreesInRequest(request, executionYear);

        setDefaultExecutionDegree(form, request, infoExecutionDegrees);

        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
        if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
        	// the student's curricular plan may not have an executionDegree
        	IUserView userView = UserView.getUser();
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
            IUserView userView = UserView.getUser();

            Object[] args = { userView.getPerson(), new Integer(executionDegreeOID) };
            try {
                ServiceUtils.executeService("EstablishFinalDegreeWorkStudentGroup", args);
            } catch (FenixServiceException ex) {
            	request.setAttribute("CalledFromSelect", Boolean.TRUE);
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        request.setAttribute("CalledFromSelect", Boolean.TRUE);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward selectExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        dynaActionForm.set("executionDegreeOID", null);
        request.setAttribute("CalledFromSelect", Boolean.TRUE);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward addStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String idInternal = (String) dynaActionForm.get("idInternal");
        String studentUsernameToAdd = (String) dynaActionForm.get("studentUsernameToAdd");

        IUserView userView = UserView.getUser();
        if (studentUsernameToAdd != null && !studentUsernameToAdd.equals("")
                && !studentUsernameToAdd.equalsIgnoreCase(userView.getUtilizador())
                && idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
            Object[] args = { new Integer(idInternal), studentUsernameToAdd };
            try {
                ServiceUtils.executeService("AddStudentToFinalDegreeWorkStudentGroup", args);
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

        IUserView userView = UserView.getUser();
        if (studentToRemove != null && !studentToRemove.equals("")
                && StringUtils.isNumeric(studentToRemove) && idInternal != null
                && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
            Object[] args = { userView.getUtilizador(), new Integer(idInternal),
                    new Integer(studentToRemove) };
            try {
                ServiceUtils.executeService("RemoveStudentFromFinalDegreeWorkStudentGroup",
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
            IUserView userView = UserView.getUser();

            Object[] args = { new Integer(groupOID) };
            List finalDegreeWorkProposalHeaders = (List) ServiceUtils.executeService(
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
            IUserView userView = UserView.getUser();

            Object[] args = { new Integer(groupOID), new Integer(selectedProposal) };
            try {
                ServiceUtils.executeService("AddFinalDegreeWorkProposalCandidacyForGroup",
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

        IUserView userView = UserView.getUser();
        if (selectedGroupProposal != null && !selectedGroupProposal.equals("")
                && StringUtils.isNumeric(selectedGroupProposal) && idInternal != null
                && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
        	try {
            	Object[] args = { new Integer(idInternal), new Integer(selectedGroupProposal) };
            	ServiceUtils.executeService("RemoveProposalFromFinalDegreeWorkStudentGroup", args);
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

        IUserView userView = UserView.getUser();
        if (selectedGroupProposal != null && !selectedGroupProposal.equals("")
                && StringUtils.isNumeric(selectedGroupProposal) && idInternal != null
                && !idInternal.equals("") && StringUtils.isNumeric(idInternal)
                && orderOfProposalPreference != null && !orderOfProposalPreference.equals("")
                && StringUtils.isNumeric(orderOfProposalPreference)) {
            Object[] args = { new Integer(idInternal), new Integer(selectedGroupProposal),
                    new Integer(orderOfProposalPreference) };
            ServiceUtils.executeService(
                    "ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy", args);
        }

        dynaActionForm.set("selectedGroupProposal", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    private boolean checkCandidacyConditions(IUserView userView, String executionDegreeOID)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { userView, new Integer(executionDegreeOID) };
        ServiceUtils.executeService("CheckCandidacyConditionsForFinalDegreeWork", args);
        return true;
    }

    private InfoGroup fillOutFinalDegreeWorkCandidacyForm(ActionForm form, HttpServletRequest request, ExecutionYear executionYear)
            throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        IUserView userView = UserView.getUser();

        Object[] args = { userView.getPerson(), executionYear };
        InfoGroup infoGroup = (InfoGroup) ServiceUtils.executeService(
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
            IUserView userView = UserView.getUser();

            InfoStudentCurricularPlan infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView);
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

    private InfoStudentCurricularPlan getDefaultStudentCurricularPlan(IUserView userView) throws FenixServiceException,
    	FenixFilterException {
	InfoStudentCurricularPlan infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.BOLONHA_MASTER_DEGREE);
	if (infoStudentCurricularPlan == null) {
	    infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	}
	if (infoStudentCurricularPlan == null) {
	    infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.BOLONHA_DEGREE);
	}
	if (infoStudentCurricularPlan == null) {
	    infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.DEGREE);
	}
	return infoStudentCurricularPlan;
    }

    private InfoStudentCurricularPlan getDefaultStudentCurricularPlan(IUserView userView, final DegreeType degreeType) throws FenixServiceException,
	    FenixFilterException {
	Object[] args = { userView, degreeType };
	return (InfoStudentCurricularPlan) ServiceUtils.executeService("ReadActiveStudentCurricularPlanByDegreeType", args);
    }

    /**
     * @param request
     * @param executionYear 
     */
    private List placeListOfExecutionDegreesInRequest(HttpServletRequest request, ExecutionYear executionYear)
            throws FenixServiceException, FenixFilterException {
        if (executionYear == null) {
            return new ArrayList(0);
        }
        final InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

        final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
        degreeTypes.add(DegreeType.DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
        Object[] args = new Object[] { infoExecutionYear.getIdInternal(), degreeTypes };
        List infoExecutionDegrees = (List) ServiceUtils.executeService("ReadExecutionDegreesByExecutionYearAndType", args);
        Collections.sort(infoExecutionDegrees, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
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