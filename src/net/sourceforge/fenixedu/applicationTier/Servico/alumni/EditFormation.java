package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
	dbFormation.setFormationType(formation.getFormationType());
	dbFormation.setDegree(formation.getFormationDegree().getName());
	dbFormation.setEducationArea(formation.getEducationArea());
	dbFormation.setBeginYear(formation.getFormationBeginYear());
	dbFormation.setYear(formation.getFormationEndYear());
	dbFormation.setEctsCredits(formation.getFormationCredits());
	dbFormation.setFormationHours(formation.getFormationHours());
	dbFormation.setInstitution(getFormationInstitution(formation));
    }

}
