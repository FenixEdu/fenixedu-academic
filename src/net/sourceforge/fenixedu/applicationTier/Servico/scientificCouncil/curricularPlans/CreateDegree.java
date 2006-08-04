package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateDegree extends Service {

    public void run(String name, String nameEn, String acronym, DegreeType degreeType,
            Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea) throws ExcepcaoPersistencia, FenixServiceException {

        if (name == null || nameEn == null || acronym == null || degreeType == null
                || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Degree> degrees = rootDomainObject.getDegrees();

        for (Degree degree : degrees) {
            if (degree.getSigla().equalsIgnoreCase(acronym)) {
                throw new FenixServiceException("error.existing.degree.acronym");
            }
            if ((degree.getNome().equalsIgnoreCase(name) || degree.getNameEn().equalsIgnoreCase(nameEn))
                    && degree.getDegreeType().equals(degreeType)) {
                throw new FenixServiceException("error.existing.degree.name.and.type");
            }
        }

        new Degree(name, nameEn, acronym, degreeType, ectsCredits, gradeScale, prevailingScientificArea);
    }

}
