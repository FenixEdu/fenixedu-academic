/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.professorship;

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

}
