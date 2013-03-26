package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPersonEditor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class InsertExternalPersons extends FenixService {

    @Service
    public static List<ExternalContract> run(final List<InfoExternalPersonEditor> infoExternalPersons)
            throws FenixServiceException {

        final List<ExternalContract> externalPersons = new ArrayList<ExternalContract>();
        for (final InfoExternalPersonEditor infoExternalPerson : infoExternalPersons) {
            // Retrieving existing work location
            Unit currentInstitution =
                    UnitUtils.readExternalInstitutionUnitByName(infoExternalPerson.getInfoInstitution().getName());
            if (currentInstitution == null) {
                currentInstitution = new InsertInstitution().run(infoExternalPerson.getInfoInstitution().getName());
            }

            final Person externalPerson =
                    Person.createExternalPerson(infoExternalPerson.getName(), Gender.MALE, null, null, null, null, null,
                            String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);

            externalPersons.add(new ExternalContract(externalPerson, currentInstitution, new YearMonthDay(), null));
        }
        return externalPersons;
    }

}