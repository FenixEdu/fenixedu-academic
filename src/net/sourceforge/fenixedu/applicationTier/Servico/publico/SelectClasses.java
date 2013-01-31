package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Mota
 */
public class SelectClasses extends FenixService {

	@Service
	public static Object run(InfoClass infoClass) {
		final ExecutionDegree executionDegree =
				rootDomainObject.readExecutionDegreeByOID(infoClass.getInfoExecutionDegree().getIdInternal());
		final Set<SchoolClass> classes =
				executionDegree.findSchoolClassesByAcademicIntervalAndCurricularYear(infoClass.getAcademicInterval(),
						infoClass.getAnoCurricular());

		List<InfoClass> infoClasses = new ArrayList<InfoClass>();
		for (SchoolClass taux : classes) {
			infoClasses.add(InfoClass.newInfoFromDomain(taux));
		}

		return infoClasses;
	}

}