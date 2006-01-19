package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

public class ReadAllDegreesByType implements IService {

    public List run(String degreeType) throws FenixServiceException, ExcepcaoPersistencia {
        List infoDegreesList;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDegreeInfo degrees = sp.getIPersistentDegreeInfo();
        List degreesList = degrees.readDegreeByType(DegreeType.valueOf(degreeType));
        infoDegreesList = (List) CollectionUtils.collect(degreesList, new Transformer() {

            public Object transform(Object input) {
                Degree degree = (Degree) input;
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                return infoDegree;
            }
        });

        return infoDegreesList;

    }
}