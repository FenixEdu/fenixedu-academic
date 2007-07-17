/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
//
public abstract class Career extends Career_Base {

    public Career() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }
	
	public void delete() {
		removeTeacher();
		removeRootDomainObject();
        super.deleteDomainObject();
	}
		
    public static List<Career> readAllByTeacherIdAndCareerType(Teacher teacher, CareerType careerType){
        if(careerType == null){
            return teacher.getAssociatedCareers();
        }
        List<Career> allTeacherCareers = new ArrayList<Career>();
        
        if(careerType.equals(CareerType.PROFESSIONAL)) {
            readCareersByClass(teacher, allTeacherCareers, ProfessionalCareer.class.getName());        
        } else if(careerType.equals(CareerType.TEACHING)) {
            readCareersByClass(teacher, allTeacherCareers, TeachingCareer.class.getName());        
        }        
        return allTeacherCareers;        
    }

    private static void readCareersByClass(Teacher teacher, List<Career> allTeacherCareers, String className) {
        for (Career career : teacher.getAssociatedCareers()) {
            if(career.getClass().getName().equals(className)){
                allTeacherCareers.add(career);
            }
        }
    }
}
