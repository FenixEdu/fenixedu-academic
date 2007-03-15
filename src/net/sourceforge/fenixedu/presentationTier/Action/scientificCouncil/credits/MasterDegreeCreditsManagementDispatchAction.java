/**
 * Jan 30, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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

	Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYears();

	List<ExecutionYear> notClosedExecutionYears = (List<ExecutionYear>) CollectionUtils.select(
		executionYears, new Predicate() {
		    public boolean evaluate(Object arg0) {
			ExecutionYear executionYear = (ExecutionYear) arg0;
			return !executionYear.getState().equals(PeriodState.CLOSED);
		    }
		});

	Iterator orderedExecutionYearsIter = new OrderedIterator(notClosedExecutionYears.iterator(), new BeanComparator("beginDate"));
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
	
	List<ExecutionDegree> executionDegrees = (List<ExecutionDegree>) executionYear.getExecutionDegreesByType(DegreeType.MASTER_DEGREE);	
	executionDegrees.addAll(executionYear.getExecutionDegreesByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
	executionDegrees.addAll(executionYear.getExecutionDegreesByType(DegreeType.BOLONHA_SPECIALIZATION_DEGREE));
		
	Iterator orderedExecutionDegreesIter = new OrderedIterator(executionDegrees.iterator(), new BeanComparator("degreeCurricularPlan.name"));
	request.setAttribute("masterDegreeExecutions", orderedExecutionDegreesIter);
	return mapping.findForward("chooseMasterDegreeExecution");
    }

    public ActionForward viewMasterDegreeCredits(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	Integer executionDegreeID = (Integer) dynaForm.get("executionDegreeID");

	if (executionDegreeID != null) {
	    ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
	    request.setAttribute("executionDegree", executionDegree);
	   
	    List<CurricularCourse> curricularCourses = executionDegree.getDegreeCurricularPlan().getCurricularCoursesWithExecutionIn(executionDegree.getExecutionYear());
	    List<MasterDegreeCreditsDTO> masterDegreeCoursesDTOs = new ArrayList<MasterDegreeCreditsDTO>();
	    for (CurricularCourse curricularCourse : curricularCourses) {
		MasterDegreeCreditsDTO masterDegreeCreditsDTO = new MasterDegreeCreditsDTO(curricularCourse, executionDegree.getExecutionYear());
		masterDegreeCoursesDTOs.add(masterDegreeCreditsDTO);
	    }
	    if (!masterDegreeCoursesDTOs.isEmpty()) {
		Iterator orderedCoursesIter = new OrderedIterator(masterDegreeCoursesDTOs.iterator(), new BeanComparator("curricularCourse.name"));
		request.setAttribute("masterDegreeCoursesDTOs", orderedCoursesIter);
	    }
	    
	    return mapping.findForward("showCreditsReport");
	    
	} else {
	    return prepare(mapping, form, request, response);
	}
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;

	Integer curricularCourseID = (Integer) dynaForm.get("curricularCourseID");
	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);

	Integer executionDegreeID = (Integer) dynaForm.get("executionDegreeID");
	ExecutionDegree executionDegree = null;
	
	if (executionDegreeID != null) {
	    executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
	} else {
	    Integer executionCourseID = Integer.parseInt(request.getParameter("executionCourseId"));
	    ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
	    executionDegree = curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionCourse.getExecutionPeriod().getExecutionYear());
	}
	
	request.setAttribute("executionDegree", executionDegree);
	
	MasterDegreeCreditsDTO masterDegreeCreditsDTO = new MasterDegreeCreditsDTO(curricularCourse, executionDegree.getExecutionYear());	
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

	boolean allowToChange = false;

	Map<ExecutionPeriod, List<ExecutionCourse>> executionCoursesMap = new TreeMap<ExecutionPeriod, List<ExecutionCourse>>();

	public MasterDegreeCreditsDTO(CurricularCourse curricularCourse, ExecutionYear executionYear) {
	    
	    setCurricularCourse(curricularCourse);
	 	    
	    List<DegreeModuleScope> ccsList = curricularCourse.getDegreeModuleScopes();
	    Iterator<DegreeModuleScope> cssListIter = ccsList.iterator();	    
	    List<Integer> semesters = new ArrayList();
	    
	    if (!ccsList.isEmpty()) {
		
		while (cssListIter.hasNext()) {
		    DegreeModuleScope ccs = (DegreeModuleScope) cssListIter.next();
		    if (!semesters.contains(ccs.getCurricularSemester())) {
			semesters.add(ccs.getCurricularSemester());
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
		
		List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod);
		
		ExecutionCourse firstExecutionCourse = executionCourses.isEmpty() ? null : executionCourses.get(0);
		for (ExecutionCourse executionCourse : executionCourses) {

		    if(isAllowToChange() || firstExecutionCourse.equals(executionCourse)) {
                        if (executionCourse.isMasterDegreeOnly() || executionCourse.isDEAOnly() || executionCourse.isDFAOnly()) {
                            setAllowToChange(true);
                        } else {
                            setAllowToChange(false);
                        }
		    }
		    		   
                    for (CurricularCourse tempCurricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                        dcpNames.add(tempCurricularCourse.getDegreeCurricularPlan().getName());
                    }
		   		    
                    List<ExecutionCourse> executionPeriodExecutionCourses = executionCoursesMap.get(executionPeriod);
                    executionPeriodExecutionCourses = executionPeriodExecutionCourses == null ? new ArrayList<ExecutionCourse>() : executionPeriodExecutionCourses;
                    executionPeriodExecutionCourses.add(executionCourse);		    
                    executionCoursesMap.put(executionPeriod, executionPeriodExecutionCourses);
		    
		    int profCounter = executionCourse.getProfessorshipsCount();
		    if (profCounter == 0) {
			profCounter = 1;
		    }		 
		    
		    totalRowSpan += profCounter;
		}	   		 
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

	public int getNumberEnrolments() {
	    return numberEnrolments;
	}

	public Map<ExecutionPeriod, List<ExecutionCourse>> getExecutionCoursesMap() {
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
