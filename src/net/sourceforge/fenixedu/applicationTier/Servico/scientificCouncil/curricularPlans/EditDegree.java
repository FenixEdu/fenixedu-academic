package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.curriculum.GradeType;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditDegree implements IService {

    public void run(Integer idInternal, String name, String nameEn, String acronym,
            BolonhaDegreeType bolonhaDegreeType, GradeType gradeType) throws FenixServiceException,
            ExcepcaoPersistencia {
        if (name == null || nameEn == null || acronym == null || bolonhaDegreeType == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IDegree degreeToEdit = (IDegree) persistentSupport.getIPersistentObject().readByOID(
                Degree.class, idInternal);

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        } else if (!degreeToEdit.getAcronym().equalsIgnoreCase(acronym)
                || !degreeToEdit.getNome().equalsIgnoreCase(name)
                || !degreeToEdit.getBolonhaDegreeType().equals(bolonhaDegreeType)) {

            final List<IDegree> degrees = (List<IDegree>) persistentSupport.getICursoPersistente()
                    .readAllFromNewDegreeStructure();

            // assert unique degree code and unique pair name/type
            for (IDegree degree : degrees) {
                if (degree.getAcronym().equalsIgnoreCase(acronym)) {
                    throw new FenixServiceException("error.existing.degree.acronym");
                }
                if ((degree.getNome().equalsIgnoreCase(name) || degree.getNameEn().equalsIgnoreCase(
                        nameEn))
                        && degree.getBolonhaDegreeType().equals(bolonhaDegreeType)) {
                    throw new FenixServiceException("error.existing.name.and.type");
                }
            }
        }

        degreeToEdit.edit(name, nameEn, acronym, bolonhaDegreeType, gradeType);
    }

}
