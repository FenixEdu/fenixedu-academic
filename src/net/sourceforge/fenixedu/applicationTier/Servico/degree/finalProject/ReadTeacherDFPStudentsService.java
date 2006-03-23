/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
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
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadTeacherDFPStudentsService extends Service {

    public TeacherDegreeFinalProjectStudentsDTO run(InfoTeacher infoTeacher, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        TeacherDegreeFinalProjectStudentsDTO teacherDfpStudentsDTO = new TeacherDegreeFinalProjectStudentsDTO();

        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();

        ExecutionPeriod executionPeriod = getExecutionPeriod(executionPeriodId, executionPeriodDAO);

        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);

        Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacher.getIdInternal());
        InfoTeacher infoTeacher2 = InfoTeacher.newInfoFromDomain(teacher);

        IPersistentTeacherDegreeFinalProjectStudent teacherDfpStudentDAO = persistentSupport
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

    private ExecutionPeriod getExecutionPeriod(Integer executionPeriodId,
            IPersistentExecutionPeriod executionPeriodDAO) throws ExcepcaoPersistencia {
        
        final ExecutionPeriod executionPeriod;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        } else {
            executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        }
        return executionPeriod;
    }
}