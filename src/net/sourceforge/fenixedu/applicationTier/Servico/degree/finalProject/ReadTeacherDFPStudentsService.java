/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.TeacherDegreeFinalProjectStudentsDTO;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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