/*
 * EditExamNew.java
 *
 * Created on 2003/11/25
 */

package ServidorAplicacao.Servico.sop.exams;

/**
 *
 * @author Ana e Ricardo
 **/

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourseScope;
import Dominio.ExecutionCourse;
import Dominio.Exam;
import Dominio.IExam;
import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import Dominio.ISala;
import Dominio.Period;
import Dominio.RoomOccupation;
import Dominio.Sala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentPeriod;
import ServidorPersistente.IPersistentRoomOccupation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;
import Util.Season;

public class EditExamNew implements IServico
{

    private static EditExamNew _servico = new EditExamNew();
    /**
     * The singleton access method of this class.
     **/
    public static EditExamNew getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private EditExamNew()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "EditExamNew";
    }

    public Boolean run(
        Calendar examDate,
        Calendar examStartTime,
        Calendar examEndTime,
        Season season,
        String[] executionCourseIDArray,
        String[] scopeIDArray,
        String[] roomIDArray,
        Integer examID)
        throws FenixServiceException
    {

        Boolean result = new Boolean(false);
        
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IPersistentPeriod persistentPeriod = sp.getIPersistentPeriod();
            IPersistentRoomOccupation roomOccupationDAO = sp.getIPersistentRoomOccupation();

            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examID);
            
			try
			{
			    System.out.println("Vou fazer o lock ao exame a editar");
				persistentExam.simpleLockWrite(exam);
			}
			catch (ExistingPersistentException ex)
			{
				throw new ExistingServiceException(ex);
			}
			
            // Execution Course list
			System.out.println("Vou ler os execution courses associados");
            List executionCourseList = new ArrayList();
            try
            {
                for (int i = 0; i < executionCourseIDArray.length; i++)
                {
                    executionCourseList.add(
                        sp.getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class,
                            new Integer(executionCourseIDArray[i])));
                }
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw new FenixServiceException(ex);
            }
			System.out.println("Vou fazer o set dos execution courses associados");
			if(executionCourseList == null)
			    System.out.println("A lista dos execution courses ta a null!!!!");
			else if(executionCourseList.size() == 0)
			    System.out.println("A lista dos execution courses ta vazia!!!!");
			
            exam.setAssociatedExecutionCourses(executionCourseList);

			// Scopes
			List scopesList = new ArrayList();
			try
			{
				for (int i = 0; i < scopeIDArray.length; i++)
				{
					scopesList.add(
						sp.getIPersistentCurricularCourseScope().readByOID(
							CurricularCourseScope.class,
							new Integer(scopeIDArray[i])));
				}
			}
			catch (ExcepcaoPersistencia ex)
			{
				throw new FenixServiceException(ex);
			}
			exam.setAssociatedCurricularCourseScope(scopesList);


            //TODO
            // This is needed for confirming if doesn't exists an exam for that season
			Iterator iterExecutionCourses = executionCourseList.iterator();
			while (iterExecutionCourses.hasNext())
			{
				ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();
				for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++)
				{
					IExam examAux = (IExam) executionCourse.getAssociatedExams().get(i);
					if (examAux.getSeason().equals(season))
					{
						// is necessary to confirm if is for the same scope
						List scopes = examAux.getAssociatedCurricularCourseScope();
						Iterator iterScopes = scopes.iterator();
						while (iterScopes.hasNext())
						{
							CurricularCourseScope scope = (CurricularCourseScope) iterScopes.next();
							if (scopesList.contains(scope))
								throw new ExistingServiceException();
						}
					}
				}
			}

            // Rooms
            List roomsList = new ArrayList();
            List roomOccupationList = new ArrayList();
            //List roomOccupationList = exam.getAssociatedRoomOccupation();
            IPeriod period;
			System.out.println("Vou ler o period associado");
            if (exam.getAssociatedRoomOccupation() != null
                && exam.getAssociatedRoomOccupation().size() != 0)
            {
                System.out.println("Ja tem period associado! Vou editar o mm");
                period = ((RoomOccupation) exam.getAssociatedRoomOccupation().get(0)).getPeriod();
				System.out.println("Vou fazer o lock ao period");
				persistentPeriod.lockWrite(period);
                period.setStartDate(examDate);
                period.setEndDate(examDate);
            }
            else
            {
				System.out.println("Nao tem period associado! Criar um novo");
                period = new Period();
				System.out.println("Vou fazer o lock ao period");
				persistentPeriod.lockWrite(period);
				period.setStartDate(examDate);
				period.setEndDate(examDate);				
            }
            
			System.out.println("Vou ler os room associados");
			try
            {  			 
			    for (int i = 0; i < roomIDArray.length; i++)
                {
                    ISala room =
                        (ISala) sp.getISalaPersistente().readByOID(
                            Sala.class,
                            new Integer(roomIDArray[i]));
                                        
					System.out.println("Vou adicionar o room " + roomIDArray[i] + " à lista de rooms");
                    roomsList.add(room);
					System.out.println("Vou criar o dia da semana " +  examDate.get(Calendar.DAY_OF_WEEK));
                    DiaSemana weekday = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

                    RoomOccupation roomOccupation =
                        new RoomOccupation(room, examStartTime, examEndTime, weekday);
                    roomOccupation.setPeriod(period);
					System.out.println("Vou criar o room occupation com: " + roomOccupation);

                    if (!exam.getAssociatedRoomOccupation().contains(roomOccupation))
                    {
						System.out.println("O exame n contem o room occupation criado");
                        try
                        {
                            roomOccupationDAO.lockWrite(roomOccupation);
                            System.out.println(
                                "Tou no edit e VOU ESCREVER A SALA "
                                    + roomOccupation.getRoom().getNome());
                        }
                        catch (ExistingPersistentException ex)
                        {
                            throw new ExistingServiceException(ex);
                        }
                        exam.getAssociatedRoomOccupation().add(roomOccupation);
                    }
                    roomOccupationList.add(roomOccupation);
                }
			    
                Iterator iterator = exam.getAssociatedRoomOccupation().iterator();
                List roomOcupationToRemove = new ArrayList();
                while (iterator.hasNext())
                {
                    RoomOccupation oldRoomOccupation = (RoomOccupation) iterator.next();
                    System.out.println(
                        "vou ver se ainda contains " + oldRoomOccupation.getRoom().getNome());
                    if (!roomOccupationList.contains(oldRoomOccupation))
                    {
                        System.out.println(
                            "## VOU REMOVER A SALA "
                                + oldRoomOccupation.getRoom().getNome()
                                + " "
                                + oldRoomOccupation.getRoom().getIdInternal());
                        roomOcupationToRemove.add(oldRoomOccupation);
						roomOccupationDAO.simpleLockWrite(oldRoomOccupation);
						roomOccupationDAO.delete(oldRoomOccupation);
                    }
                }
                System.out.println(
                    "VOU FAZER REMOVEALL de " + roomOcupationToRemove.size() + " ELEMENTO");
                exam.getAssociatedRoomOccupation().removeAll(roomOcupationToRemove);
                System.out.println(
                    "Fiquei com " + exam.getAssociatedRoomOccupation().size() + " ELEMENTO");
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw new FenixServiceException(ex);
            }
            exam.setAssociatedRooms(roomsList);
            //exam.setAssociatedRoomOccupation(roomOccupationList);

            // exam - execution course association
            //            Iterator iter = executionCourseList.iterator();
            //            List examExecutionCourseList = new ArrayList();
            //            while (iter.hasNext())
            //            {
            //                examExecutionCourseList.add(
            //                    new ExamExecutionCourse(exam, (ExecutionCourse) iter.next()));
            //            }
            
            
			System.out.println("Vou fazer o set da data, hora e epoca do exame");
			exam.setDay(examDate);
			exam.setBeginning(examStartTime);
			exam.setEnd(examEndTime);
			exam.setSeason(season);
			
			//persistentExam.lockWrite(exam);
            
            result = new Boolean(true);

        }
        catch (ExcepcaoPersistencia ex)
        {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }
}