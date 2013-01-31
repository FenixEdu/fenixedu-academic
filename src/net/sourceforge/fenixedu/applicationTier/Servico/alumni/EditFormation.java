package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.Formation;

public class EditFormation extends FormationManagement {

	public void run(final AlumniFormation formation) {
		editAlumniFormation(formation);
	}

	public void run(final List<AlumniFormation> formationList) {

		for (AlumniFormation formation : formationList) {
			editAlumniFormation(formation);
		}
	}

	private void editAlumniFormation(final AlumniFormation formation) {

		Formation dbFormation = formation.getAssociatedFormation();
		dbFormation.edit(formation, getFormationInstitution(formation));
	}

}
