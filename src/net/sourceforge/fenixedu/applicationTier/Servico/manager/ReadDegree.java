package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */

public class ReadDegree extends Service {

    /**
     * Executes the service. Returns the current infodegree.
     * @throws ExcepcaoPersistencia 
     */
    public InfoDegree run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Degree degree = (Degree)sp.getICursoPersistente().readByOID(Degree.class,idInternal);

		if (degree == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos.newInfoFromDomain(degree);
    }
}