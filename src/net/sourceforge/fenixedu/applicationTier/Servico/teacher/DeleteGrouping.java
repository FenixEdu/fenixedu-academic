/*
 * Created on 2/Abr/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.ShiftGroupingProperties;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteGrouping extends FenixService {

    public Boolean run(Integer executionCourseId, Integer groupPropertiesId) throws FenixServiceException {

	if (groupPropertiesId == null) {
	    return Boolean.FALSE;
	}

	Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesId);

	if (groupProperties == null) {
	    throw new ExistingServiceException();
	}
	if (groupProperties.getDifferentiatedCapacity()) {
	    List<ShiftGroupingProperties> shiftGroupingProperties = groupProperties.getShiftGroupingProperties();
	    for (ShiftGroupingProperties shiftGP : shiftGroupingProperties)
		shiftGP.delete();
	}
	groupProperties.delete();

	return Boolean.TRUE;
    }

}
