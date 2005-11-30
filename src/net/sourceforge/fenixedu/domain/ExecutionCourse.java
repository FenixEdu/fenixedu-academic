package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.ProposalState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ExecutionCourse extends ExecutionCourse_Base {

    public String toString() {
        String result = "[EXECUTION_COURSE";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", theoreticalHours=" + getTheoreticalHours();
        result += ", praticalHours=" + getPraticalHours();
        result += ", theoPratHours=" + getTheoPratHours();
        result += ", labHours=" + getLabHours();
        result += ", executionPeriod=" + getExecutionPeriod();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EC" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        IExecutionPeriod executionPeriod = getExecutionPeriod();
        return executionPeriod;
    }

    public List<IGrouping> getGroupings() {
        List<IGrouping> result = new ArrayList();
        for (final IExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                result.add(exportGrouping.getGrouping());
            }
        }
        return result;
    }

    public IGrouping getGroupingByName(String groupingName) {
        for (final IGrouping grouping : this.getGroupings()) {
            if (grouping.getName().equals(groupingName)) {
                return grouping;
            }
        }
        return null;
    }

    public boolean existsGroupingExecutionCourse(IExportGrouping groupPropertiesExecutionCourse) {
        return getExportGroupings().contains(groupPropertiesExecutionCourse);
    }

    public boolean existsGroupingExecutionCourse() {
        return getExportGroupings().isEmpty();
    }

    public boolean hasProposals() {
        boolean result = false;
        boolean found = false;
        List groupPropertiesExecutionCourseList = getExportGroupings();
        Iterator iter = groupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext() && !found) {

            IExportGrouping groupPropertiesExecutionCourseAux = (IExportGrouping) iter.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 3) {
                result = true;
                found = true;
            }

        }
        return result;
    }

    public boolean isMasterDegreeOnly() {
        for (final ICurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso() != DegreeType.MASTER_DEGREE) {
                return false;
            }
        }
        return true;
    }

    public void edit(String name, String acronym, double theoreticalHours,
            double theoreticalPraticalHours, double praticalHours, double laboratoryHours, String comment) {

        if (name == null || acronym == null || theoreticalHours < 0 || theoreticalPraticalHours < 0
                || praticalHours < 0 || laboratoryHours < 0 || comment == null)
            throw new NullPointerException();

        setNome(name);
        setSigla(acronym);
        setTheoreticalHours(theoreticalHours);
        setTheoPratHours(theoreticalPraticalHours);
        setPraticalHours(praticalHours);
        setLabHours(laboratoryHours);
        setComment(comment);
    }

    public void createSite() {
        final ISite site = new Site();
        site.setExecutionCourse(this);
    }

    public void createEvaluationMethod(final String evaluationElements,
            final String evaluationElementsEng) {
        if (evaluationElements == null || evaluationElementsEng == null)
            throw new NullPointerException();

        final IEvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setEvaluationElements(evaluationElements);
        evaluationMethod.setEvaluationElementsEn(evaluationElementsEng);
        evaluationMethod.setExecutionCourse(this);
    }

    public void createBibliographicReference(final String title, final String authors,
            final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null)
            throw new NullPointerException();

        final IBibliographicReference bibliographicReference = new BibliographicReference();
        bibliographicReference.setTitle(title);
        bibliographicReference.setAuthors(authors);
        bibliographicReference.setReference(reference);
        bibliographicReference.setYear(year);
        bibliographicReference.setOptional(optional);
        bibliographicReference.setExecutionCourse(this);
    }

    public ICourseReport createCourseReport(String report) {
        if (report == null)
            throw new NullPointerException();

        final ICourseReport courseReport = new CourseReport();
        courseReport.setReport(report);
        courseReport.setLastModificationDate(Calendar.getInstance().getTime());
        courseReport.setExecutionCourse(this);
        
        return courseReport;
    }

    private ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson) {

        if (title == null || summaryText == null || isExtraLesson == null)
            throw new NullPointerException();

        final ISummary summary = new Summary();
        summary.setTitle(title);
        summary.setSummaryText(summaryText);
        summary.setStudentsNumber(studentsNumber);
        summary.setIsExtraLesson(isExtraLesson);
        summary.setLastModifiedDate(Calendar.getInstance().getTime());
        summary.setExecutionCourse(this);

        return summary;
    }

    public ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, IProfessorship professorship) {

        if (professorship == null)
            throw new NullPointerException();

        final ISummary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setProfessorship(professorship);
        summary.setTeacher(null);
        summary.setTeacherName(null);

        return summary;
    }

    public ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, ITeacher teacher) {

        if (teacher == null)
            throw new NullPointerException();

        final ISummary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setTeacher(teacher);
        summary.setProfessorship(null);
        summary.setTeacherName(null);

        return summary;
    }

    public ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, String teacherName) {

        if (teacherName == null)
            throw new NullPointerException();

        final ISummary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setTeacherName(teacherName);
        summary.setTeacher(null);
        summary.setProfessorship(null);

        return summary;
    }

    public List<IProfessorship> responsibleFors() {
        final List<IProfessorship> res = new ArrayList<IProfessorship>();
        for (final IProfessorship professorship : this.getProfessorships()) {
            if (professorship.getResponsibleFor())
                res.add(professorship);
        }
        return res;
    }

    public IAttends getAttendsByStudent(final IStudent student) {

        return (IAttends) CollectionUtils.find(getAttends(), new Predicate() {

            public boolean evaluate(Object o) {
                IAttends attends = (IAttends) o;
                return attends.getAluno().equals(student);
            }

        });
    }

    public List<IExam> getAssociatedExams() {
        List<IExam> associatedExams = new ArrayList<IExam>();

        for (IEvaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof IExam) {
                associatedExams.add((IExam) evaluation);
            }
        }

        return associatedExams;
    }

    public List<IWrittenTest> getAssociatedWrittenTests() {
        List<IWrittenTest> associatedWrittenTests = new ArrayList<IWrittenTest>();

        for (IEvaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof IWrittenTest) {
                associatedWrittenTests.add((IWrittenTest) evaluation);
            }
        }

        return associatedWrittenTests;
    }

    public List<IOnlineTest> getAssociatedOnlineTests() {
        List<IOnlineTest> associatedOnlineTests = new ArrayList<IOnlineTest>();

        for (IEvaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof IOnlineTest) {
                associatedOnlineTests.add((IOnlineTest) evaluation);
            }
        }

        return associatedOnlineTests;
    }
    
    //Delete Methods
    
    public void delete() {
    	if(canBeDeleted()) {
	        setExecutionPeriod(null);
	        
	        if(getSite() != null) {
	        	getSite().delete();
	        }
	        	
	        for (;!getProfessorships().isEmpty(); getProfessorships().get(0).delete());
	        
	        getAssociatedCurricularCourses().clear();
	        
	        for (;!getExecutionCourseProperties().isEmpty(); getExecutionCourseProperties().get(0).delete());
	        
	        getNonAffiliatedTeachers().clear();
	        
	        super.deleteDomainObject();
    	} 
    	else 
    		throw new DomainException("error.execution.course.cant.delete");
    }
    
    private boolean canBeDeleted() {
        if (hasAnyAssociatedSummaries()) {
            return false;
        }
        if (!getGroupings().isEmpty()) {
            return false;
        }
        if (hasAnyAssociatedBibliographicReferences()) {
            return false;
        }
        if (hasAnyAssociatedEvaluations()) {
            return false;
        }
        if (hasAnyAttends()) {
            return false;
        }
        if (hasEvaluationMethod()) {
            return false;
        }
        if (hasAnyAssociatedShifts()) {
            return false;
        }
        if (hasCourseReport()) {
            return false;
        }
        final ISite site = getSite();
        if (site != null) {
            if (site.hasAnyAssociatedAnnouncements()) {
                return false;
            }
            if (site.hasAnyAssociatedSections()) {
                return false;
            }
        }
        
        for (final IProfessorship professorship : getProfessorships()) {
            if (professorship.hasAnyAssociatedShiftProfessorship()) {
                return false;
            }
            if (professorship.hasAnyAssociatedSummaries()) {
                return false;
            }
            if (professorship.hasAnySupportLessons()) {
                return false;
            }
        }

        return true;
    }
    
    public boolean teacherLecturesExecutionCourse(ITeacher teacher){
        for (IProfessorship professorship : this.getProfessorships()) {
            if(teacher.getProfessorships().contains(professorship)){
                return true;
            }
        }                
        return false;
    } 

    public List<IProject> getAssociatedProjects() {
        final List<IProject> result = new ArrayList<IProject>();
        
        for (IEvaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof IProject) {
                result.add((IProject) evaluation);
            }
        }
        return result;
    }
    
    
    private int countAssociatedStudentsByEnrolmentNumber(int enrolmentNumber){
    	int executionCourseAssociatedStudents = 0;
		
		for(ICurricularCourse curricularCourseFromExecutionCourseEntry: getAssociatedCurricularCourses()) {
			for(IEnrolment enrolmentsEntry: curricularCourseFromExecutionCourseEntry.getEnrolments()) {
				if(enrolmentsEntry.getExecutionPeriod() == getExecutionPeriod()) {
					
					IStudentCurricularPlan studentCurricularPlanEntry = enrolmentsEntry.getStudentCurricularPlan();
					int numberOfEnrolmentsForThatExecutionCourse = 0;
					
					for(IEnrolment enrolmentsFromStudentCPEntry: studentCurricularPlanEntry.getEnrolments()) {
						if(enrolmentsFromStudentCPEntry.getCurricularCourse() == curricularCourseFromExecutionCourseEntry) {
							++numberOfEnrolmentsForThatExecutionCourse;
						}
					}
						
					if(numberOfEnrolmentsForThatExecutionCourse == enrolmentNumber) {
						executionCourseAssociatedStudents++;
					}
				}
			}
		}
		
		return executionCourseAssociatedStudents;
    }
    
    
    public Integer getTotalEnrolmentStudentNumber() {  
		int executionCourseStudentNumber = 0;
		
		for(ICurricularCourse curricularCourseFromExecutionCourseEntry: getAssociatedCurricularCourses()) {
			for(IEnrolment enrolmentsEntry: curricularCourseFromExecutionCourseEntry.getEnrolments()) {
				if(enrolmentsEntry.getExecutionPeriod() == getExecutionPeriod()) {
					executionCourseStudentNumber++;
				}
			}
		}
		
		return executionCourseStudentNumber;
    }
    
    public Integer getFirstTimeEnrolmentStudentNumber(){
		
		return countAssociatedStudentsByEnrolmentNumber(1);
    }
    
    
    public Integer getSecondOrMoreTimeEnrolmentStudentNumber() {
		
		return getTotalEnrolmentStudentNumber() - getFirstTimeEnrolmentStudentNumber();    	
    }
    
    
    public Double getTotalHours(ShiftType shiftType) {
    	double totalTime = 0;
				
    	for(IShift shiftEntry : this.getAssociatedShifts()){
			if(shiftEntry.getTipo() == shiftType){
				totalTime += shiftEntry.hours();
			}
		}	
    	
    	return totalTime;
    }
        
    
    public Double getStudentsNumberByShift(ShiftType shiftType) {
    	int numShifts = 0;
		int executionCourseStudentNumber = getTotalEnrolmentStudentNumber();
    	
    	for(IShift shiftEntry : this.getAssociatedShifts()){
			if(shiftEntry.getTipo() == shiftType){
				numShifts++;
			}
		}
    	
    	if(numShifts == 0)
    		return 0.0;
    	else 
    		return (double) executionCourseStudentNumber / numShifts;
    }

}
