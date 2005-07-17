package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadDegree implements IService {

    /**
     * Executes the service. Returns the current infodegree.
     * @throws ExcepcaoPersistencia 
     */
    public InfoDegree run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IDegree degree = (IDegree)sp.getICursoPersistente().readByOID(Degree.class,idInternal);

		if (degree == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos.newInfoFromDomain(degree);
    }
}