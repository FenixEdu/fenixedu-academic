package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID {

    @Service
    public static InfoDegree run(String degreeId) {
        Degree degree = FenixFramework.getDomainObject(degreeId);
        return InfoDegree.newInfoFromDomain(degree);
    }

}