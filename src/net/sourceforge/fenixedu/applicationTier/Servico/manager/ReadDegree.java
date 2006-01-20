package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class ReadDegree extends Service {

    /**
     * Executes the service. Returns the current infodegree.
     * @throws ExcepcaoPersistencia 
     */
    public InfoDegree run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
		Degree degree = (Degree) persistentSupport.getICursoPersistente().readByOID(Degree.class,idInternal);

		if (degree == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos.newInfoFromDomain(degree);
    }
}