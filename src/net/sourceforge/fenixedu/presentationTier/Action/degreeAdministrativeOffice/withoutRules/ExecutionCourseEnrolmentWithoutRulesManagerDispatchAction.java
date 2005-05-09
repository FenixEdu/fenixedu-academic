/*
 * Created on 17/Fev/2004
 *    
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.withoutRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

/**
 * @author Tânia Pousão
 *  
 */
public class ExecutionCourseEnrolmentWithoutRulesManagerDispatchAction extends DispatchAction {
    private static final int MAX_CURRICULAR_YEARS = 5;

    private static final int MAX_CURRICULAR_SEMESTERS = 2;

    public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("exit");
    }

    public ActionForward prepareEnrollmentChooseStudentAndExecutionYear(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        //degree type's code
        String degreeType = request.getParameter("degreeType");
        if (degreeType == null) {
            degreeType = (String) request.getAttribute("degreeType");
            if (degreeType == null) {
                DynaActionForm actionForm = (DynaActionForm) form;
                degreeType = (String) actionForm.get("degreeType");
            }
        }
              
        request.setAttribute("degreeType", degreeType);

        //execution years
        List executionPeriods = null;
        Object[] args = {DegreeType.valueOf(degreeType)};
        try {
            executionPeriods = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadExecutionPeriodsEnrollmentFenix", args);
        } catch (FenixServiceException e) {
            errors.add("noExecutionYears", new ActionError("error.impossible.operations"));
            saveErrors(request, errors);
            return mapping.findForward("globalEnrolment");
        }
        if (executionPeriods == null || executionPeriods.size() <= 0) {
            errors.add("noExecutionYears", new ActionError("error.impossible.operations"));
            saveErrors(request, errors);
            return mapping.findForward("globalEnrolment");
        }

        sortExecutionPeriods(executionPeriods, (DynaActionForm) form);

        List executionYearLabels = buildLabelValueBeanForJsp(executionPeriods);
        request.setAttribute("executionPeriods", executionYearLabels);

        return mapping.findForward("prepareEnrollmentChooseStudentWithoutRules");
    }

    private void sortExecutionPeriods(List executionPeriods, DynaActionForm form) {
        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
        comparator.addComparator(new BeanComparator("semester"), true);
        Collections.sort(executionPeriods, comparator);

        int size = executionPeriods.size();
        for (int i = (size - 1); i >= 0; i--) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            if (infoExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
                form.set("executionPeriod", infoExecutionPeriod.getIdInternal().toString());
                break;
            }
        }
    }

    private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {
        List executionPeriodsLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
            public Object transform(Object arg0) {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionPeriod.getName() + " - " + infoExecutionPeriod.getInfoExecutionYear().getYear(),
                        infoExecutionPeriod.getIdInternal().toString());
                return executionYear;
            }
        }, executionPeriodsLabels);
        return executionPeriodsLabels;
    }

    public ActionForward readEnrollments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm prepareEnrolmentForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf((String) prepareEnrolmentForm.get("studentNumber"));
        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        String executionPeriodID = (String) prepareEnrolmentForm.get("executionPeriod");

        String degreeTypeCode = (String) prepareEnrolmentForm.get("degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);

        Object[] args = { infoStudent, degreeType, new Integer(executionPeriodID) };
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        try {
            infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                    .executeService(userView, "ReadEnrollmentsWithStateEnrolledByStudent", args);

        } catch (NotAuthorizedFilterException e) {
            e.printStackTrace();

            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                errors.add("noStudentCurricularPlan", new ActionError(e.getMessage(), studentNumber));
            } else if (e.getMessage() != null
                    && e.getMessage().endsWith("not.from.chosen.execution.year")) {
                errors.add("notFromChosenExecutionYear", new ActionError(e.getMessage(), studentNumber));
            } else {
                errors.add("noResult", new ActionError("error.impossible.operations"));
            }

            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

        return mapping.findForward("curricularCourseEnrollmentList");
    }

    public ActionForward unEnrollCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm unEnrollForm = (DynaActionForm) form;
        Integer studentNumber = Integer.valueOf((String) unEnrollForm.get("studentNumber"));
        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        /*String executionYear = (String) unEnrollForm.get("executionYear");
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setYear(executionYear);*/

        String degreeTypeCode = (String) unEnrollForm.get("degreeType");
        DegreeType degreeType = null;
        if (degreeTypeCode != null && degreeTypeCode.length() > 0) {
            degreeType = DegreeType.valueOf(degreeTypeCode);
        }

        Integer[] unenrollments = (Integer[]) unEnrollForm.get("unenrollments");
        List unenrollmentsList = Arrays.asList(unenrollments);

        Object[] args = { infoStudent, degreeType, unenrollmentsList };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteEnrollmentsList", args);
        } catch (NotAuthorizedFilterException e) {

            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            e.printStackTrace();

            errors.add("unenroll",
                    new ActionError("error.impossible.operations.unenroll", studentNumber));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        return mapping.findForward("readCurricularCourseEnrollmentList");
    }

    public ActionForward prepareEnrollmentCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm prepareEnrolmentForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf((String) prepareEnrolmentForm.get("studentNumber"));
        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        String executionPeriodID = (String) prepareEnrolmentForm.get("executionPeriod");
        String studentCurricularPlan = (String) prepareEnrolmentForm.get("studentCurricularPlan");
        /*InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setYear(executionYear);*/

        String degreeTypeCode = (String) prepareEnrolmentForm.get("degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);

        String executionDegreeString = (String) prepareEnrolmentForm.get("executionDegree");
        Integer executionDegreeId = null;
        if (executionDegreeString != null && executionDegreeString.length() > 0) {
            executionDegreeId = Integer.valueOf((String) prepareEnrolmentForm.get("executionDegree"));
        }

        //read execution degrees
        Object args[] = { infoStudent, degreeType, executionDegreeId, new Integer(executionPeriodID) };
        List executionDegreeList = null;
        InfoExecutionDegree infoExecutionDegreeSelected = null;
        try {
            
            //get InfoStudent with InfoPerson to mantain context
            Object argsStudent[] = {studentNumber, degreeType};
            infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView, "ReadStudentByNumberAndDegreeType", argsStudent);
            if (infoStudent == null) {
                throw new FenixServiceException();
            }
            
            // it is return a list where the first element is the degree
            // pre-select and the tail is all
            // degrees
            executionDegreeList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareDegreesListByStudentNumber", args);
            if (executionDegreeList == null || executionDegreeList.size() < 0) {
                throw new FenixServiceException();
            }
        } catch (FenixServiceException e) {
            errors.add("impossibleOperation", new ActionError("error.impossible.operations"));
            saveErrors(request, errors);
            return mapping.findForward("readCurricularCourseEnrollmentList");
        }
        infoExecutionDegreeSelected = (InfoExecutionDegree) executionDegreeList.get(0);
        executionDegreeList.remove(0);

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        
        MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
        
        List executionDegreeLabels = ExecutionDegreesFormat
                .buildExecutionDegreeLabelValueBean(executionDegreeList, messageResources);
        request.setAttribute(SessionConstants.DEGREE_LIST, executionDegreeLabels);

        //read all curricular years and semester
        List listOfChosenCurricularYears = getListOfChosenCurricularYears();
        List listOfChosenCurricularSemesters = getListOfChosenCurricularSemesters();
        request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, listOfChosenCurricularYears);
        request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY,
                listOfChosenCurricularSemesters);

        //maintenance of the form
        DynaActionForm enrollForm = (DynaActionForm) form;
        enrollForm.set("studentNumber", studentNumber.toString());
        enrollForm.set("executionPeriod", executionPeriodID);
        enrollForm.set("degreeType", degreeTypeCode);
        enrollForm.set("executionDegree", infoExecutionDegreeSelected.getIdInternal().toString());

        //maintenance of the Context with the student's number and name and
        // execution year
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = maintenanceContext(infoStudent,
                infoExecutionDegreeSelected.getInfoExecutionYear(), studentCurricularPlan);
        request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

        return mapping.findForward("choosesForEnrollment");
    }

    private List getListOfChosenCurricularYears() {
        List result = new ArrayList();

        for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++) {
            result.add(new Integer(i));
        }
        return result;
    }

    private List getListOfChosenCurricularSemesters() {
        List result = new ArrayList();

        for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++) {
            result.add(new Integer(i));
        }
        return result;
    }

    private InfoStudentEnrollmentContext maintenanceContext(InfoStudent infoStudent,
            InfoExecutionYear infoExecutionYear, String studentCurricularPlan) {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();

        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
        infoStudentCurricularPlan.setInfoStudent(infoStudent);
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setName(studentCurricularPlan);
        infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
        infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
        return infoStudentEnrolmentContext;
    }

    public ActionForward readCoursesToEnroll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm enrollForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf((String) enrollForm.get("studentNumber"));
        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        String executionPeriodID = (String) enrollForm.get("executionPeriod");
        /*InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setYear(executionYear);*/

        String degreeTypeCode = (String) enrollForm.get("degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);

        Integer executionDegreeID = Integer.valueOf((String) enrollForm.get("executionDegree"));

        Integer[] curricularYears = (Integer[]) enrollForm.get("curricularYears");
        List curricularYearsList = Arrays.asList(curricularYears);

        Integer[] curricularSemesters = (Integer[]) enrollForm.get("curricularSemesters");
        List curricularSemestersList = Arrays.asList(curricularSemesters);

        Object args[] = { infoStudent, degreeType, new Integer(executionPeriodID), executionDegreeID,
                curricularYearsList, curricularSemestersList };
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        try {
            Integer userType = (Integer) enrollForm.get("userType");
  
            if(userType.equals(0)) {
                infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                .executeService(userView, "ReadCurricularCoursesToEnroll", args);
            }
            else {
                infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                .executeService(userView, "ReadCurricularCoursesToEnrollSuperUser", args);
            }

        } catch (NotAuthorizedFilterException e) {
            e.printStackTrace();

            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                errors.add("noStudentCurricularPlan", new ActionError(e.getMessage(), studentNumber));
            } else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
                errors.add("noResult", new ActionError(e.getMessage()));
            } else {
                errors.add("impossibleOperation", new ActionError("error.impossible.operations"));
            }

            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);
        enrollForm.set("enrollmentTypes", getInitializedMap(infoStudentEnrolmentContext
                .getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled()));
        return mapping.findForward("showCurricularCourseToEnroll");
    }

    /**
     * @param curricularCourses2Enroll
     * @return
     */
    private Object getInitializedMap(List curricularCourses2Enroll) {
        Map map = new HashMap();
        Iterator iter = curricularCourses2Enroll.iterator();
        while (iter.hasNext()) {
            InfoCurricularCourse2Enroll infoCurricularCourse2Enroll = (InfoCurricularCourse2Enroll) iter
                    .next();
            map.put(infoCurricularCourse2Enroll.getInfoCurricularCourse().getIdInternal(),
                    new Integer(0));
        }
        return map;
    }

    public ActionForward enrollCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm enrollForm = (DynaActionForm) form;
        Integer studentNumber = Integer.valueOf((String) enrollForm.get("studentNumber"));
        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        String executionPeriodID = (String) enrollForm.get("executionPeriod");
        /*InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setYear(executionYear);*/

        String degreeTypeCode = (String) enrollForm.get("degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeCode);

        String[] curricularCourses = (String[]) enrollForm.get("curricularCourses");
        List curricularCoursesList = Arrays.asList(curricularCourses);
        Map optionalEnrollments = (HashMap) enrollForm.get("enrollmentTypes");
        Object[] args = { infoStudent, degreeType, new Integer(executionPeriodID), curricularCoursesList,
                optionalEnrollments, userView };
        try {
            ServiceManagerServiceFactory.executeService(userView, "WriteEnrollmentsList", args);
        } catch (NotAuthorizedFilterException e) {
            e.printStackTrace();

            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                errors.add("noStudentCurricularPlan", new ActionError(e.getMessage(), studentNumber));
            } else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
                errors.add("noResult", new ActionError(e.getMessage()));
            } else {
                errors.add("impossibleOperation", new ActionError("error.impossible.operations"));
            }

            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        return mapping.findForward("readCurricularCourseEnrollmentList");
    }
}