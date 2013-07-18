package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import pt.ist.fenixWebFramework.services.Service;

public class CreateFormation extends FormationManagement {

    protected void run(final Alumni alumni, final AlumniFormation formation) {
        createAlumniFormation(alumni, formation);
    }

    protected void run(final Alumni alumni, final ArrayList<AlumniFormation> formationList) {
        for (AlumniFormation formation : formationList) {
            createAlumniFormation(alumni, formation);
        }
    }

    private void createAlumniFormation(final Alumni alumni, final AlumniFormation formation) {

        new Formation(alumni.getStudent().getPerson(), formation.getFormationType(), formation.getFormationDegree(),
                formation.getEducationArea(), formation.getFormationBeginYear(), formation.getFormationEndYear(),
                formation.getFormationCredits(), formation.getFormationHours(), getFormationInstitution(formation),
                formation.getParentInstitution(), formation.getInstitutionType(), formation.getCountryUnit());
    }

    // Service Invokers migrated from Berserk

    private static final CreateFormation serviceInstance = new CreateFormation();

    @Service
    public static void runCreateFormation(Alumni alumni, AlumniFormation formation) {
        serviceInstance.run(alumni, formation);
    }

}