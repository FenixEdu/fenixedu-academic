/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.workingTime;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;
import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import DataBeans.teacher.workTime.TeacherInstitutionWorkingTimeDTO;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

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
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
        return teacherInstitutionWorkingTimeDTO;
    }
}