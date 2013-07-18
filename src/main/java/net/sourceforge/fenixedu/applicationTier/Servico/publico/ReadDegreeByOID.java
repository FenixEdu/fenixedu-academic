package net.sourceforge.fenixedu.applicationTier.Servico.publico;


import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID {

    @Service
    public static InfoDegree run(Integer degreeId) {
        Degree degree = RootDomainObject.getInstance().readDegreeByOID(degreeId);
        return InfoDegree.newInfoFromDomain(degree);
    }

}