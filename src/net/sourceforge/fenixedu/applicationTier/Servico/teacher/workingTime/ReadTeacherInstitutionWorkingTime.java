/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.TeacherInstitutionWorkingTimeDTO;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadTeacherInstitutionWorkingTime implements IServico {

    private static ReadTeacherInstitutionWorkingTime service = new ReadTeacherInstitutionWorkingTime();

    /**
     * The singleton access method of this class.
     */
    public static ReadTeacherInstitutionWorkingTime getService() {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadTeacherInstitutionWorkingTime";
    }

    public TeacherInstitutionWorkingTimeDTO run(InfoTeacher infoTeacher, Integer executionPeriodId)
            throws FenixServiceException {
        TeacherInstitutionWorkingTimeDTO teacherInstitutionWorkingTimeDTO = new TeacherInstitutionWorkingTimeDTO();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                    .getIPersistentTeacherInstitutionWorkingTime();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, infoTeacher
                    .getIdInternal());
            InfoTeacher infoTeacher2 = Cloner.copyITeacher2InfoTeacher(teacher);

            IExecutionPeriod executionPeriod = null;
            if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else {
                executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                        executionPeriodId);
            }
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

            List teacherInstitutionWorkTimeList = teacherInstitutionWorkingTimeDAO
                    .readByTeacherAndExecutionPeriod(teacher, executionPeriod);

            List infoTeacherInstitutionWorkTimeList = (List) CollectionUtils.collect(
                    teacherInstitutionWorkTimeList, new Transformer() {

                        public Object transform(Object input) {
                            ITeacherInstitutionWorkTime teacherInstitutionWorkTime = (ITeacherInstitutionWorkTime) input;
                            InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = Cloner
                                    .copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime(teacherInstitutionWorkTime);
                            return infoTeacherInstitutionWorkTime;
                        }
                    });

            teacherInstitutionWorkingTimeDTO.setInfoExecutionPeriod(infoExecutionPeriod);
            teacherInstitutionWorkingTimeDTO.setInfoTeacher(infoTeacher2);
            teacherInstitutionWorkingTimeDTO
                    .setInfoTeacherInstitutionWorkTimeList(infoTeacherInstitutionWorkTimeList);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }
        return teacherInstitutionWorkingTimeDTO;
    }
}