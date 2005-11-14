package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 */
public class ReadFinalDegreeWorksByTeacherIDAndExecutionYearID implements IService {

    public List<IProposal> run(Integer teacherID, Integer executionYearID) throws ExcepcaoPersistencia,
            FenixServiceException {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();
        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        IExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();

        List<IProposal> finalDegreeWorks = teacher
                .getFinalDegreeWorksByExecutionYear(previousExecutionYear);

        return finalDegreeWorks;

    }
}