/*
 * Created on 12/Nov/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class InfoSiteCourseInformation extends DataTranferObject implements ISiteComponent {

    private DomainReference<ExecutionCourse> executionCourseDomainReference;
    
    private DomainReference<ExecutionYear> executionYearDomainReference;
    
    public InfoSiteCourseInformation(final ExecutionCourse executionCourse) {
	executionCourseDomainReference = new DomainReference<ExecutionCourse>(executionCourse);
    }

    public InfoSiteCourseInformation(final ExecutionCourse executionCourse, final ExecutionYear executionYear) {
	this(executionCourse);
	executionYearDomainReference = new DomainReference<ExecutionYear>(executionYear);
    }

    private ExecutionCourse getExecutionCourse() {
	return this.executionCourseDomainReference == null ?  null : this.executionCourseDomainReference.getObject();
    }

    private ExecutionYear getExecutionYear() {
	return this.executionYearDomainReference == null ?  null : this.executionYearDomainReference.getObject();
    }


    //=================== FIELDS RETRIEVED BY DOMAIN LOGIC =======================
    
    public InfoExecutionCourse getInfoExecutionCourse() {
        return InfoExecutionCourse.newInfoFromDomain(getExecutionCourse());
    }

    public InfoCourseReport getInfoCourseReport() {
        return getInfoExecutionCourse().getInfoCourseReport();
    }

    public InfoEvaluationMethod getInfoEvaluationMethod() {
        return getInfoExecutionCourse().getInfoEvaluationMethod();
    }

    public List<InfoBibliographicReference> getInfoBibliographicReferences() {
        return getInfoExecutionCourse().getAssociatedInfoBibliographicReferences();
    }

    public List<InfoCurricularCourse> getInfoCurricularCourses() {
	return getExecutionYear() == null ? getInfoExecutionCourse().getAssociatedInfoCurricularCourses() : getInfoExecutionCourse().getAssociatedInfoCurricularCourses(getExecutionYear());
    }

    public List<InfoTeacher> getInfoLecturingTeachers() {
	final List<InfoTeacher> result = new ArrayList<InfoTeacher>();
	
        for (final Professorship professorship : getExecutionCourse().getProfessorships()) {
            result.add(InfoTeacher.newInfoFromDomain(professorship.getTeacher()));
        }
        
        return result;
    }

    public List<InfoTeacher> getInfoResponsibleTeachers() {
	final List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        
	for (final Professorship responsibleFor : getExecutionCourse().responsibleFors()) {
            result.add(InfoTeacher.newInfoFromDomain(responsibleFor.getTeacher()));
        }

        return result;
    }

    
    //=================== FIELDS NOT RETRIEVED BY DOMAIN LOGIC =======================
    
    public Integer getNumberOfTheoLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.TEORICA));
    }

    public Integer getNumberOfPratLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.PRATICA));
    }

    public Integer getNumberOfTheoPratLessons() {
	return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.TEORICO_PRATICA));
    }

    public Integer getNumberOfLabLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.LABORATORIAL));
    }

    private Integer getNumberOfLessons(final Collection<Shift> shifts) {
        int result = 0;
	
        for (final Shift shift : shifts) {
            result += shift.getAssociatedLessonsCount();
        }
        
        return result;
    }

    static final private int MIN_LENGTH = 10;
    
    public Integer getNumberOfFieldsFilled() {
        int result = 0;

        if (!getInfoLecturingTeachers().isEmpty())
            result++;
        Iterator iter = getInfoBibliographicReferences().iterator();
        while (iter.hasNext()) {
            InfoBibliographicReference infoBibliographicReference = (InfoBibliographicReference) iter
                    .next();
            if (infoBibliographicReference.getTitle() != null
                    && infoBibliographicReference.getTitle().length() > MIN_LENGTH) {
                result++;
                break;
            }
        }
        if (getInfoEvaluationMethod() != null && getInfoEvaluationMethod().getEvaluationElements() != null
                && getInfoEvaluationMethod().getEvaluationElements().getContent(Language.pt) != null
                && getInfoEvaluationMethod().getEvaluationElements().getContent(Language.pt).length() > MIN_LENGTH)
            result++;

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getGeneralObjectives() != null
                    && infoCurriculum.getGeneralObjectives().length() > MIN_LENGTH) {
                result++;
                break;
            }
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getProgram() != null && infoCurriculum.getProgram().length() > MIN_LENGTH) {
                result++;
                break;
            }
        }

        return new Integer(result);
    }

    public Integer getNumberOfFieldsFilledEn() {
        int numberOfFieldsFilled = 0;

        if (!getInfoLecturingTeachers().isEmpty())
            numberOfFieldsFilled++;
        Iterator iter = getInfoBibliographicReferences().iterator();
        while (iter.hasNext()) {
            InfoBibliographicReference infoBibliographicReference = (InfoBibliographicReference) iter
                    .next();
            if (infoBibliographicReference.getTitle().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }
        if (getInfoEvaluationMethod() != null
                && getInfoEvaluationMethod().getEvaluationElements() != null
                && getInfoEvaluationMethod().getEvaluationElements().getContent(Language.en) != null
                && getInfoEvaluationMethod().getEvaluationElements().getContent(Language.en).length() > MIN_LENGTH)
            numberOfFieldsFilled++;

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getGeneralObjectivesEn() != null
                    && infoCurriculum.getGeneralObjectivesEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getOperacionalObjectivesEn() != null
                    && infoCurriculum.getOperacionalObjectivesEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getProgramEn() != null
                    && infoCurriculum.getProgramEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        return new Integer(numberOfFieldsFilled);
    }

    public Date getLastModificationDate() {
	final Set<DateTime> dates = new HashSet<DateTime>();
        
	if (getExecutionCourse().hasCourseReport()) {
	    dates.add(getExecutionCourse().getCourseReport().getLastModificationDateDateTime());    
	}
	
        for (final Curriculum curriculum : getExecutionCourse().getCurriculums(getExecutionYear())) {
            dates.add(curriculum.getLastModificationDateDateTime());
        }
	
        return dates.isEmpty() ? null : Collections.max(dates).toDate();
    }

    public List<InfoDepartment> getInfoDepartments() {
	final Set<Teacher> responsibleForTeachers = new HashSet<Teacher>();
	for (final Professorship responsibleFor : getExecutionCourse().responsibleFors()) {
	    responsibleForTeachers.add(responsibleFor.getTeacher());
	}

	final List<InfoDepartment> result = new ArrayList<InfoDepartment>();
	
	for (final Teacher teacher : responsibleForTeachers) {
	    result.add(InfoDepartment.newInfoFromDomain(teacher.getCurrentWorkingDepartment()));
        }

        return result;
    }

    public List<InfoCurriculum> getInfoCurriculums() {
        final List<InfoCurriculum> result = new ArrayList<InfoCurriculum>();
        
        for (final Curriculum curriculum : getExecutionCourse().getCurriculums(getExecutionYear())) {
            final InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
            
            final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curriculum.getCurricularCourse());
            infoCurricularCourse.setInfoScopes(getInfoScopes(curriculum.getCurricularCourse()));
            infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
            
            result.add(infoCurriculum);
        }

        return result;
    }

    private List<InfoCurricularCourseScope> getInfoScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();
        
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
            result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
        }
        
        return result;
    }
    
    public List<InfoLesson> getInfoLessons() {
    	final List<InfoLesson> result = new ArrayList<InfoLesson>();

    	for (final Lesson lesson : getExecutionCourse().getLessons()) {
    	    result.add(InfoLesson.newInfoFromDomain(lesson));
    	}
    	
    	return getFilteredInfoLessons(result);
    }

    private List<InfoLesson> getFilteredInfoLessons(final List<InfoLesson> infoLessons) {
        final List<InfoLesson> result = new ArrayList<InfoLesson>();
        
        InfoLesson infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.TEORICA);
        if (infoLesson != null)
            result.add(infoLesson);

        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.PRATICA);
        if (infoLesson != null)
            result.add(infoLesson);

        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.LABORATORIAL);
        if (infoLesson != null)
            result.add(infoLesson);

        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.TEORICO_PRATICA);
        if (infoLesson != null)
            result.add(infoLesson);
        
        return result;
    }

    private InfoLesson getFilteredInfoLessonByType(List infoLessons, ShiftType type) {
        final ShiftType lessonType = type;
        InfoLesson infoLesson = (InfoLesson) CollectionUtils.find(infoLessons, new Predicate() {
            public boolean evaluate(Object o) {
                InfoLesson infoLesson = (InfoLesson) o;
                return infoLesson.getTipo().equals(lessonType);
            }
        });
        return infoLesson;
    }
    
    public List<InfoSiteEvaluationInformation> getInfoSiteEvaluationInformations() {
        final List<InfoSiteEvaluationInformation> result = new ArrayList<InfoSiteEvaluationInformation>();
        
        final ExecutionPeriod executionPeriod = getExecutionCourse().getExecutionPeriod();
        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            final InfoSiteEvaluationInformation infoSiteEvaluationInformation = new InfoSiteEvaluationInformation();
            
            final InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            final List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
            infoSiteEvaluationStatistics.setEnrolled(enrolled.size());
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));

            infoSiteEvaluationInformation.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);
            infoSiteEvaluationInformation.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
            infoSiteEvaluationInformation.setInfoSiteEvaluationHistory(getInfoSiteEvaluationsHistory(executionPeriod, curricularCourse));
            
            result.add(infoSiteEvaluationInformation);
        }

        return result;
    }

    private int getEvaluated(List enrolments) {
        int result = 0;
        
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            Enrolment enrolment = (Enrolment) iter.next();
            if (enrolment.isEnrolmentStateApproved()
                    || enrolment.isEnrolmentStateNotApproved()) {
                result++;
            }
        }
        
        return result;
    }

    private int getApproved(List enrolments) {
        int result = 0;
        
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            Enrolment enrolment = (Enrolment) iter.next();
            if (enrolment.isEnrolmentStateApproved()) {
                result++;
            }
        }
        
        return result;
    }

    private List<InfoSiteEvaluationStatistics> getInfoSiteEvaluationsHistory(final ExecutionPeriod executionPeriodToTest, final CurricularCourse curricularCourse) {
        final List<InfoSiteEvaluationStatistics> result = new ArrayList<InfoSiteEvaluationStatistics>();

        final Set<ExecutionPeriod> executionPeriods = new HashSet<ExecutionPeriod>();
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

            // filter the executionPeriods by semester;
            // also, information regarding execution years after the course's
            // execution year must not be shown
            if (executionPeriod.getSemester().equals(executionPeriodToTest.getSemester())
        	    && executionPeriod.getExecutionYear().isBefore(executionPeriodToTest.getExecutionYear())) {
        	executionPeriods.add(executionPeriod);
            }
        }

        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            final InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
            
            List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
            infoSiteEvaluationStatistics.setEnrolled(enrolled.size());
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
            
            result.add(infoSiteEvaluationStatistics);
        }

        Collections.sort(result, new ReverseComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.year")));
        
        return result;
    }

}
