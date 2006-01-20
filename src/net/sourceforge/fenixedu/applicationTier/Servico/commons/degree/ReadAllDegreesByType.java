package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadAllDegreesByType extends Service {

    public List run(String degreeType) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentDegreeInfo degrees = persistentSupport.getIPersistentDegreeInfo();
        List degreesList = degrees.readDegreeByType(DegreeType.valueOf(degreeType));
        List infoDegreesList = (List) CollectionUtils.collect(degreesList, new Transformer() {

            public Object transform(Object input) {
                Degree degree = (Degree) input;
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                return infoDegree;
            }
        });

        return infoDegreesList;

    }
}