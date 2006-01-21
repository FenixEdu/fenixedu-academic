package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID extends Service {

    public InfoDegree run(Integer degreeId) throws ExcepcaoPersistencia {
        Degree degree = (Degree) persistentObject.readByOID(Degree.class, degreeId);
        return InfoDegree.newInfoFromDomain(degree);
    }

}