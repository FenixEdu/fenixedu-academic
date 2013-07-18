package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditarTurma {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Object run(final Integer idInternal, final String className) throws ExistingServiceException {

        final SchoolClass classToEdit = RootDomainObject.getInstance().readSchoolClassByOID(idInternal);
        classToEdit.edit(className);
        return InfoClass.newInfoFromDomain(classToEdit);
    }

}