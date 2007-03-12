package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class SpecialSeasonStudentCurriculumGroupBean extends StudentCurriculumGroupBean {
    
    public SpecialSeasonStudentCurriculumGroupBean(final CurriculumGroup curriculumGroup, final ExecutionPeriod executionPeriod) {
	super(curriculumGroup, executionPeriod, null);
    }

    @Override
    protected List<IDegreeModuleToEvaluate> buildCourseGroupsToEnrol(CurriculumGroup group, ExecutionPeriod executionPeriod) {
        return Collections.emptyList();
    }
    
    @Override
    protected List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	List<StudentCurriculumEnrolmentBean> result = new ArrayList<StudentCurriculumEnrolmentBean>();
	for (CurriculumModule curriculumModule : group.getCurriculumModules()) {
	    if(curriculumModule.isEnrolment()) {
		Enrolment enrolment = (Enrolment) curriculumModule;
		if(enrolment.isSpecialSeasonEnroled(executionPeriod.getExecutionYear())) {
		    result.add(new StudentCurriculumEnrolmentBean(enrolment));
		}
	    }
	}
	
        return result;
    }
    
    @Override
    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group, ExecutionPeriod executionPeriod) {
	Map<CurricularCourse, Enrolment> enrolmentsMap = new HashMap<CurricularCourse, Enrolment>();
	for (CurriculumModule curriculumModule : group.getCurriculumModules()) {
	    if(curriculumModule.isEnrolment()) {
		Enrolment enrolment = (Enrolment) curriculumModule;
		if(enrolment.canBeSpecialSeasonEnroled(executionPeriod.getExecutionYear())) {
		    if(enrolmentsMap.get(enrolment.getCurricularCourse()) != null) {
			Enrolment enrolmentMap = enrolmentsMap.get(enrolment.getCurricularCourse());
			if (enrolment.getExecutionPeriod().compareTo(enrolmentMap.getExecutionPeriod()) > 0) {
			    enrolmentsMap.put(enrolment.getCurricularCourse(), enrolment);
			}
		    } else {
			enrolmentsMap.put(enrolment.getCurricularCourse(), enrolment);
		    }
		}
	    }
	}
	
	List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
	for (Enrolment enrolment : enrolmentsMap.values()) {
	    result.add(new CurriculumModuleEnroledWrapper(enrolment, enrolment.getExecutionPeriod()));
	}
	
	return result;
    }
    
    @Override
    protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup, ExecutionPeriod executionPeriod, int[] curricularYears) {
	final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
	for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroups()) {
	    result.add(new SpecialSeasonStudentCurriculumGroupBean(curriculumGroup, executionPeriod));
	}

	return result;
    }
    
    @Override
    public List<IDegreeModuleToEvaluate> getSortedDegreeModulesToEvaluate() {
	final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(
		getCurricularCoursesToEnrol());
	Collections.sort(result, new BeanComparator("executionPeriod"));

	return result;

    }
    
    @Override
    public boolean isToBeDisabled() {
        return true;
    }

}
