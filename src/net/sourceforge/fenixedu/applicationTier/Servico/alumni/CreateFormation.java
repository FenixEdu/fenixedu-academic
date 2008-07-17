package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;

public class CreateFormation extends FormationManagement {

    public void run(final Alumni alumni, final AlumniFormation formation) {
	createAlumniFormation(alumni, formation);
    }

    public void run(final Alumni alumni, final ArrayList<AlumniFormation> formationList) {
	for (AlumniFormation formation : formationList) {
	    createAlumniFormation(alumni, formation);
	}
    }

    private void createAlumniFormation(final Alumni alumni, final AlumniFormation formation) {

	Formation newFormation = new Formation(alumni.getStudent().getPerson(), formation.getFormationType(), formation
		.getFormationDegree(), formation.getEducationArea(), formation.getFormationBeginYear(), formation
		.getFormationEndYear(), formation.getFormationCredits(), formation.getFormationHours(),
		getFormationInstitution(formation));
    }

}
