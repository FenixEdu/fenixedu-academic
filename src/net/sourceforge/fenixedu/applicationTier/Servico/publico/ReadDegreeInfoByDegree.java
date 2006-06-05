package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadDegreeInfoByDegree extends Service {

    public InfoDegreeInfo run(final Integer degreeId) throws FenixServiceException, ExcepcaoPersistencia {
        if (degreeId == null) {
            throw new FenixServiceException("error.impossibleDegreeSite");
        }

        final Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        if (degree == null || degree.getDegreeInfos().isEmpty()) {
            throw new FenixServiceException("error.impossibleDegreeSite");
        }

        DegreeInfo mostRecentDegreeInfo = degree.getMostRecentDegreeInfo();
        if (mostRecentDegreeInfo == null) {
            throw new FenixServiceException();
        }
        
        return InfoDegreeInfo.newInfoFromDomain(mostRecentDegreeInfo);
    }

}
