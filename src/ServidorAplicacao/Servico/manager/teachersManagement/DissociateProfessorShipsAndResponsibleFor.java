package ServidorAplicacao.Servico.manager.teachersManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

/**
 * @author Fernanda Quitério 12/Dez/2003
 *  
 */
public class DissociateProfessorShipsAndResponsibleFor implements IServico
{
	private static DissociateProfessorShipsAndResponsibleFor service =
		new DissociateProfessorShipsAndResponsibleFor();
	/**
	 * The singleton access method of this class.
	 */
	public static DissociateProfessorShipsAndResponsibleFor getService()
	{
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome()
	{
		return "DissociateProfessorShipsAndResponsibleFor";
	}

	public HashMap run(Integer teacherNumber, List professorships, List responsibleFors)
		throws FenixServiceException
	{
		List professorshipsWithSupportLessons = new ArrayList();
		List professorshipsWithShifts = new ArrayList();
		HashMap professorshipsNotRemoved = new HashMap();

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
			IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
			IPersistentSupportLesson persistentSupportLesson = sp.getIPersistentSupportLesson();
			IPersistentShiftProfessorship persistentShiftProfessorship =
				sp.getIPersistentShiftProfessorship();

			if (teacherNumber == null)
			{
				throw new FenixServiceException("nullTeacherNumber");
			}

			ITeacher teacher = teacherDAO.readByNumber(teacherNumber);
			if (teacher == null)
			{
				throw new NonExistingServiceException("noTeacher");
			}

			if (professorships != null && responsibleFors != null)
			{
				List newProfessorships = new ArrayList();
				Iterator iterProfessorships = professorships.iterator();
				while (iterProfessorships.hasNext())
				{
					Integer professorshipId = (Integer) iterProfessorships.next();

					IProfessorship professorship = new Professorship();
					professorship.setIdInternal(professorshipId);

					professorship =
						(IProfessorship) persistentProfessorship.readByOId(professorship, false);
					if (professorship == null)
					{
						throw new FenixServiceException("nullPSNorRF");
					}
					if (!professorship.getTeacher().equals(teacher))
					{
						throw new FenixServiceException("notPSNorRFTeacher");
					}
					newProfessorships.add(professorship);
				}

				List newResponsibleFor = new ArrayList();
				Iterator iterResponsibleFor = responsibleFors.iterator();
				while (iterResponsibleFor.hasNext())
				{
					Integer responsibleForId = (Integer) iterResponsibleFor.next();

					IResponsibleFor responsibleFor = new ResponsibleFor();
					responsibleFor.setIdInternal(responsibleForId);

					responsibleFor =
						(IResponsibleFor) persistentResponsibleFor.readByOId(responsibleFor, false);
					if (responsibleFor == null)
					{
						throw new FenixServiceException("nullPSNorRF");
					}
					if (!responsibleFor.getTeacher().equals(teacher))
					{
						throw new FenixServiceException("notPSNorRFTeacher");
					}
					newResponsibleFor.add(responsibleFor);
				}

				//			everithing is ok for removal, but first check
				//			professorships with support lessons and shifts
				iterProfessorships = newProfessorships.iterator();
				while (iterProfessorships.hasNext())
				{
					IProfessorship professorship = (IProfessorship) iterProfessorships.next();

					List supportLessons = persistentSupportLesson.readByProfessorship(professorship);
					List shiftProfessorships =
						persistentShiftProfessorship.readByProfessorship(professorship);

					if ((shiftProfessorships == null || shiftProfessorships.size() == 0)
						&& (supportLessons == null || supportLessons.size() == 0))
					{
						persistentProfessorship.delete(professorship);
					}
					else
					{
						if (supportLessons.size() > 0)
						{
							professorshipsWithSupportLessons.add(
								Cloner.copyIProfessorship2InfoProfessorship(professorship));
						}
						if (shiftProfessorships.size() > 0)
						{
							professorshipsWithShifts.add(
								Cloner.copyIProfessorship2InfoProfessorship(professorship));
						}
					}

				}

				iterResponsibleFor = newResponsibleFor.iterator();
				while (iterResponsibleFor.hasNext())
				{
					IResponsibleFor responsibleFor = (IResponsibleFor) iterResponsibleFor.next();

					persistentResponsibleFor.delete(responsibleFor);
				}
			}
		}
		catch (ExcepcaoPersistencia ex)
		{
			throw new RuntimeException(ex);
		}
		if (professorshipsWithSupportLessons.size() > 0 || professorshipsWithShifts.size() > 0)
		{
			professorshipsNotRemoved.put(new String("supportLessons"), professorshipsWithSupportLessons);
			professorshipsNotRemoved.put(new String("shifts"), professorshipsWithShifts);
		}
		
		return professorshipsNotRemoved;
	}
}