package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateDegree implements IService {

    public void run(String name, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType,
            GradeScale gradeScale) throws ExcepcaoPersistencia, FenixServiceException {

        if (name == null || nameEn == null || acronym == null || bolonhaDegreeType == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final List<IDegree> degrees = (List<IDegree>) persistentSupport.getICursoPersistente()
                .readAllFromNewDegreeStructure();

        for (IDegree degree : degrees) {
            if (degree.getAcronym().equalsIgnoreCase(acronym)) {
                throw new FenixServiceException("error.existing.degree.acronym");
            }
            if ((degree.getNome().equalsIgnoreCase(name) || degree.getNameEn().equalsIgnoreCase(nameEn))
                    && degree.getBolonhaDegreeType().equals(bolonhaDegreeType)) {
                throw new FenixServiceException("error.existing.degree.name.and.type");
            }
        }

        DomainFactory.makeDegree(name, nameEn, acronym, bolonhaDegreeType, gradeScale);
    }

}
