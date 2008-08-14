/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.professorship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Professorship;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ProfessorshipDTO {

    public ProfessorshipDTO(Professorship professorship) {
	setProfessorship(professorship);
    }

    Professorship professorship;

    public Professorship getProfessorship() {
	return professorship;
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = professorship;
    }

    public List<String> getDegreeSiglas() {
	List<String> degreeSiglas = new ArrayList();
	for (Iterator iter = getProfessorship().getExecutionCourse().getAssociatedCurricularCourses().iterator(); iter.hasNext();) {
	    CurricularCourse curricularCourse = (CurricularCourse) iter.next();
	    String degreeSigla = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
	    if (!degreeSiglas.contains(degreeSigla)) {
		degreeSiglas.add(degreeSigla);
	    }
	}
	return degreeSiglas;
    }
}
