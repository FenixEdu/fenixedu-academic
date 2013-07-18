package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EditDegree {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(Integer idInternal, String name, String nameEn, String acronym, DegreeType degreeType,
            Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea, ExecutionYear executionYear)
            throws FenixServiceException {
        if (idInternal == null || name == null || nameEn == null || acronym == null || degreeType == null || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToEdit = RootDomainObject.getInstance().readDegreeByOID(idInternal);

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        } else if (!degreeToEdit.getSigla().equalsIgnoreCase(acronym)
                || !degreeToEdit.getNameFor(executionYear).getContent(Language.pt).equalsIgnoreCase(name)
                || !degreeToEdit.getDegreeType().equals(degreeType)) {

            final List<Degree> degrees = Degree.readNotEmptyDegrees();

            // assert unique degree code and unique pair name/type
            for (Degree degree : degrees) {
                if (degree != degreeToEdit) {
                    if (degree.getSigla().equalsIgnoreCase(acronym)) {
                        throw new FenixServiceException("error.existing.degree.acronym");
                    }
                    if ((degree.getNameFor(executionYear).getContent(Language.pt).equalsIgnoreCase(name) || degree
                            .getNameFor(executionYear).getContent(Language.en).equalsIgnoreCase(nameEn))
                            && degree.getDegreeType().equals(degreeType)) {
                        throw new FenixServiceException("error.existing.degree.name.and.type");
                    }
                }
            }
        }

        degreeToEdit.edit(name, nameEn, acronym, degreeType, ectsCredits, gradeScale, prevailingScientificArea, executionYear);
    }

}