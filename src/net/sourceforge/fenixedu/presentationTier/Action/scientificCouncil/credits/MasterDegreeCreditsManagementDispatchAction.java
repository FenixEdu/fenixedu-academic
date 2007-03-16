/**
 * Jan 30, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.GenericTrio;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTime;

import pt.ist.utl.fenix.utils.Pair;

/**
 * @author Ricardo Rodrigues
 * Modified by Manuel Pinto
 */

public class MasterDegreeCreditsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYears();

	List<ExecutionYear> notClosedExecutionYears = (List<ExecutionYear>) CollectionUtils.select(executionYears, new Predicate() {
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
	   
	    List<MasterDegreeCreditsDTO> masterDegreeCoursesDTOs = getListing(executionDegree);	    
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

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

	ExecutionDegree executionDegree = getExecutionDegreeFromParameter(request);
        List<MasterDegreeCreditsDTO> listing = getListing(executionDegree);
        if (!listing.isEmpty()) {
            Collections.sort(listing, new BeanComparator("curricularCourse.name"));                     
        }
        
        try {
            String filename = getFileName(executionDegree);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportToXls(executionDegree, listing, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }
            
    public class MasterDegreeCreditsDTO {

	CurricularCourse curricularCourse;

	StringBuilder semesters = new StringBuilder();

	Map<ExecutionCourse, String> dcpNames = new HashMap<ExecutionCourse, String>();

	int totalRowSpan = 0;

	Map<ExecutionPeriod, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>> executionCoursesMap = new TreeMap<ExecutionPeriod, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>>();

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
	       
	    for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
		
		List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod);			
		for (ExecutionCourse executionCourse : executionCourses) {		    	                		                   
            				                       
                    dcpNames.put(executionCourse, getExecutionCourseDCPNames(executionCourse).toString());
		   		    
                    List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>> executionPeriodMap = executionCoursesMap.get(executionPeriod);
                    executionPeriodMap = executionPeriodMap == null ? new ArrayList<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>() : executionPeriodMap;
                    
                    GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer> executionCourseMap = new GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>(
                	    new Pair<ExecutionCourse, Boolean>(executionCourse, executionCourse.isMasterDegreeDFAOrDEAOnly()), 
                	    curricularCourse.getEnrolmentsByExecutionPeriod(executionCourse.getExecutionPeriod()).size(), 
                	    Integer.valueOf(curricularCourse.getNumberOfStudentsWithFirstEnrolmentIn(executionCourse.getExecutionPeriod())));
                    
                    executionPeriodMap.add(executionCourseMap);		 
                    
                    executionCoursesMap.put(executionPeriod, executionPeriodMap);
		    
		    int profCounter = executionCourse.getProfessorshipsCount();
		    if (profCounter == 0) {
			profCounter = 1;
		    }		 
		    
		    totalRowSpan += profCounter;
		}	   		 
	    }	    	   
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

	public Map<ExecutionPeriod, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>> getExecutionCoursesMap() {
	    return executionCoursesMap;
	}

	public StringBuilder getSemesters() {
	    return semesters;
	}

	public void setSemesters(StringBuilder semesters) {
	    this.semesters = semesters;
	}

	public Map<ExecutionCourse, String> getDcpNames() {
	    return dcpNames;
	}

	public void setDcpNames(Map<ExecutionCourse, String> dcpNames) {
	    this.dcpNames = dcpNames;
	}
    }

    // Private Methods
    
    private List<MasterDegreeCreditsDTO> getListing(ExecutionDegree executionDegree) {
        List<CurricularCourse> curricularCourses = executionDegree.getDegreeCurricularPlan().getCurricularCoursesWithExecutionIn(executionDegree.getExecutionYear());
        List<MasterDegreeCreditsDTO> masterDegreeCoursesDTOs = new ArrayList<MasterDegreeCreditsDTO>();
        for (CurricularCourse curricularCourse : curricularCourses) {
            MasterDegreeCreditsDTO masterDegreeCreditsDTO = new MasterDegreeCreditsDTO(curricularCourse, executionDegree.getExecutionYear());
            masterDegreeCoursesDTOs.add(masterDegreeCreditsDTO);
        }       
        return masterDegreeCoursesDTOs;
    }
    
    private void exportToXls(ExecutionDegree executionDegree, List<MasterDegreeCreditsDTO> listing, ServletOutputStream writer) throws IOException {
	final List<Object> headers = getHeaders();
	final Spreadsheet spreadsheet = new Spreadsheet(executionDegree.getDegreeCurricularPlan().getName().replace('/', '_'), headers);
	fillSpreadSheet(executionDegree, listing, spreadsheet);
	spreadsheet.exportToXLSSheet(writer);	
    }

    private void fillSpreadSheet(ExecutionDegree executionDegree, List<MasterDegreeCreditsDTO> listing, Spreadsheet spreadsheet) {
	for (MasterDegreeCreditsDTO masterDegreeCreditsDTO : listing) {
	    Map<ExecutionPeriod, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>> executionCoursesMap = masterDegreeCreditsDTO.getExecutionCoursesMap();
	    for (ExecutionPeriod executionPeriod : executionCoursesMap.keySet()) {
		for (GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer> executionCourseMap : executionCoursesMap.get(executionPeriod)) {
		    ExecutionCourse executionCourse = executionCourseMap.getFirst().getKey();		    		    
		    final Row row = spreadsheet.addRow();
		    row.setCell(executionDegree.getDegreeCurricularPlan().getName());
		    row.setCell(masterDegreeCreditsDTO.getCurricularCourse().getName());
		    row.setCell(enumerationResources.getString(masterDegreeCreditsDTO.getCurricularCourse().getType().getName()));
		    row.setCell(masterDegreeCreditsDTO.getCurricularCourse().getCredits().toString());
		    row.setCell(String.valueOf(executionCourseMap.getSecond()));
		    row.setCell(String.valueOf(executionCourseMap.getThird()));		    
		    row.setCell(executionCourse.getExecutionPeriod().getSemester().toString());
		    row.setCell(executionCourse.getSigla() + "\n");
		    row.setCell(getTeachersNumbers(executionCourse).toString());
		    row.setCell(getTeachersNames(executionCourse).toString());
		    row.setCell(getTeachersDepartaments(executionCourse).toString());
		    row.setCell(getTeachersHours(executionCourse).toString());
		    row.setCell(getTeachersCredits(executionCourse).toString());
		    row.setCell(getExecutionCourseDCPNames(executionCourse).toString());
		}
	    }	    	  
	}
    }
    
    private StringBuilder getExecutionCourseDCPNames(ExecutionCourse executionCourse) {
	StringBuilder builder = new StringBuilder();
        for (CurricularCourse tempCurricularCourse : executionCourse.getAssociatedCurricularCourses()) {                	
            builder.append(tempCurricularCourse.getDegreeCurricularPlan().getName()).append(";\n");
        }
        return builder;
    }    

    private StringBuilder getTeachersCredits(ExecutionCourse executionCourse) {
	StringBuilder teachers = new StringBuilder();
	for (Professorship professorship : executionCourse.getProfessorships()) {	    	   	    
	    TeacherService teacherService = professorship.getTeacher().getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
	    TeacherMasterDegreeService masterDegreeService = null;
	    if(teacherService != null){
		masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);	
	    } 	    
	    teachers.append(masterDegreeService != null ? masterDegreeService.getCredits() : "").append("\n");	   
	}
	return teachers;
    }
    
    private StringBuilder getTeachersHours(ExecutionCourse executionCourse) {
	StringBuilder teachers = new StringBuilder();
	for (Professorship professorship : executionCourse.getProfessorships()) {	    	   	    
	    TeacherService teacherService = professorship.getTeacher().getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
	    TeacherMasterDegreeService masterDegreeService = null;
	    if(teacherService != null){
		masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);	
	    } 	    
	    teachers.append(masterDegreeService != null ? masterDegreeService.getHours() : "").append("\n");	   
	}
	return teachers;
    }
    
    private StringBuilder getTeachersDepartaments(ExecutionCourse executionCourse) {
	StringBuilder teachers = new StringBuilder();
	for (Professorship professorship : executionCourse.getProfessorships()) {
	    ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
	    Department department = professorship.getTeacher().getLastWorkingDepartment(executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay());
	    teachers.append(department != null ? department.getRealName() : "").append("\n");
	}
	return teachers;
    }
    
    private StringBuilder getTeachersNames(ExecutionCourse executionCourse) {
	StringBuilder teachers = new StringBuilder();
	for (Professorship professorship : executionCourse.getProfessorships()) {
	    teachers.append(professorship.getTeacher().getPerson().getName()).append("\n");
	}
	return teachers;
    }

    private StringBuilder getTeachersNumbers(ExecutionCourse executionCourse) {
	StringBuilder teachers = new StringBuilder();
	for (Professorship professorship : executionCourse.getProfessorships()) {
	    teachers.append(professorship.getTeacher().getTeacherNumber()).append("\n");
	}
	return teachers;
    }
    
    private String getFileName(ExecutionDegree executionDegree) {
	return executionDegree.getDegreeCurricularPlan().getName() + "_" + new DateTime().toString("dd_MM_yyyy_HH_mm");
    }
    
    private List<Object> getHeaders() {
        final List<Object> headers = new ArrayList<Object>();        
        headers.add("Plano Curricular");
        headers.add("Disciplina");        
        headers.add("Tipo");        
        headers.add("Créditos");
        headers.add("Nº Alunos Inscritos (Total)");
        headers.add("Nº Alunos Inscritos (1º vez)");
        headers.add("Semestre");
        headers.add("Código da Execução");
        headers.add("Número");
        headers.add("Docente");
        headers.add("Departamento");
        headers.add("Horas Leccionadas");
        headers.add("Créditos Lectivos");
        headers.add("Planos Curriculares");
        return headers;
    }   
    
    private ExecutionDegree getExecutionDegreeFromParameter(final HttpServletRequest request) {	
	final String executionDegreeIDString = request.getParameter("executionDegreeID");
	final Integer executionDegreeID = Integer.valueOf(executionDegreeIDString);
	return rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
    } 
}
