package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InsertDegree extends Service {

    public void run(final String code, final String name, final String nameEn, final DegreeType degreeType,
	    final GradeScale gradeScale) throws FenixServiceException {
	if (name == null || nameEn == null || code == null || degreeType == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final List<Degree> degrees = Degree.readOldDegrees();

	// assert unique degree code and unique pair name/type
	for (Degree degree : degrees) {
	    if (degree.getSigla().equalsIgnoreCase(code)) {
		throw new FenixServiceException("error.existing.code");
	    }
	    ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	    if ((degree.getNameFor(currentExecutionYear).getContent(Language.pt).equalsIgnoreCase(name) || degree.getNameFor(
		    currentExecutionYear).getContent(Language.en).equalsIgnoreCase(nameEn))
		    && degree.getDegreeType().equals(degreeType)) {
		throw new FenixServiceException("error.existing.name.and.type");
	    }
	}

	new Degree(name, nameEn, code, degreeType, gradeScale);
    }

}
