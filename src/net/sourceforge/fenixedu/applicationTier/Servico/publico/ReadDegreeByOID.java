package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID extends FenixService {

	@Service
	public static InfoDegree run(Integer degreeId) {
		Degree degree = rootDomainObject.readDegreeByOID(degreeId);
		return InfoDegree.newInfoFromDomain(degree);
	}

}