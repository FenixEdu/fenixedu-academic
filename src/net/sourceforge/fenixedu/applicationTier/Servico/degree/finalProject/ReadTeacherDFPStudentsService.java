/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.TeacherDegreeFinalProjectStudentsDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author jpvl
 */
public class ReadTeacherDFPStudentsService implements IService {

    public TeacherDegreeFinalProjectStudentsDTO run(InfoTeacher infoTeacher, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        TeacherDegreeFinalProjectStudentsDTO teacherDfpStudentsDTO = new TeacherDegreeFinalProjectStudentsDTO();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        ExecutionPeriod executionPeriod = getExecutionPeriod(executionPeriodId, executionPeriodDAO);

        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);

        Teacher teacher = (Teacher) teacherDAO.readByOID(Teacher.class, infoTeacher.getIdInternal());
        InfoTeacher infoTeacher2 = InfoTeacher.newInfoFromDomain(teacher);

        IPersistentTeacherDegreeFinalProjectStudent teacherDfpStudentDAO = sp
                .getIPersistentTeacherDegreeFinalProjectStudent();

        List teacherDFPStudentList = teacherDfpStudentDAO.readByTeacherAndExecutionPeriod(teacher
                .getIdInternal(), executionPeriod.getIdInternal());

        List infoteacherDFPStudentList = (List) CollectionUtils.collect(teacherDFPStudentList,
                new Transformer() {

                    public Object transform(Object input) {
                        TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (TeacherDegreeFinalProjectStudent) input;
                        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson.newInfoFromDomain(
                                teacherDegreeFinalProjectStudent);
                        return infoTeacherDegreeFinalProjectStudent;
                    }
                });

        teacherDfpStudentsDTO.setInfoTeacher(infoTeacher2);
        teacherDfpStudentsDTO.setInfoExecutionPeriod(infoExecutionPeriod);
        teacherDfpStudentsDTO.setInfoTeacherDegreeFinalProjectStudentList(infoteacherDFPStudentList);

        return teacherDfpStudentsDTO;

    }

    /**
     * @param infoExecutionPeriodParam
     * @param executionPeriodDAO
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ExecutionPeriod getExecutionPeriod(Integer executionPeriodId,
            IPersistentExecutionPeriod executionPeriodDAO) throws ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

        } else {
            executionPeriod = (ExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        return executionPeriod;
    }
}