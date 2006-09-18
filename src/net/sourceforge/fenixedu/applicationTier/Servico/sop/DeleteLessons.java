/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DeleteLessons extends Service {

    public void run(final List<Integer> lessonOIDs) throws FenixServiceException {
	final List<DomainException> exceptionList = new ArrayList<DomainException>();

	for (final Integer lessonOID : lessonOIDs) {
	    try {
		rootDomainObject.readLessonByOID(lessonOID).delete();
	    } catch (DomainException e) {
		exceptionList.add(e);
	    }
	}

	if (!exceptionList.isEmpty()) {
	    throw new FenixServiceMultipleException(exceptionList);
	}
    }

}
