package ServidorAplicacao.Servico.coordinator;

import java.util.Calendar;

import DataBeans.InfoCurriculum;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 21/Nov/2003
 * 
 */
public class EditCurriculumForCurricularCourse implements IServico
{
	private static EditCurriculumForCurricularCourse service = new EditCurriculumForCurricularCourse();

	public static EditCurriculumForCurricularCourse getService()
	{
		return service;
	}

	public EditCurriculumForCurricularCourse()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public final String getNome()
	{
		return "EditCurriculumForCurricularCourse";
	}

	public Boolean run(
		Integer infoExecutionDegreeId,
		Integer oldCurriculumId,
		Integer curricularCourseCode,
		InfoCurriculum newInfoCurriculum,
		String username,
		String language)
		throws FenixServiceException
	{
		Boolean result = new Boolean(false);
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

			if (oldCurriculumId == null)
			{
				throw new FenixServiceException("nullCurriculumCode");
			}
			if (curricularCourseCode == null)
			{
				throw new FenixServiceException("nullCurricularCourseCode");
			}
			if (newInfoCurriculum == null)
			{
				throw new FenixServiceException("nullCurriculum");
			}
			if (username == null)
			{
				throw new FenixServiceException("nullUsername");
			}

			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse =
				(ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
			if (curricularCourse == null)
			{
				throw new NonExistingServiceException("noCurricularCourse");
			}

			IPessoa person = persistentPerson.lerPessoaPorUsername(username);
			if (person == null)
			{
				throw new NonExistingServiceException("noPerson");
			}

			ICurriculum oldCurriculum = new Curriculum();
			oldCurriculum.setIdInternal(oldCurriculumId);
			oldCurriculum = (ICurriculum) persistentCurriculum.readByOId(oldCurriculum, true);
			if (oldCurriculum == null)
			{
				oldCurriculum = new Curriculum();

				persistentCurriculum.simpleLockWrite(oldCurriculum);

				oldCurriculum.setCurricularCourse(curricularCourse);
				Calendar today = Calendar.getInstance();
				oldCurriculum.setLastModificationDate(today.getTime());
			}

			IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();
			// modification of curriculum is made in context of an execution year
			if (!oldCurriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
				&& !oldCurriculum.getLastModificationDate().after(currentExecutionYear.getEndDate()))
			{
				// let's edit curriculum
				if (language == null)
				{
					oldCurriculum.setGeneralObjectives(newInfoCurriculum.getGeneralObjectives());
					oldCurriculum.setOperacionalObjectives(newInfoCurriculum.getOperacionalObjectives());
					oldCurriculum.setProgram(newInfoCurriculum.getProgram());
				} else
				{
					oldCurriculum.setGeneralObjectivesEn(newInfoCurriculum.getGeneralObjectivesEn());
					oldCurriculum.setOperacionalObjectivesEn(newInfoCurriculum.getOperacionalObjectivesEn());
					oldCurriculum.setProgramEn(newInfoCurriculum.getProgramEn());
				}

				oldCurriculum.setPersonWhoAltered(person);
				Calendar today = Calendar.getInstance();
				oldCurriculum.setLastModificationDate(today.getTime());
			} else
			{
				ICurriculum newCurriculum = new Curriculum();
				persistentCurriculum.simpleLockWrite(newCurriculum);
				newCurriculum.setCurricularCourse(curricularCourse);
				if (language == null)
				{
					newCurriculum.setGeneralObjectives(newInfoCurriculum.getGeneralObjectives());
					newCurriculum.setOperacionalObjectives(newInfoCurriculum.getOperacionalObjectives());
					newCurriculum.setProgram(newInfoCurriculum.getProgram());
				} else
				{
					newCurriculum.setGeneralObjectivesEn(newInfoCurriculum.getGeneralObjectivesEn());
					newCurriculum.setOperacionalObjectivesEn(newInfoCurriculum.getOperacionalObjectivesEn());
					newCurriculum.setProgramEn(newInfoCurriculum.getProgramEn());
				}

				newCurriculum.setPersonWhoAltered(person);
				Calendar today = Calendar.getInstance();
				newCurriculum.setLastModificationDate(today.getTime());

			}
			result = Boolean.TRUE;
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return result;
	}
}