package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse extends Service {

    public List<InfoClass> run(ExecutionCourse executionCourse) {

	final Set<SchoolClass> classes = executionCourse.findSchoolClasses();
	final List<InfoClass> infoClasses = new ArrayList<InfoClass>(classes.size());

	for (final SchoolClass schoolClass : classes) {
	    final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
	    infoClasses.add(infoClass);
	}

	return infoClasses;
    }
}