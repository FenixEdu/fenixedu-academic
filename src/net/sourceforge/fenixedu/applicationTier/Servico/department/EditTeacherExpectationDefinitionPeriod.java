package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class EditTeacherExpectationDefinitionPeriod extends Service {
    public void run(Integer teacherExpectationDefinitionPeriodID, Date startDate, Date endDate)
            throws FenixServiceException, ExcepcaoPersistencia {
        TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = rootDomainObject.readTeacherExpectationDefinitionPeriodByOID(teacherExpectationDefinitionPeriodID);
        teacherExpectationDefinitionPeriod.edit(startDate, endDate);
    }
    
}
