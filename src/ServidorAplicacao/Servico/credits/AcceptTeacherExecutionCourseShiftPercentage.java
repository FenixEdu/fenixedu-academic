/*
 * Created on 19/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.ShiftProfessorship;
import Dominio.Teacher;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class AcceptTeacherExecutionCourseShiftPercentage implements IServico
{
    private static AcceptTeacherExecutionCourseShiftPercentage service =
        new AcceptTeacherExecutionCourseShiftPercentage();

    /**
	 * The singleton access method of this class.
	 */
    public static AcceptTeacherExecutionCourseShiftPercentage getService()
    {
        return service;
    }

    private ITurno getIShift(
        ITurnoPersistente shiftDAO,
        InfoShiftProfessorship infoShiftProfessorship)
        throws ExcepcaoPersistencia
    {
        ITurno shift = new Turno(infoShiftProfessorship.getInfoShift().getIdInternal());
        shift = (ITurno) shiftDAO.readByOId(shift, false);
        return shift;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "AcceptTeacherExecutionCourseShiftPercentage";
    }

    /**
	 * @param infoTeacherShiftPercentageList
	 *                   list of shifts and percentages that teacher needs...
	 * @return @throws
	 *              FenixServiceException
	 */
    public List run(
        InfoTeacher infoTeacher,
        InfoExecutionCourse infoExecutionCourse,
        List infoShiftProfessorshipList)
        throws FenixServiceException
    {
        List shiftWithErrors = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IPersistentShiftProfessorship shiftProfessorshipDAO =
                sp.getIPersistentTeacherShiftPercentage();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

            //read execution course
            IExecutionCourse executionCourse =
                new ExecutionCourse(infoExecutionCourse.getIdInternal());
            executionCourse = (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);

            //read teacher
            ITeacher teacher = new Teacher(infoTeacher.getIdInternal());
            teacher = (ITeacher) teacherDAO.readByOId(teacher, false);

            //read professorship
            IProfessorship professorship =
                professorshipDAO.readByTeacherIDAndExecutionCourseID(teacher, executionCourse);

            if (professorship != null)
            {
                Iterator iterator = infoShiftProfessorshipList.iterator();
                while (iterator.hasNext())
                {
                    InfoShiftProfessorship infoShiftProfessorship =
                        (InfoShiftProfessorship) iterator.next();

                    ITurno shift = getIShift(shiftDAO, infoShiftProfessorship);

                    IShiftProfessorship shiftProfessorship = shiftProfessorshipDAO.readByProfessorshipAndShift(professorship, shift);
                    
                    
                    if (shiftProfessorship == null) {
                        shiftProfessorship = new ShiftProfessorship();
                        shiftProfessorship.setProfessorship(professorship);
                        shiftProfessorship.setShift(shift);
                    }
					shiftProfessorshipDAO.simpleLockWrite(shiftProfessorship);
                    shiftProfessorship.setPercentage(infoShiftProfessorship.getPercentage());

//                    boolean ok = validate(shift, infoShiftProfessorship.getPercentage(), teacher);

//                    if (ok)
//                    {
//                        if (infoShiftProfessorship.getPercentage().floatValue() != 0)
//                        {
//                            lockPercentages(shiftProfessorshipDAO, /* teacherShiftPercentageAdded, */
//                            shift, shiftProfessorship);
//                        }
//                        else
//                        {
//                            //delete because is zero
//                            shiftProfessorship =
//                                shiftProfessorshipDAO.readByUnique(shiftProfessorship);
//
//                            if (shiftProfessorship != null)
//                            {
//                                shiftProfessorshipDAO.delete(shiftProfessorship);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        shiftWithErrors.add(Cloner.copyIShift2InfoShift(shift));
//                    }
                }
            }

            //List teacherShiftPercentageAdded = new ArrayList();

        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException(e);
        }

        return shiftWithErrors; //retorna a lista com os turnos que causaram erros!
    }

//    private void lockPercentages(IPersistentShiftProfessorship teacherShiftPercentageDAO,
//    //List teacherShiftPercentageAdded,
//    ITurno shift, IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia
//    {
//
//        List associatedTeacherProfessorShipPercentage =
//            shift.getAssociatedTeacherProfessorShipPercentage();
//        int indexOf = associatedTeacherProfessorShipPercentage.indexOf(teacherShiftPercentage);
//
//        IShiftProfessorship teacherShiftPercentageToWrite = null;
//        if (indexOf != -1)
//        {
//            teacherShiftPercentageToWrite =
//                (IShiftProfessorship) associatedTeacherProfessorShipPercentage.get(indexOf);
//        }
//        else
//        {
//            teacherShiftPercentageToWrite = teacherShiftPercentage;
//        }
//
//        teacherShiftPercentageDAO.simpleLockWrite(teacherShiftPercentageToWrite);
//        teacherShiftPercentageToWrite.setPercentage(teacherShiftPercentage.getPercentage());
//        //teacherShiftPercentageAdded.add(teacherShiftPercentageToWrite);
//    }
//
//    private boolean validate(ITurno shift, Double percentage, ITeacher teacher)
//    {
//        double percentageAvailable = 100;
//        if (shift.getTipo().getTipo().intValue() != TipoAula.LABORATORIAL)
//        {
//            List teacherProfessorShipPercentageList =
//                shift.getAssociatedTeacherProfessorShipPercentage();
//            Iterator iterator = teacherProfessorShipPercentageList.iterator();
//            while (iterator.hasNext())
//            {
//                IShiftProfessorship teacherShiftPercentageBD = (IShiftProfessorship) iterator.next();
//                if (!teacherShiftPercentageBD.getProfessorShip().getTeacher().equals(teacher))
//                {
//                    percentageAvailable -= teacherShiftPercentageBD.getPercentage().doubleValue();
//                }
//            }
//            if (percentageAvailable < percentage.doubleValue())
//            {
//                // 100% exceeded
//                return false;
//            }
//        }
//        return true;
//    }
}
