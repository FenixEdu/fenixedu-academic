package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID implements IService {

    public InfoDegree run(Integer degreeId) throws ExcepcaoPersistencia {
        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICursoPersistente persistentDegree = suportePersistente.getICursoPersistente();

        IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeId);

        InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
        return infoDegree;
    }
}