package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.InfoTeacherWithPerson;
import DataBeans.credits.InfoCredits;
import DataBeans.credits.InfoCreditsWithTeacherAndExecutionPeriod;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 *  
 */
public class ReadTeacherCredits implements IService {

    public List run(Integer teacherOID) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherOID);
            IExecutionPeriod startExecutionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(
                    ExecutionPeriod.class, new Integer(2));
            IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

            InfoTeacherWithPerson infoTeacher = new InfoTeacherWithPerson();
            infoTeacher.copyFromDomain(teacher);

            List list = new ArrayList();
            IExecutionPeriod executionPeriod2 = null;
            do {
                InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = new InfoExecutionPeriodWithInfoExecutionYear();
                infoExecutionPeriod.copyFromDomain(executionPeriod);
                
                InfoCredits infoCredits = teacher.getExecutionPeriodCredits(executionPeriod);
                InfoCreditsWithTeacherAndExecutionPeriod infoCreditsWrapper = new InfoCreditsWithTeacherAndExecutionPeriod(infoCredits);
                infoCreditsWrapper.setInfoExecutionPeriod(infoExecutionPeriod);
                infoCreditsWrapper.setInfoTeacher(infoTeacher);
                list.add(infoCreditsWrapper);
                executionPeriod2 = executionPeriod;
                executionPeriod = executionPeriod.getPreviousExecutionPeriod();
            } while (!startExecutionPeriod.equals(executionPeriod2));
            return list;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }

    }

}