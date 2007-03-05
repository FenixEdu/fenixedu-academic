/*
 * Created on 12/Mai/2005 - 10:36:31
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Professorship;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers
		extends InfoFrequenta {
	
	List<InfoTeacher> teachers;
	List<InfoNonAffiliatedTeacher> nonAffiliatedTeachers;
	
	/**
	 * @return Returns the nonAffiliatedTeachers.
	 */
	public List<InfoNonAffiliatedTeacher> getNonAffiliatedTeachers() {
		return nonAffiliatedTeachers;
	}
	
	/**
	 * @param nonAffiliatedTeachers The nonAffiliatedTeachers to set.
	 */
	public void setNonAffiliatedTeachers(
			List<InfoNonAffiliatedTeacher> nonAffiliatedTeachers) {
		this.nonAffiliatedTeachers = nonAffiliatedTeachers;
	}
	
	/**
	 * @return Returns the teachers.
	 */
	public List<InfoTeacher> getTeachers() {
		return teachers;
	}
	
	/**
	 * @param teachers The teachers to set.
	 */
	public void setTeachers(List<InfoTeacher> teachers) {
		this.teachers = teachers;
	}
	
    public void copyFromDomain(Attends attends) {
        super.copyFromDomain(attends);
		
		this.teachers = new ArrayList<InfoTeacher>();
		List<Professorship> professorships = attends.getExecutionCourse().getProfessorships();
		for(Professorship professorship : professorships) {
			this.teachers.add(InfoTeacher.newInfoFromDomain(professorship.getTeacher()));
		}
		
		this.nonAffiliatedTeachers = new ArrayList<InfoNonAffiliatedTeacher>();
		List<NonAffiliatedTeacher> nonAffiliatedTeachers = attends.getExecutionCourse().getNonAffiliatedTeachers();
		for(NonAffiliatedTeacher naTeacher : nonAffiliatedTeachers) {
			this.nonAffiliatedTeachers.add(InfoNonAffiliatedTeacher.newInfoFromDomain(naTeacher));
		}
    }

    public static InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers newInfoFromDomain(Attends attends) {
		InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers infoAttends = null;
        if (attends != null) {
			infoAttends = new InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers();
			infoAttends.copyFromDomain(attends);
        }
        return infoAttends;
    }

	


}
