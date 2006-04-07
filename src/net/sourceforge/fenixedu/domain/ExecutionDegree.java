/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {

    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME;
    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR;
    static {
        final Comparator degreeTypeComparator = new BeanComparator("degreeCurricularPlan.degree.tipoCurso");
        final Comparator degreeNameComparator = new BeanComparator("degreeCurricularPlan.degree.nome");
        final Comparator executionYearComparator = new BeanComparator("executionYear.year");
    	EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME = new ComparatorChain();
    	((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME).addComparator(degreeTypeComparator);
    	((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME).addComparator(degreeNameComparator);
        EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR = new ComparatorChain();
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR).addComparator(degreeTypeComparator);
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR).addComparator(degreeNameComparator);
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR).addComparator(executionYearComparator);
    }

    public ExecutionDegree() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
    
    public void edit(ExecutionYear executionYear, Campus campus, Boolean temporaryExamMap,
            Date periodLessonsFirstSemesterBegin, Date periodLessonsFirstSemesterEnd,
            Date periodExamsFirstSemesterBegin, Date periodExamsFirstSemesterEnd,
            Date periodLessonsSecondSemesterBegin, Date periodLessonsSecondSemesterEnd,
            Date periodExamsSecondSemesterBegin, Date periodExamsSecondSemesterEnd) {
     
        checkPeriodDates(periodLessonsFirstSemesterBegin,
                periodLessonsFirstSemesterEnd, periodExamsFirstSemesterBegin, periodExamsFirstSemesterEnd,
                periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd, periodExamsSecondSemesterBegin,
                periodExamsSecondSemesterEnd);
        
        setExecutionYear(executionYear);
        setCampus(campus);
        setTemporaryExamMap(temporaryExamMap);
        getPeriodLessonsFirstSemester().setStart(periodLessonsFirstSemesterBegin);
        getPeriodLessonsFirstSemester().setEnd(periodLessonsFirstSemesterEnd);
        getPeriodExamsFirstSemester().setStart(periodExamsFirstSemesterBegin);
        getPeriodExamsFirstSemester().setEnd(periodExamsFirstSemesterEnd);
        getPeriodLessonsSecondSemester().setStart(periodLessonsSecondSemesterBegin);
        getPeriodLessonsSecondSemester().setEnd(periodLessonsSecondSemesterEnd);
        getPeriodExamsSecondSemester().setStart(periodExamsSecondSemesterBegin);
        getPeriodExamsSecondSemester().setEnd(periodExamsSecondSemesterEnd);
    }

	private void checkPeriodDates(Date periodLessonsFirstSemesterBegin,
            Date periodLessonsFirstSemesterEnd, Date periodExamsFirstSemesterBegin,
            Date periodExamsFirstSemesterEnd, Date periodLessonsSecondSemesterBegin,
            Date periodLessonsSecondSemesterEnd, Date periodExamsSecondSemesterBegin,
            Date periodExamsSecondSemesterEnd) {
        
        if (periodLessonsFirstSemesterBegin == null || periodLessonsFirstSemesterEnd == null
                || periodLessonsFirstSemesterBegin.after(periodLessonsFirstSemesterEnd)) {
            throw new DomainException("error.executionDegree.beginPeriod.after.endPeriod");
        }

        if (periodExamsFirstSemesterBegin == null || periodExamsFirstSemesterEnd == null
                || periodExamsFirstSemesterBegin.after(periodExamsFirstSemesterEnd)) {
            throw new DomainException("error.executionDegree.beginPeriod.after.endPeriod");
        }

        if (periodLessonsSecondSemesterBegin == null || periodLessonsSecondSemesterEnd == null
                || periodLessonsSecondSemesterBegin.after(periodLessonsSecondSemesterEnd)) {
            throw new DomainException("error.executionDegree.beginPeriod.after.endPeriod");
        }

        if (periodExamsSecondSemesterBegin == null || periodExamsSecondSemesterEnd == null
                || periodExamsSecondSemesterBegin.after(periodExamsSecondSemesterEnd)) {
            throw new DomainException("error.executionDegree.beginPeriod.after.endPeriod");
        }
    }

    public boolean isFirstYear() {

        List<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();

        ExecutionDegree firstExecutionDegree = (ExecutionDegree) Collections.min(executionDegrees,
                new BeanComparator("executionYear.year"));

        if (firstExecutionDegree.equals(this)) {
            return true;
        }

        return false;
    }

	public Set<Shift> findAvailableShifts(final CurricularYear curricularYear, final ExecutionPeriod executionPeriod) {
		final Set<Shift> shifts = new HashSet<Shift>();
		for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCourses()) {
			if (curricularCourse.hasScopeForCurricularYear(curricularYear.getYear())) {
				for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
					if (executionCourse.getExecutionPeriod() == executionPeriod) {
						shifts.addAll(executionCourse.getAssociatedShifts());
					}
				}
			}
		}
		return shifts;
	}

	public Set<SchoolClass> findSchoolClassesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
		final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		for (final SchoolClass schoolClass : getSchoolClasses()) {
			if (schoolClass.getExecutionPeriod() == executionPeriod) {
				schoolClasses.add(schoolClass);
			}
		}
		return schoolClasses;
	}

	public Set<SchoolClass> findSchoolClassesByExecutionPeriodAndCurricularYear(final ExecutionPeriod executionPeriod, final Integer curricularYear) {
		final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		for (final SchoolClass schoolClass : getSchoolClasses()) {
			if (schoolClass.getExecutionPeriod() == executionPeriod && schoolClass.getAnoCurricular().equals(curricularYear)) {
				schoolClasses.add(schoolClass);
			}
		}
		return schoolClasses;
	}

	public SchoolClass findSchoolClassesByExecutionPeriodAndName(final ExecutionPeriod executionPeriod, final String name) {
		for (final SchoolClass schoolClass : getSchoolClasses()) {
			if (schoolClass.getExecutionPeriod() == executionPeriod && schoolClass.getNome().equals(name)) {
				return schoolClass;
			}
		}
		return null;
	}

    public List<CandidateSituation> getCandidateSituationsInSituation(List<SituationName> situationNames) {
        List<CandidateSituation> result = new ArrayList<CandidateSituation>();

        for (MasterDegreeCandidate candidate : getMasterDegreeCandidates()) {
            for (CandidateSituation situation : candidate.getSituations()) {

                if (situation.getValidation().getState() == null || situation.getValidation().getState() != State.ACTIVE) {
                    continue;
                }
                
                if (situationNames != null && !situationNames.contains(situation.getSituation())) {
                    continue;
                }

                result.add(situation);
            }
        }
        
        return result;
    }
	
	public Coordinator getCoordinatorByTeacher(Teacher teacher) {
		for (Coordinator coordinator : getCoordinatorsList()) {
			if (coordinator.getTeacher().equals(teacher)) {
				return coordinator;
			}
		}
        
		return null;
	}
    
    public MasterDegreeCandidate getMasterDegreeCandidateBySpecializationAndCandidateNumber(
            Specialization specialization, Integer candidateNumber) {
        
        for (final MasterDegreeCandidate masterDegreeCandidate : this.getMasterDegreeCandidatesSet()) {
            if (masterDegreeCandidate.getSpecialization() == specialization
                    && masterDegreeCandidate.getCandidateNumber().equals(candidateNumber)) {
                return masterDegreeCandidate;
            }
        }
        return null;
    }
    

    public static List<ExecutionDegree> getAllByExecutionYear(String year) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        
        if (year == null) {
            return result;
        }
        
        for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
            if (! year.equals(executionDegree.getExecutionYear().getYear())) {
                continue;
            }
            
            result.add(executionDegree);
        }
        
        // sort by degreeCurricularPlan.idInternal descending
        Collections.sort(result, new Comparator<ExecutionDegree>() { 

            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                Integer idInternal1 = o1.getDegreeCurricularPlan().getIdInternal();
                Integer idInternal2 = o2.getDegreeCurricularPlan().getIdInternal();
                
                return idInternal2.compareTo(idInternal1); 
            }
            
        });
        
        return result;
    }

    public static List<ExecutionDegree> getAllByExecutionCourseAndTeacher(ExecutionCourse executionCourse, Teacher teacher) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        
        for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
            boolean matchExecutionCourse = false;
            for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCourses()) {
                if (curricularCourse.getAssociatedExecutionCourses().contains(executionCourse)) {
                    matchExecutionCourse = true;
                    break;
                }
            }
            
            if (! matchExecutionCourse) {
                continue;
            }
            
            // if teacher is not a coordinator of the executionDegree
            if (executionDegree.getCoordinatorByTeacher(teacher) == null) {
                continue;
            }
            
            result.add(executionDegree);
        }
        
        return result;
    }

    public static List<ExecutionDegree> getAllCoordinatedByTeacher(Teacher teacher) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        
        if (teacher == null) {
            return result;
        }
        
        for (Coordinator coordinator : teacher.getCoordinators()) {
            result.add(coordinator.getExecutionDegree());
        }
        
        Comparator<ExecutionDegree> degreNameComparator = new Comparator<ExecutionDegree>() {

            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                String name1 = o1.getDegreeCurricularPlan().getDegree().getName();
                String name2 = o2.getDegreeCurricularPlan().getDegree().getName();
                
                return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
            }
            
        };
        
        Comparator<ExecutionDegree> yearComparator = new Comparator<ExecutionDegree>() {

            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                String year1 = o1.getExecutionYear().getYear();
                String year2 = o2.getExecutionYear().getYear();
                
                return String.CASE_INSENSITIVE_ORDER.compare(year1, year2);
            }
            
        };
        
        // sort by degreeCurricularPlan.degree.nome ascending, executionYear.year descending
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(degreNameComparator, false);
        comparatorChain.addComparator(yearComparator, true);
     
        Collections.sort(result, comparatorChain);
        
        return result;
    }

    public static List<ExecutionDegree> getAllByExecutionYearAndDegreeType(String year, DegreeType typeOfCourse) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        
        if (year == null) {
            return result;
        }
        
        if (typeOfCourse == null) {
            return result;
        }
        
        for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
            if (! year.equalsIgnoreCase(executionDegree.getExecutionYear().getYear())) {
                continue;
            }
            
            if (! typeOfCourse.equals(executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso())) {
                continue;
            }
            
            result.add(executionDegree);
        }
        
        // sorty by degreeCurricularPlan.idInternal descending
        Collections.sort(result, new Comparator<ExecutionDegree>() {

            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                Integer idInternal1 = o1.getDegreeCurricularPlan().getIdInternal();
                Integer idInternal2 = o2.getDegreeCurricularPlan().getIdInternal();
                
                return idInternal2.compareTo(idInternal1);
            }
            
        });
        
        return result;
    }

    public static List<ExecutionDegree> getAllByDegreeAndExecutionYear(Degree degree, String year, CurricularStage stage) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        
        if (degree == null) {
            return result;
        }
        
        if (stage == null) {
            return result;
        }
        
        if (year == null) {
            return result;
        }
        
        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
        
        if (executionYear == null) {
            return result;
        }
        
        for (ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
            if (! stage.equals(executionDegree.getDegreeCurricularPlan().getCurricularStage())) {
                continue;
            }
            
            if (! degree.equals(executionDegree.getDegreeCurricularPlan().getDegree())) {
                continue;
            }
            
            result.add(executionDegree);
        }
        
        return result;
    }

    public static List<ExecutionDegree> getAllByDegreeAndCurricularStage(Degree degree, CurricularStage stage) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        
        if (degree == null) {
            return result;
        }
        
        if (stage == null) {
            return result;
        }
        
        for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
            if (! degree.equals(executionDegree.getDegreeCurricularPlan().getDegree())) {
                continue;
            }
            
            if (! stage.equals(executionDegree.getDegreeCurricularPlan().getCurricularStage())) {
                continue;
            }
            
            result.add(executionDegree);
        }
        
        return result;
    }

    public static ExecutionDegree getByDegreeCurricularPlanAndExecutionYear(DegreeCurricularPlan degreeCurricularPlan, String executionYear) {
        if (degreeCurricularPlan == null) {
            return null;
        }

        if (executionYear == null) {
            return null;
        }
        
        for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            if (executionYear.equalsIgnoreCase(executionDegree.getExecutionYear().getYear())) {
                return executionDegree;
            }
        }
        
        return null;
    }

    public static ExecutionDegree getByDegreeCurricularPlanNameAndExecutionYear(String degreeName, ExecutionYear executionYear) {
        if (degreeName == null) {
            return null;
        }
        
        if (executionYear == null) {
            return null;
        }
        
        for (ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
            if (degreeName.equalsIgnoreCase(executionDegree.getDegreeCurricularPlan().getName())) {
                return executionDegree;
            }
        }
        
        return null;
    }
	
}
