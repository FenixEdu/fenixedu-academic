/*
 * Created on 2004/04/15
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkStudentGroupByUsername extends Service {

    public InfoGroup run(final Person personUser) throws ExcepcaoPersistencia {
	Registration registration = personUser.getStudentByType(DegreeType.DEGREE);
	if (registration == null) registration = personUser.getStudentByType(DegreeType.BOLONHA_MASTER_DEGREE);
	if (registration == null) registration = personUser.getStudentByType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	if (registration == null) {
	    return null;
	}

	final Group group = registration.findFinalDegreeWorkGroupForCurrentExecutionYear();
        return InfoGroup.newInfoFromDomain(group);
    }

}
