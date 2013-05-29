package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID {

    @Service
    public static InfoDegree run(String degreeId) {
        Degree degree = AbstractDomainObject.fromExternalId(degreeId);
        return InfoDegree.newInfoFromDomain(degree);
    }

}