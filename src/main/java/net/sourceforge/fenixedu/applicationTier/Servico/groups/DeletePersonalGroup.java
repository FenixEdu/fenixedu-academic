package net.sourceforge.fenixedu.applicationTier.Servico.groups;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class DeletePersonalGroup {

    @Service
    public static void run(Integer groupId) throws FenixServiceException {
        PersonalGroup personalGroup = RootDomainObject.getInstance().readPersonalGroupByOID(groupId);
        if (personalGroup == null) {
            throw new FenixServiceException("error.noPersonalGroup");
        }

        personalGroup.delete();
    }

}