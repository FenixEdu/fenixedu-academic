/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftPercentage;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.TeacherExecutionCourseProfessorshipShiftsDTO;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia & Alexandra
 */
public class ReadTeacherExecutionCourseShiftsPercentage implements IService {

    public TeacherExecutionCourseProfessorshipShiftsDTO run(InfoTeacher infoTeacher,
            InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        TeacherExecutionCourseProfessorshipShiftsDTO result = new TeacherExecutionCourseProfessorshipShiftsDTO();

        List infoShiftPercentageList = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = readExecutionCourse(infoExecutionCourse, sp);
            ITeacher teacher = readTeacher(infoTeacher, sp);

            result.setInfoExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
            result.setInfoTeacher(Cloner.copyITeacher2InfoTeacher(teacher));

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

            List executionCourseShiftsList = null;

            executionCourseShiftsList = shiftDAO
                    .readByExecutionCourseID(executionCourse.getIdInternal());

            Iterator iterator = executionCourseShiftsList.iterator();
            while (iterator.hasNext()) {
                IShift shift = (IShift) iterator.next();

                InfoShiftPercentage infoShiftPercentage = new InfoShiftPercentage();
                infoShiftPercentage.setShift((InfoShift) Cloner.get(shift));
                double availablePercentage = 100;
                InfoShiftProfessorship infoShiftProfessorship = null;

                Iterator iter = shift.getAssociatedShiftProfessorship().iterator();
                while (iter.hasNext()) {
                    IShiftProfessorship shiftProfessorship = (IShiftProfessorship) iter.next();
                    /**
                     * if shift's type is LABORATORIAL the shift professorship
                     * percentage can exceed 100%
                     */
                    if ((shift.getTipo().getTipo().intValue() != TipoAula.LABORATORIAL)
                            && (!shiftProfessorship.getProfessorship().getTeacher().equals(teacher))) {
                        availablePercentage -= shiftProfessorship.getPercentage().doubleValue();
                    }
                    infoShiftProfessorship = Cloner
                            .copyIShiftProfessorship2InfoShiftProfessorship(shiftProfessorship);
                    infoShiftPercentage.addInfoShiftProfessorship(infoShiftProfessorship);
                }

                List infoLessons = (List) CollectionUtils.collect(shift.getAssociatedLessons(),
                        new Transformer() {
                            public Object transform(Object input) {
                                ILesson lesson = (ILesson) input;
                                return Cloner.copyILesson2InfoLesson(lesson);
                            }
                        });
                infoShiftPercentage.setInfoLessons(infoLessons);

                infoShiftPercentage.setAvailablePercentage(new Double(availablePercentage));

                infoShiftPercentageList.add(infoShiftPercentage);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        result.setInfoShiftPercentageList(infoShiftPercentageList);
        return result;
    }

    private ITeacher readTeacher(InfoTeacher infoTeacher, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, infoTeacher.getIdInternal());
        return teacher;
    }

    private IExecutionCourse readExecutionCourse(InfoExecutionCourse infoExecutionCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(
                ExecutionCourse.class, infoExecutionCourse.getIdInternal());
        return executionCourse;
    }
}