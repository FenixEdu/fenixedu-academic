/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.finalProject;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.degree.finalProject.TeacherDegreeFinalProjectStudentsDTO;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class ReadTeacherDFPStudentsService implements IService {

    public TeacherDegreeFinalProjectStudentsDTO run(InfoTeacher infoTeacher, Integer executionPeriodId)
            throws FenixServiceException {
        TeacherDegreeFinalProjectStudentsDTO teacherDfpStudentsDTO = new TeacherDegreeFinalProjectStudentsDTO();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

            IExecutionPeriod executionPeriod = getExecutionPeriod(executionPeriodId, executionPeriodDAO);

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, infoTeacher
                    .getIdInternal());
            InfoTeacher infoTeacher2 = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentTeacherDegreeFinalProjectStudent teacherDfpStudentDAO = sp
                    .getIPersistentTeacherDegreeFinalProjectStudent();

            List teacherDFPStudentList = teacherDfpStudentDAO.readByTeacherAndExecutionPeriod(teacher,
                    executionPeriod);

            List infoteacherDFPStudentList = (List) CollectionUtils.collect(teacherDFPStudentList,
                    new Transformer() {

                        public Object transform(Object input) {
                            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) input;
                            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = Cloner
                                    .copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(teacherDegreeFinalProjectStudent);
                            return infoTeacherDegreeFinalProjectStudent;
                        }
                    });

            teacherDfpStudentsDTO.setInfoTeacher(infoTeacher2);
            teacherDfpStudentsDTO.setInfoExecutionPeriod(infoExecutionPeriod);
            teacherDfpStudentsDTO.setInfoTeacherDegreeFinalProjectStudentList(infoteacherDFPStudentList);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!");
        }

        return teacherDfpStudentsDTO;

    }

    /**
     * @param infoExecutionPeriodParam
     * @param executionPeriodDAO
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private IExecutionPeriod getExecutionPeriod(Integer executionPeriodId,
            IPersistentExecutionPeriod executionPeriodDAO) throws ExcepcaoPersistencia {
        IExecutionPeriod executionPeriod;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

        } else {
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        return executionPeriod;
    }
}