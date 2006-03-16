/**
 * Jan 30, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class MasterDegreeCreditsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { ExecutionYear.class };
        List<ExecutionYear> executionYears = (List<ExecutionYear>) ServiceUtils.executeService(userView,
                "ReadAllDomainObjects", args);

        List<ExecutionYear> notClosedExecutionYears = (List<ExecutionYear>) CollectionUtils.select(
                executionYears, new Predicate() {
                    public boolean evaluate(Object arg0) {
                        ExecutionYear executionYear = (ExecutionYear) arg0;
                        return !executionYear.getState().equals(PeriodState.CLOSED);
                    }
                });
        Iterator orderedExecutionYearsIter = new OrderedIterator(notClosedExecutionYears.iterator(),
                new BeanComparator("beginDate"));
        request.setAttribute("executionYears", orderedExecutionYearsIter);
        DynaActionForm dynaForm = (DynaActionForm) form;
        final Integer executionYearID = (Integer) dynaForm.get("executionYearID");
        ExecutionYear executionYear = null;
        if (executionYearID == null || executionYearID == 0) {
            for (ExecutionYear tempExecutionYear : notClosedExecutionYears) {
                if (tempExecutionYear.getState().equals(PeriodState.CURRENT)) {
                    executionYear = tempExecutionYear;
                    break;
                }
            }
            dynaForm.set("executionYearID", executionYear.getIdInternal());
        } else {
            for (ExecutionYear tempExecutionYear : notClosedExecutionYears) {
                if (tempExecutionYear.getIdInternal().equals(executionYearID)) {
                    executionYear = tempExecutionYear;
                    break;
                }
            }
        }
        List<ExecutionDegree> masterDegreeExecutions = (List<ExecutionDegree>) executionYear
                .getExecutionDegreesByType(DegreeType.MASTER_DEGREE);
        Iterator orderedExecutionDegreesIter = new OrderedIterator(masterDegreeExecutions.iterator(),
                new BeanComparator("degreeCurricularPlan.name"));
        request.setAttribute("masterDegreeExecutions", orderedExecutionDegreesIter);
        return mapping.findForward("chooseMasterDegreeExecution");
    }

    public ActionForward viewMasterDegreeCredits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer executionDegreeID = (Integer) dynaForm.get("executionDegreeID");

        if (executionDegreeID != null) {
            Object[] args = { ExecutionDegree.class, executionDegreeID };
            ExecutionDegree executionDegree = (ExecutionDegree) ServiceUtils.executeService(userView,
                    "ReadDomainObject", args);
            request.setAttribute("executionDegree", executionDegree);

            List<CurricularCourse> curricularCourses = executionDegree.getDegreeCurricularPlan()
                    .getCurricularCoursesWithExecutionIn(executionDegree.getExecutionYear());

            List<MasterDegreeCreditsDTO> masterDegreeCoursesDTOs = new ArrayList<MasterDegreeCreditsDTO>();
            for (CurricularCourse curricularCourse : curricularCourses) {
                MasterDegreeCreditsDTO masterDegreeCreditsDTO = new MasterDegreeCreditsDTO(
                        curricularCourse, executionDegree.getExecutionYear());
                masterDegreeCoursesDTOs.add(masterDegreeCreditsDTO);
            }
            if (!masterDegreeCoursesDTOs.isEmpty()) {
                Iterator orderedCoursesIter = new OrderedIterator(masterDegreeCoursesDTOs.iterator(),
                        new BeanComparator("curricularCourse.name"));
                request.setAttribute("masterDegreeCoursesDTOs", orderedCoursesIter);
            }

            return mapping.findForward("showCreditsReport");
        } else {
            return prepare(mapping,form,request,response);
        }
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer curricularCourseID = (Integer) dynaForm.get("curricularCourseID");
        Object args1[] = { CurricularCourse.class, curricularCourseID };
        CurricularCourse curricularCourse = (CurricularCourse) ServiceUtils.executeService(userView,
                "ReadDomainObject", args1);

        Integer executionDegreeID = (Integer) dynaForm.get("executionDegreeID");
        ExecutionDegree executionDegree = null;
        if (executionDegreeID != null) {
            Object args2[] = { ExecutionDegree.class, executionDegreeID };
            executionDegree = (ExecutionDegree) ServiceUtils.executeService(userView,
                    "ReadDomainObject", args2);
        } else {
            Integer executionCourseID = Integer.parseInt(request.getParameter("executionCourseId"));
            Object args3[] = { ExecutionCourse.class, executionCourseID };
            ExecutionCourse executionCourse = (ExecutionCourse) ServiceUtils.executeService(userView,
                    "ReadDomainObject", args3);
            executionDegree = curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(
                    executionCourse.getExecutionPeriod().getExecutionYear());

        }
        request.setAttribute("executionDegree", executionDegree);

        MasterDegreeCreditsDTO masterDegreeCreditsDTO = new MasterDegreeCreditsDTO(curricularCourse,
                executionDegree.getExecutionYear());
        request.setAttribute("masterDegreeCreditsDTO", masterDegreeCreditsDTO);
        dynaForm.set("executionDegreeID", executionDegree.getIdInternal());
        dynaForm.set("curricularCourseID", curricularCourseID);
        return mapping.findForward("editMasterDegreeCredits");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;
        Map creditsMap = (Map) dynaForm.get("creditsMap");
        Map hoursMap = (Map) dynaForm.get("hoursMap");

        Object args[] = { hoursMap, creditsMap };
        try {
            ServiceUtils.executeService(userView, "EditTeacherMasterDegreeCredits", args);
        } catch (FenixServiceException fse) {
            Throwable throwable = fse.getCause();
            if (throwable.getCause() instanceof NumberFormatException) {
                throw new NumberFormatException();
            }
        }
        return mapping.findForward("successfulEdit");
    }

    public class MasterDegreeCreditsDTO {

        CurricularCourse curricularCourse;

        StringBuilder semesters = new StringBuilder();

        Set<String> dcpNames = new HashSet<String>();

        int totalRowSpan = 0;

        int numberEnrolments = 0;

        boolean allowToChange = true;

        Map<String, Integer> professorshipsRowSpanMap = new HashMap<String, Integer>();

        Map<String, ExecutionCourse> executionCoursesMap = new TreeMap<String, ExecutionCourse>();

        public MasterDegreeCreditsDTO(CurricularCourse curricularCourse, ExecutionYear executionYear) {
            setCurricularCourse(curricularCourse);
            if (curricularCourse.getType().equals(CurricularCourseType.ML_TYPE_COURSE)) {
                setAllowToChange(false);
            }
            List<CurricularCourseScope> ccsList = curricularCourse.getScopes();

            Iterator cssListIter = ccsList.iterator();
            List<Integer> semesters = new ArrayList();
            if (!ccsList.isEmpty()) {
                while (cssListIter.hasNext()) {
                    CurricularCourseScope ccs = (CurricularCourseScope) cssListIter.next();
                    if (!semesters.contains(ccs.getCurricularSemester().getSemester())) {
                        semesters.add(ccs.getCurricularSemester().getSemester());
                    }
                }
                int semestersSize = semesters.size();
                Collections.sort(semesters);
                for (Integer semesterNumber : semesters) {
                    this.semesters.append(semesterNumber);
                    if (semestersSize == 2) {
                        this.semesters.append(" e ");
                        semestersSize = 1;
                    }
                }
            } else {
                this.semesters.append("0");
            }
            
            numberEnrolments = curricularCourse.getCurriculumModules().size();
            for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                List<ExecutionCourse> executionCourses = curricularCourse
                        .getExecutionCoursesByExecutionPeriod(executionPeriod);
                if (!executionCourses.isEmpty()) {
                    ExecutionCourse executionCourse = executionCourses.iterator().next();
                    if (!executionCourse.isMasterDegreeOnly()) {
                        setAllowToChange(false);
                    }
                    if (!isAllowToChange()) {
                        for (CurricularCourse tempCurricularCourse : executionCourse
                                .getAssociatedCurricularCourses()) {
                            dcpNames.add(tempCurricularCourse.getDegreeCurricularPlan().getName());
                        }
                    }
                    executionCoursesMap.put(executionPeriod.getName(), executionCourse);
                    int profCounter = executionCourse.getProfessorshipsCount();
                    if (profCounter == 0) {
                        profCounter = 1;
                    }
                    professorshipsRowSpanMap.put(executionPeriod.getName(), new Integer(profCounter));
                    totalRowSpan += profCounter;
                }
            }
            if (totalRowSpan == 0) {
                totalRowSpan = 1;
            }
        }

        public String getDegreeNames() {

            return null;
        }

        public CurricularCourse getCurricularCourse() {
            return curricularCourse;
        }

        public void setCurricularCourse(CurricularCourse curricularCourse) {
            this.curricularCourse = curricularCourse;
        }

        public int getTotalRowSpan() {
            return totalRowSpan;
        }

        public void setTotalRowSpan(int totalRowSpan) {
            this.totalRowSpan = totalRowSpan;
        }

        public Map<String, Integer> getProfessorshipsRowSpanMap() {
            return professorshipsRowSpanMap;
        }

        public int getNumberEnrolments() {
            return numberEnrolments;
        }

        public Map<String, ExecutionCourse> getExecutionCoursesMap() {
            return executionCoursesMap;
        }

        public StringBuilder getSemesters() {
            return semesters;
        }

        public void setSemesters(StringBuilder semesters) {
            this.semesters = semesters;
        }

        public boolean isAllowToChange() {
            return allowToChange;
        }

        public void setAllowToChange(boolean allowToChange) {
            this.allowToChange = allowToChange;
        }

        public List<String> getDcpNames() {
            List<String> orderedDCPNames = new ArrayList(dcpNames);
            Collections.sort(orderedDCPNames);
            return orderedDCPNames;
        }

        public void setDcpNames(Set<String> dcpNames) {
            this.dcpNames = dcpNames;
        }
    }

}
