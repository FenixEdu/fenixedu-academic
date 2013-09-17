package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID {

    @Atomic
    public static InfoDegree run(String degreeId) {
        Degree degree = FenixFramework.getDomainObject(degreeId);
        return InfoDegree.newInfoFromDomain(degree);
    }

}