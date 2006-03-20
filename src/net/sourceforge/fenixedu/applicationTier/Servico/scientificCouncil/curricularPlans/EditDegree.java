package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditDegree extends Service {

    public void run(Integer idInternal, String name, String nameEn, String acronym,
            BolonhaDegreeType bolonhaDegreeType, Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea) throws FenixServiceException, 
            ExcepcaoPersistencia {
        if (idInternal == null || name == null || nameEn == null || acronym == null
                || bolonhaDegreeType == null || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToEdit = (Degree) persistentObject.readByOID(
                Degree.class, idInternal);

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        } else if (!degreeToEdit.getAcronym().equalsIgnoreCase(acronym)
                || !degreeToEdit.getNome().equalsIgnoreCase(name)
                || !degreeToEdit.getBolonhaDegreeType().equals(bolonhaDegreeType)) {

        	final List<Degree> degrees = Degree.readAllFromNewDegreeStructure();
            
            // assert unique degree code and unique pair name/type
            for (Degree degree : degrees) {
                if (degree != degreeToEdit) {
                    if (degree.getAcronym().equalsIgnoreCase(acronym)) {
                        throw new FenixServiceException("error.existing.degree.acronym");
                    }
                    if ((degree.getNome().equalsIgnoreCase(name) || degree.getNameEn().equalsIgnoreCase(
                            nameEn))
                            && degree.getBolonhaDegreeType().equals(bolonhaDegreeType)) {
                        throw new FenixServiceException("error.existing.degree.name.and.type");
                    }
                }
            }
        }

        degreeToEdit.edit(name, nameEn, acronym, bolonhaDegreeType, ectsCredits, gradeScale, prevailingScientificArea);
    }

}
