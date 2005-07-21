package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.fileSuport.INode;

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

    public List getGroupProperties() {
        List groupProperties = new ArrayList();
        if (getGroupPropertiesExecutionCourse() != null) {
            Iterator iterGroupPropertiesExecutionCourse = getGroupPropertiesExecutionCourse().iterator();
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = null;
            while (iterGroupPropertiesExecutionCourse.hasNext()) {
                groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) iterGroupPropertiesExecutionCourse
                        .next();
                if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1
                        || groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2) {
                    groupProperties.add(groupPropertiesExecutionCourse.getGroupProperties());
                }
            }
        }
        return groupProperties;
    }

    public IGroupProperties getGroupPropertiesByName(String name) {
        Iterator iter = getGroupProperties().iterator();
        while (iter.hasNext()) {
            IGroupProperties gp = (IGroupProperties) iter.next();
            if ((gp.getName()).equals(name))
                return gp;
        }
        return null;
    }

    public boolean existsGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        return getGroupPropertiesExecutionCourse().contains(groupPropertiesExecutionCourse);
    }

    public boolean existsGroupPropertiesExecutionCourse() {
        return getGroupPropertiesExecutionCourse().isEmpty();
    }

    public boolean hasProposals() {
        boolean result = false;
        boolean found = false;
        List groupPropertiesExecutionCourseList = getGroupPropertiesExecutionCourse();
        Iterator iter = groupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext() && !found) {

            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourseAux = (IGroupPropertiesExecutionCourse) iter
                    .next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 3) {
                result = true;
                found = true;
            }

        }
        return result;
    }

    public boolean isMasterDegreeOnly() {
        return CollectionUtils.exists(this.getAssociatedCurricularCourses(), new Predicate() {

            public boolean evaluate(Object input) {
                ICurricularCourse curricularCourse = (ICurricularCourse) input;
                return !curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                        DegreeType.DEGREE);
            }
        });
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

    public void createCourseReport(String report) {
        if (report == null)
            throw new NullPointerException();

        final ICourseReport courseReport = new CourseReport();
        courseReport.setReport(report);
        courseReport.setLastModificationDate(Calendar.getInstance().getTime());
        courseReport.setExecutionCourse(this);
    }

    private ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson) {
        
        if (title == null || summaryText == null || studentsNumber == null || isExtraLesson == null)
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
    
    public List responsibleFors() {
        List<IProfessorship> professorships = this.getProfessorships();
        List res = new ArrayList();

        for (IProfessorship professorship : professorships) {
            if (professorship.getResponsibleFor())
                res.add(professorship);
        }
        return res;
    }
	
	public IAttends getAttendsByStudent (final IStudent student) {
		
		return (IAttends)CollectionUtils.find(getAttends(),new Predicate() {

			public boolean evaluate(Object o) {
				IAttends attends = (IAttends) o;
				return attends.getAluno().equals(student);
			}
			
		});
	}
}
