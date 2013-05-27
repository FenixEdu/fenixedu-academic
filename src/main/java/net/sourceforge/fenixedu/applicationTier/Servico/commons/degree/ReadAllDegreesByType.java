package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

public class ReadAllDegreesByType {

    @Service
    public static List<InfoDegree> run(String degreeType) throws FenixServiceException {
        return readDegreesByType(DegreeType.valueOf(degreeType));
    }

    @Service
    public static List<InfoDegree> run(DegreeType degreeType) throws FenixServiceException {
        return readDegreesByType(degreeType);
    }

    private static List<InfoDegree> readDegreesByType(DegreeType degreeType) {
        List<Degree> degreesList = Degree.readAllByDegreeType(degreeType);
        List<InfoDegree> infoDegreesList = (List<InfoDegree>) CollectionUtils.collect(degreesList, new Transformer() {

            @Override
            public Object transform(Object input) {
                Degree degree = (Degree) input;
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                return infoDegree;
            }
        });

        return infoDegreesList;
    }

}