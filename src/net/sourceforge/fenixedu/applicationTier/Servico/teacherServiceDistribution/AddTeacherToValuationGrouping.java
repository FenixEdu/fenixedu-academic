package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author jpmsit, amak
 */
public class AddTeacherToValuationGrouping extends Service {

	public void run(Integer valuationGroupingId, final Integer teacherId) throws FenixServiceException, ExcepcaoPersistencia {

		ValuationGrouping rootGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId).getRootValuationGrouping();
		Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);

		if(rootGrouping.getValuationTeacherByTeacher(teacher) == null){	
			rootGrouping.addValuationTeachers(new ValuationTeacher(teacher));
		}
	}
}
