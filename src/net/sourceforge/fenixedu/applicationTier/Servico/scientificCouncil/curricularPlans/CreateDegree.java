package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class CreateDegree implements IService {

    public void run(String name, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType,
            Double ectsCredits, GradeScale gradeScale) throws ExcepcaoPersistencia, FenixServiceException {

        if (name == null || nameEn == null || acronym == null || bolonhaDegreeType == null
                || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final List<Degree> degrees = (List<Degree>) persistentSupport.getICursoPersistente()
                .readAllFromNewDegreeStructure();

        for (Degree degree : degrees) {
            if (degree.getAcronym().equalsIgnoreCase(acronym)) {
                throw new FenixServiceException("error.existing.degree.acronym");
            }
            if ((degree.getNome().equalsIgnoreCase(name) || degree.getNameEn().equalsIgnoreCase(nameEn))
                    && degree.getBolonhaDegreeType().equals(bolonhaDegreeType)) {
                throw new FenixServiceException("error.existing.degree.name.and.type");
            }
        }

        new Degree(name, nameEn, acronym, bolonhaDegreeType, ectsCredits, gradeScale);
    }

}
