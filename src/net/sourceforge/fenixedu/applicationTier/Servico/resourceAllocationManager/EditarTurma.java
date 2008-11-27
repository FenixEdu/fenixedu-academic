package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;

public class EditarTurma extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Object run(final Integer idInternal, final String className, final Integer curricularYear,
	    final InfoExecutionDegree infoExecutionDegree, final InfoExecutionPeriod infoExecutionPeriod)
	    throws ExistingServiceException {

	final SchoolClass classToEdit = rootDomainObject.readSchoolClassByOID(idInternal);
	classToEdit.edit(className);
	return InfoClass.newInfoFromDomain(classToEdit);
    }

}