package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadExternalPersonsByInstitution {

    @Atomic
    public static List run(String institutionID) throws FenixServiceException {
        List infoExternalPersons = new ArrayList();

        Unit institution = (Unit) FenixFramework.getDomainObject(institutionID);
        Collection<ExternalContract> externalPersons = institution.getExternalPersons();

        for (ExternalContract externalPerson : externalPersons) {
            infoExternalPersons.add(InfoExternalPerson.newInfoFromDomain(externalPerson));
        }

        return infoExternalPersons;
    }
}