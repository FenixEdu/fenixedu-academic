/**
 * 
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.advise;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.IAdvise;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 * 
 */
public class ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID implements IService {

    public List<IAdvise> run(AdviseType adviseType, Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        List<IAdvise> result;

        if (executionYearID != null) {
            IPersistentExecutionYear persistentExecutionYear = persistentSupport
                    .getIPersistentExecutionYear();
            IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);

            result = teacher.getAdvisesByAdviseTypeAndExecutionYear(adviseType, executionYear);
        } else {
            result = teacher.getAdvisesByAdviseType(adviseType);
        }

        return result;

    }
}