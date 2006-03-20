package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadDegrees extends Service {

    public List run() throws ExcepcaoPersistencia {
    	return run(null);
    }

    public List run(final DegreeType degreeType) throws ExcepcaoPersistencia {
    	final List<Degree> degrees = Degree.readAllFromOldDegreeStructure();
        final List<InfoDegree> infoDegrees = new ArrayList<InfoDegree>(degrees.size());
        for (final Degree degree : degrees) {
        	if (degreeType == null || degreeType == degree.getTipoCurso()) {
        		infoDegrees.add(InfoDegree.newInfoFromDomain(degree));
        	}
        }
        return infoDegrees;
    }

}