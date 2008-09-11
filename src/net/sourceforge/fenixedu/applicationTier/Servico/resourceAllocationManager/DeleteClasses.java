/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

public class DeleteClasses extends Service {

    public Boolean run(List<Integer> classOIDs) {
	for (Integer classId : classOIDs) {
	    final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(classId);

	    // TODO: ----------------------------
	    for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
		shift.checkXpto();
	    }
	    // TODO: ----------------------------

	    schoolClass.delete();
	}

	return Boolean.TRUE;
    }

}
