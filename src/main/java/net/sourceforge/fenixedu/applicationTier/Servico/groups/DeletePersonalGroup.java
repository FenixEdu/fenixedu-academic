package net.sourceforge.fenixedu.applicationTier.Servico.groups;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import pt.ist.fenixWebFramework.services.Service;

public class DeletePersonalGroup extends FenixService {

    @Service
    public static void run(Integer groupId) throws FenixServiceException {
        PersonalGroup personalGroup = rootDomainObject.readPersonalGroupByOID(groupId);
        if (personalGroup == null) {
            throw new FenixServiceException("error.noPersonalGroup");
        }

        personalGroup.delete();
    }

}