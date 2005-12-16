package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ITeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentTeacherExpectationDefinitionPeriod;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author naat
 * 
 */
public class EditTeacherExpectationDefinitionPeriod implements IService {
    public void run(Integer teacherExpectationDefinitionPeriodID, Date startDate, Date endDate)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentTeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriodDAO = persistenceSupport
                .getIPersistentTeacherExpectationDefinitionPeriod();

        ITeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = (ITeacherExpectationDefinitionPeriod) teacherExpectationDefinitionPeriodDAO
                .readByOID(TeacherExpectationDefinitionPeriod.class,
                        teacherExpectationDefinitionPeriodID);

        teacherExpectationDefinitionPeriod.edit(startDate, endDate);
    }
}