package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExercise extends Service {
    public Metadata run(Integer executionCourseId, Integer metadataId) throws FenixServiceException,
            ExcepcaoPersistencia {
        return rootDomainObject.readMetadataByOID(metadataId);
    }

}

