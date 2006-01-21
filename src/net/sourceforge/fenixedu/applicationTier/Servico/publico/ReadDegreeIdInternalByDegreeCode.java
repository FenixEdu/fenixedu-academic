/*
 * Created on 11/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;

/**
 * @author Pedro Santos e Rita Carvalho
 */
public class ReadDegreeIdInternalByDegreeCode extends Service {
	
	public Integer run(String degreeCode) throws ExcepcaoPersistencia {
        ICursoPersistente degreeDAO = persistentSupport.getICursoPersistente();

        Degree degree = degreeDAO.readBySigla(degreeCode);

        return degree != null ? degree.getIdInternal() : null;
	}

}
