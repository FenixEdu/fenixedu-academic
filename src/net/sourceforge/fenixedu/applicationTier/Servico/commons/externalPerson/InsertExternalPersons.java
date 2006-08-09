package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertExternalPersons extends Service {

    public List<ExternalPerson> run(List<InfoExternalPerson> infoExternalPersons)
            throws ExcepcaoPersistencia, FenixServiceException {

        List<ExternalPerson> externalPersons = new ArrayList<ExternalPerson>();

        List<Unit> institutions = Unit.readAllUnits();

        for (InfoExternalPerson infoExternalPerson : infoExternalPersons) {

            // retrieving existing work location
            Unit currentInstitution = UnitUtils.readExternalInstitutionUnitByName(infoExternalPerson
                    .getInfoInstitution().getName());

            // creating a new one if it doesn't already exist
            if (currentInstitution == null) {
                InsertInstitution iwl = new InsertInstitution();
                currentInstitution = iwl.run(infoExternalPerson.getInfoInstitution().getName());
                institutions.add(currentInstitution);
            }

            String name = infoExternalPerson.getInfoPerson().getNome();

            // creating a new ExternalPerson
            ExternalPerson externalPerson = new ExternalPerson(name, Gender.MALE, null, null, null,
                    null, null, null, null, null, null, null, null, String.valueOf(System
                            .currentTimeMillis()), currentInstitution);

            externalPersons.add(externalPerson);
        }

        return externalPersons;
    }

}
