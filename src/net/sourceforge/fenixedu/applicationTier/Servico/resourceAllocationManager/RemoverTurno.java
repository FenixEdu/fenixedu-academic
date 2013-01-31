package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemoverTurno extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static Object run(final InfoShift infoShift, final InfoClass infoClass) {
		final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());
		if (shift == null) {
			return Boolean.FALSE;
		}
		final SchoolClass schoolClass = (SchoolClass) CollectionUtils.find(shift.getAssociatedClasses(), new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				final SchoolClass schoolClass = (SchoolClass) arg0;
				return schoolClass.getIdInternal().equals(infoClass.getIdInternal());
			}
		});
		if (schoolClass == null) {
			return Boolean.FALSE;
		}
		shift.getAssociatedClasses().remove(schoolClass);
		return Boolean.TRUE;
	}

}