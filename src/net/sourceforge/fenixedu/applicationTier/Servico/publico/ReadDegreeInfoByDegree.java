package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadDegreeInfoByDegree extends Service {

    public InfoDegreeInfo run(final Integer degreeId) throws FenixServiceException, ExcepcaoPersistencia {
        if (degreeId == null) {
            throw new FenixServiceException("error.impossibleDegreeSite");
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();

        final Degree degree = (Degree) persistentDegree.readByOID(Degree.class, degreeId);
        if (degree == null || degree.getDegreeInfos().isEmpty()) {
            throw new FenixServiceException("error.impossibleDegreeSite");
        }

        final DegreeInfo latestDegreeInfo = (DegreeInfo) Collections.max(degree.getDegreeInfos(), new BeanComparator("lastModificationDate"));
        return InfoDegreeInfo.newInfoFromDomain(latestDegreeInfo);
    }

}