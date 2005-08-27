/*
 * Created on Jan 17, 2005
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author nmgo
 */
public class SpecificLEICEnrollmentRule extends SpecificEnrolmentRule implements IEnrollmentRule {

	protected Integer optionalCourses = 0;
	
	protected boolean isSpecAreaDone = false;
	
	protected boolean isSecAreaDone = false;
	
	protected List optionalCoursesList = new ArrayList();
	
	protected List specializationAndSecundaryAreaCurricularCoursesToCountForCredits = null;
	
    public SpecificLEICEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }


    protected Integer getOptionalCourses() {
		return optionalCourses;
	}


	protected void setOptionalCourses(Integer optionalCourses) {
		this.optionalCourses = optionalCourses;
	}


	protected List specificAlgorithm(IStudentCurricularPlan studentCurricularPlan) {

        HashMap creditsInScientificAreas = new HashMap();
        HashMap creditsInSpecializationAreaGroups = new HashMap();
        HashMap creditsInSecundaryAreaGroups = new HashMap();
        int creditsInAnySecundaryArea = 0;

        Collection allCurricularCourses = getSpecializationAndSecundaryAreaCurricularCourses(studentCurricularPlan);

        specializationAndSecundaryAreaCurricularCoursesToCountForCredits = getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(allCurricularCourses);

        calculateGroupsCreditsFromEnrollments(studentCurricularPlan,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups);

        /*this.creditsInSecundaryArea = calculateCredits(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(), creditsInSecundaryAreaGroups);
        this.creditsInSpecializationArea = calculateCredits(studentCurricularPlan.getBranch().getSpecializationCredits(),
                creditsInSpecializationAreaGroups);*/

        return selectCurricularCourses(studentCurricularPlan, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
    }

    /**
     * @param maxCredits
     * @param studentCurricularPlan2
     * @param creditsInSecundaryAreaGroups
     * @return
     */
    protected Integer calculateCredits(Integer maxCredits, HashMap creditsAreaGroups) {
        
        int credits = 0;
        for(Iterator iter = creditsAreaGroups.values().iterator(); iter.hasNext(); ) {
            Integer value = (Integer) iter.next();
            credits += value.intValue();
        }
        
        if (credits >= maxCredits.intValue()) {
            credits = maxCredits.intValue();
        }
        
        return new Integer(credits);
    }

    
    protected List filter(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod,
            List curricularCoursesToBeEnrolledIn,
            final List selectedCurricularCoursesFromSpecializationAndSecundaryAreas) {

        final List curricularCoursesFromCommonAreas = getCommonAreasCurricularCourses(studentCurricularPlan);

        List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCoursesFromCommonAreas.contains(curricularCourse2Enroll
                        .getCurricularCourse());
            }
        });

        List curricularCoursesFromOtherAreasToMantain = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn,
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                        return selectedCurricularCoursesFromSpecializationAndSecundaryAreas.contains(curricularCourse2Enroll
                                .getCurricularCourse())
                                || SpecificLEICEnrollmentRule.this.studentCurricularPlan.getDegreeCurricularPlan().getTFCs()
                                        .contains(curricularCourse2Enroll.getCurricularCourse());
                    }
                });

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan,
                executionPeriod, specializationAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    specializationAreaCurricularCourses);
        }

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan, executionPeriod,
                secundaryAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    secundaryAreaCurricularCourses);
        }

        result.addAll(curricularCoursesFromOtherAreasToMantain);
        //FIXME
        if(result.isEmpty() || allTemporary(result)){
        	result.addAll(getOptionalCourses(result));
        }
        else {
        	IEnrollmentRule enrollmentRule = new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan, executionPeriod);
        	try {
				result = enrollmentRule.apply(result);
			} catch (EnrolmentRuleDomainException e) {
			}
        }
        return result;
    }


	protected void calculateGroupsCreditsFromEnrollments(IStudentCurricularPlan studentCurricularPlan,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
            HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups) {
		
		Integer specCredits = 0;
		Integer secCredits = 0;
        
        List specCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);
        
        optionalCoursesList.addAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

        int size = specializationAndSecundaryAreaCurricularCoursesToCountForCredits.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) specializationAndSecundaryAreaCurricularCoursesToCountForCredits
                    .get(i);
            
            Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

            if(specCurricularCourses.contains(curricularCourse)) {
            	if(!isSpecAreaDone) {
            		specCredits = specCredits + curricularCourseCredits;
            		optionalCoursesList.remove(curricularCourse);
            		if(isAreaDone(studentCurricularPlan.getBranch().getSpecializationCredits(), specCredits)) {
            			isSpecAreaDone = true;
            		}
            	}
                //sumInHashMap(creditsInSpecializationAreaGroups, studentCurricularPlan.getBranch().getIdInternal(), curricularCourseCredits);
            } else if(secCurricularCourses.contains(curricularCourse)) {
            	if(!isSecAreaDone) {
            		secCredits = secCredits + curricularCourseCredits;
            		optionalCoursesList.remove(curricularCourse);
            		if(isAreaDone(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(), secCredits)) {
            			isSecAreaDone = true;
            		}
            	}
                //sumInHashMap(creditsInSecundaryAreaGroups, studentCurricularPlan.getSecundaryBranch().getIdInternal(), curricularCourseCredits);
            }            
        }
        
        this.creditsInSecundaryArea = secCredits;
        this.creditsInSpecializationArea = specCredits;
    }
    
    protected List selectCurricularCourses(IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits) {

        /*boolean isSpecializationAreaDone = isAreaDone(studentCurricularPlan.getBranch().getSpecializationCredits(),
                creditsInSpecializationArea);
        boolean isSecundaryAreaDone = isAreaDone(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(),
                creditsInSecundaryArea);*/

        Set finalListOfCurricularCourses = new HashSet();

        if (!isSpecAreaDone) {
            finalListOfCurricularCourses.addAll(getSpecializationAreaCurricularCourses(studentCurricularPlan));
        }

        if (!isSecAreaDone) {
            finalListOfCurricularCourses.addAll(getSecundaryAreaCurricularCourses(studentCurricularPlan));
        }

        finalListOfCurricularCourses
                .removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

        List result = new ArrayList();
        result.addAll(finalListOfCurricularCourses);
        return result;
    }

    
    protected boolean isAreaDone(Integer maxCredits, Integer credits) {
        if(credits.intValue() >= maxCredits.intValue())
            return true;
        return false;
    }
    
    
    
    //FIXME
    protected boolean allTemporary(List<CurricularCourse2Enroll> result) {
    	for (CurricularCourse2Enroll enroll : result) {
			if(!enroll.getEnrollmentType().equals(CurricularCourseEnrollmentType.TEMPORARY)){
				return false;
			}
		}
		return true;
	}
    
	protected List<CurricularCourse2Enroll> getOptionalCourses(List<CurricularCourse2Enroll> result) {

		optionalCourses = optionalCoursesList.size();
		List<ICurricularCourse> curricularCourses = getOptionalCurricularCourses();
		List<CurricularCourse2Enroll> curricularCoursesToEnroll = new ArrayList<CurricularCourse2Enroll>();
		
		if(optionalCourses.intValue() < 2) {
			if(result.isEmpty()) {
				curricularCoursesToEnroll.addAll(transformToCurricularCoursesToEnroll(curricularCourses, false));
			}
			else {
				curricularCoursesToEnroll.addAll(transformToCurricularCoursesToEnroll(curricularCourses, true));
			}
			
			curricularCoursesToEnroll = studentCurricularPlan.initAcumulatedEnrollments(curricularCoursesToEnroll);
		}
		return curricularCoursesToEnroll;
	}


	protected List<ICurricularCourse> getOptionalCurricularCourses() {
		List<ICurricularCourse> result = new ArrayList<ICurricularCourse>();
		List<ICurricularCourse> tagusCourses = getTagus4And5Courses();
		List<ICurricularCourse> leicCourses = getLEIC4And5Courses();
		result.addAll(tagusCourses);
		result.addAll(leicCourses);
		return result;
	}


	protected List<CurricularCourse2Enroll> transformToCurricularCoursesToEnroll(List<ICurricularCourse> curricularCourses, boolean temporary) {
		List<CurricularCourse2Enroll> result = new ArrayList<CurricularCourse2Enroll>();
		for (ICurricularCourse curricularCourse : curricularCourses) {
			CurricularCourse2Enroll course2Enroll = new CurricularCourse2Enroll();
			course2Enroll.setCurricularCourse(curricularCourse);
			course2Enroll.setOptionalCurricularCourse(Boolean.TRUE);
			if(temporary) {
				course2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
			}
			else {
				course2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.DEFINITIVE);
			}
			result.add(course2Enroll);
		}
		
		return result;
	}


	protected List<ICurricularCourse> getLEIC4And5Courses() {
		Set<ICurricularCourse> allCurricularCourses = new HashSet<ICurricularCourse>();
		
		Set<ICurricularCourse> areaCourses = new HashSet<ICurricularCourse>();
		List<IBranch> specAreas = studentCurricularPlan.getDegreeCurricularPlan().getSpecializationAreas();
		List<IBranch> secAreas = studentCurricularPlan.getDegreeCurricularPlan().getSecundaryAreas();
		
		for (IBranch branch : specAreas) {
			List<ICurricularCourseGroup> curricularCourseGroups = branch.getCurricularCourseGroups();
			for (ICurricularCourseGroup group : curricularCourseGroups) {
				List<ICurricularCourse> curricularCourses = group.getCurricularCourses();
				allCurricularCourses.addAll(curricularCourses);
			}
		}
		
		for (IBranch branch : secAreas) {
			List<ICurricularCourseGroup> curricularCourseGroups = branch.getCurricularCourseGroups();
			for (ICurricularCourseGroup group : curricularCourseGroups) {
				List<ICurricularCourse> curricularCourses = group.getCurricularCourses();
				allCurricularCourses.addAll(curricularCourses);
			}
		}
		
		allCurricularCourses.removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

		
        List<ICurricularCourseGroup> optionalCurricularCourseGroups = studentCurricularPlan.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();
        for (ICurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
			allCurricularCourses.addAll(curricularCourseGroup.getCurricularCourses());
		}
        
		for (ICurricularCourse course : allCurricularCourses) {
			if(!studentCurricularPlan.isCurricularCourseApproved(course) && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(course, executionPeriod)) {
				if(isAnyScopeActive(course.getScopes())) {
					areaCourses.add(course);
				}
			}
			else {
				if(isAnyScopeActive(course.getScopes())) {
					optionalCourses++;
				}
			}
		}
		
		return new ArrayList<ICurricularCourse>(areaCourses);
	}


	protected boolean isAnyScopeActive(List<ICurricularCourseScope> scopes) {
		for (ICurricularCourseScope curricularCourseScope : scopes) {
			if (curricularCourseScope.isActive() && 
					curricularCourseScope.getCurricularSemester().getSemester().equals(executionPeriod.getSemester())) {
				return true;
			}
		}
		return false;
	}


	protected List<ICurricularCourse> getTagus4And5Courses() {
		try {
			IDegreeCurricularPlan tagusCurricularPlan = (IDegreeCurricularPlan) PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class, 89);
			Set<ICurricularCourse> areaCourses = new HashSet<ICurricularCourse>();
			Set<ICurricularCourse> allCurricularCourses = new HashSet<ICurricularCourse>();
			List<IBranch> specAreas = tagusCurricularPlan.getSpecializationAreas();
			List<IBranch> secAreas = tagusCurricularPlan.getSecundaryAreas();

			for (IBranch branch : specAreas) {
				List<ICurricularCourseGroup> curricularCourseGroups = branch.getCurricularCourseGroups();
				for (ICurricularCourseGroup group : curricularCourseGroups) {
					allCurricularCourses.addAll(group.getCurricularCourses());
				}
			}
			
			for (IBranch branch : secAreas) {
				List<ICurricularCourseGroup> curricularCourseGroups = branch.getCurricularCourseGroups();
				for (ICurricularCourseGroup group : curricularCourseGroups) {
					allCurricularCourses.addAll(group.getCurricularCourses());
				}
			}
			
	        List<ICurricularCourseGroup> optionalCurricularCourseGroups = tagusCurricularPlan.getAllOptionalCurricularCourseGroups();
	        for (ICurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
				allCurricularCourses.addAll(curricularCourseGroup.getCurricularCourses());
			}
	        
			for (ICurricularCourse course : allCurricularCourses) {
				if (!isApproved(course) && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(course, executionPeriod)) {
					if (!studentCurricularPlan.isCurricularCourseApproved(course) && isAnyScopeActive(course.getScopes())) {
						areaCourses.add(course);
					}
				} else {
					if(isAnyScopeActive(course.getScopes())) {
						optionalCourses++;
					}
				}
			}

			return new ArrayList<ICurricularCourse>(areaCourses);
		}catch(ExcepcaoPersistencia e) {
			throw new RuntimeException(e);
		}

	}


	protected boolean isAnyScopeActive(List<ICurricularCourseScope> scopes,
			List<Integer> years) {
		for (ICurricularCourseScope curricularCourseScope : scopes) {
			if (curricularCourseScope.isActive() && 
					years.contains(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear())&& 
					curricularCourseScope.getCurricularSemester().getSemester().equals(executionPeriod.getSemester())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isApproved(ICurricularCourse curricularCourse) {
		for(IEnrolment enrolment : studentCurricularPlan.getEnrolments()) {
			if(enrolment.getCurricularCourse().equals(curricularCourse) && enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
				return true;
			}
		}
		return false;
	}
}
