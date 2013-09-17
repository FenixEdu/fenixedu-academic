package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.Formation;
import pt.ist.fenixframework.Atomic;

public class EditFormation extends FormationManagement {

    protected void run(final AlumniFormation formation) {
        editAlumniFormation(formation);
    }

    protected void run(final List<AlumniFormation> formationList) {

        for (AlumniFormation formation : formationList) {
            editAlumniFormation(formation);
        }
    }

    private void editAlumniFormation(final AlumniFormation formation) {

        Formation dbFormation = formation.getAssociatedFormation();
        dbFormation.edit(formation, getFormationInstitution(formation));
    }

    // Service Invokers migrated from Berserk

    private static final EditFormation serviceInstance = new EditFormation();

    @Atomic
    public static void runEditFormation(AlumniFormation formation) {
        serviceInstance.run(formation);
    }

}