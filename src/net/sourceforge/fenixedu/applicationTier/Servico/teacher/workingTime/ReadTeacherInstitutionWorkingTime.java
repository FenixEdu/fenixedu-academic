/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.TeacherInstitutionWorkingTimeDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author jpvl
 */
public class ReadTeacherInstitutionWorkingTime extends Service {

    public TeacherInstitutionWorkingTimeDTO run(InfoTeacher infoTeacher, Integer executionPeriodId)
            throws FenixServiceException, ExcepcaoPersistencia {
        TeacherInstitutionWorkingTimeDTO teacherInstitutionWorkingTimeDTO = new TeacherInstitutionWorkingTimeDTO();

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = persistentSupport
                .getIPersistentTeacherInstitutionWorkingTime();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
        IPersistentTeacher teacherDAO = persistentSupport.getIPersistentTeacher();

        Teacher teacher = (Teacher) teacherDAO.readByOID(Teacher.class, infoTeacher.getIdInternal());
        InfoTeacher infoTeacher2 = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);

        ExecutionPeriod executionPeriod = null;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (ExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);

        List teacherInstitutionWorkTimeList = teacherInstitutionWorkingTimeDAO
                .readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        List infoTeacherInstitutionWorkTimeList = (List) CollectionUtils.collect(
                teacherInstitutionWorkTimeList, new Transformer() {

                    public Object transform(Object input) {
                        TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) input;
                        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = InfoTeacherInstitutionWorkTime
                                .newInfoFromDomain(teacherInstitutionWorkTime);
                        return infoTeacherInstitutionWorkTime;
                    }
                });

        teacherInstitutionWorkingTimeDTO.setInfoExecutionPeriod(infoExecutionPeriod);
        teacherInstitutionWorkingTimeDTO.setInfoTeacher(infoTeacher2);
        teacherInstitutionWorkingTimeDTO
                .setInfoTeacherInstitutionWorkTimeList(infoTeacherInstitutionWorkTimeList);

        return teacherInstitutionWorkingTimeDTO;
    }
}