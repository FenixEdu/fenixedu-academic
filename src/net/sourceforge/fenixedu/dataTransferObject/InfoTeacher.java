/*
 * Created on 12/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author João Mota
 */
public class InfoTeacher extends InfoObject implements ISmsDTO {

	private final Teacher teacher;

    public InfoTeacher(final Teacher teacher) {
    	this.teacher = teacher;
    }

    public List getProfessorShipsExecutionCourses() {
    	final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>();
    	for (final Professorship professorship : teacher.getProfessorshipsSet()) {
    		infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
    	}
        return infoProfessorships;
    }

    public List getResponsibleForExecutionCourses() {
    	final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>();
    	for (final Professorship professorship : teacher.getProfessorshipsSet()) {
    		if (professorship.isResponsibleFor()) {
    			infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
    		}
    	}
        return infoProfessorships;
    }

    public Integer getTeacherNumber() {
        return teacher.getTeacherNumber();
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(teacher.getPerson());
    }

    public InfoCategory getInfoCategory() {
        return InfoCategory.newInfoFromDomain(teacher.getCategory());
    }

    public boolean equals(Object obj) {
    	return obj != null && teacher == ((InfoTeacher) obj).teacher;
    }

    public String toString() {
    	return teacher.toString();
    }

    public static InfoTeacher newInfoFromDomain(final Teacher teacher) {
    	return teacher == null ? null : new InfoTeacher(teacher);
    }

    public String toSmsText() {
        return "Nome: " + teacher.getPerson().getNome() + " Numero: " + getTeacherNumber();
    }

	@Override
	public Integer getIdInternal() {
		return teacher.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}