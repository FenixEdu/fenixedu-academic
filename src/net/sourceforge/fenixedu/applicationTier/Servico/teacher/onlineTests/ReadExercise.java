package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;

public class ReadExercise extends Service {
    public Metadata run(Integer executionCourseId, Integer metadataId) throws FenixServiceException {
	return rootDomainObject.readMetadataByOID(metadataId);
    }

}
