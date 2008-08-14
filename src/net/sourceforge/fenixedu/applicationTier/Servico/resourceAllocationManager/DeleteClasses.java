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
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteClasses extends Service {

    public Boolean run(List<Integer> classOIDs) {
	for (Integer classId : classOIDs) {
	    final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(classId);
	    schoolClass.delete();
	}

	return Boolean.TRUE;
    }

}
