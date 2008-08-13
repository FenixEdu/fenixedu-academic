package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 */
public class SelectClasses extends Service {

    public Object run(InfoClass infoClass) {
	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoClass.getInfoExecutionDegree()
		.getIdInternal());
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoClass
		.getInfoExecutionPeriod().getIdInternal());
	final Set<SchoolClass> classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionSemester,
		infoClass.getAnoCurricular());

	List<InfoClass> infoClasses = new ArrayList<InfoClass>();
	for (SchoolClass taux : classes) {
	    infoClasses.add(InfoClass.newInfoFromDomain(taux));
	}

	return infoClasses;
    }

}
