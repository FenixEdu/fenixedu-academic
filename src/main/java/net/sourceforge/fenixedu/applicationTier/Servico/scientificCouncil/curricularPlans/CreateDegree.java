package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CreateDegree {

    @Atomic
    public static void run(String name, String nameEn, String acronym, DegreeType degreeType, Double ectsCredits,
            GradeScale gradeScale, String prevailingScientificArea, AdministrativeOffice administrativeOffice)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (name == null || nameEn == null || acronym == null || degreeType == null || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Degree> degrees = Degree.readNotEmptyDegrees();

        for (Degree degree : degrees) {
            if (degree.getSigla().equalsIgnoreCase(acronym)) {
                throw new FenixServiceException("error.existing.degree.acronym");
            }
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

            if ((degree.getNameFor(currentExecutionYear).getContent(Language.pt).equalsIgnoreCase(name) || degree
                    .getNameFor(currentExecutionYear).getContent(Language.en).equalsIgnoreCase(nameEn))
                    && degree.getDegreeType().equals(degreeType)) {
                throw new FenixServiceException("error.existing.degree.name.and.type");
            }
        }

        new Degree(name, nameEn, acronym, degreeType, ectsCredits, gradeScale, prevailingScientificArea, administrativeOffice);
    }

}