package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.CreditsView;
import DataBeans.InfoProfessorship;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class ReadCreditsTeacher implements IServico {
	private static ReadCreditsTeacher service = new ReadCreditsTeacher();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadCreditsTeacher getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadCreditsTeacher";
	}

	public CreditsView run(Integer teacherOID) throws FenixServiceException {
		CreditsView creditsView = new CreditsView();
	
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
			
			//teacher
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			ITeacher teacher = new Teacher();
			teacher.setIdInternal(teacherOID);
			teacher = (ITeacher) teacherDAO.readByOId(teacher, false);
			
			//read actual execution period
			IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

			ICredits creditsTeacherExample = new Credits();
			creditsTeacherExample.setTeacher(teacher);
			creditsTeacherExample.setExecutionPeriod(executionPeriod);

			//read teacher's credits
			IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();
			ICredits creditsTeacher = creditsTeacherDAO.readByUnique(creditsTeacherExample);
			InfoCredits infoCredits;		
			if (creditsTeacher != null) {
				infoCredits = Cloner.copyICreditsTeacher2InfoCreditsTeacher(creditsTeacher);
			}else  {
				infoCredits = Cloner.copyICreditsTeacher2InfoCreditsTeacher(creditsTeacherExample);
			}
			
			
			creditsView.setInfoCredits(infoCredits);
			readProfessorShips(creditsView, teacher, professorshipDAO);
			
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		} 
		return creditsView;
		
	}

	/**
	 * @param infoCreditsTeacher
	 * @param teacher
	 */
	private void readProfessorShips(CreditsView creditsView, ITeacher teacher, IPersistentProfessorship professorshipDAO) throws ExcepcaoPersistencia {
		List professorships =
			professorshipDAO.readByTeacher(teacher);
		Iterator iter = professorships.iterator();
		List infoProfessorshipList = new ArrayList();
		
		while (iter.hasNext()) {
			IProfessorship professorship = (IProfessorship) iter.next();
			InfoProfessorship infoProfessorShip = Cloner.copyIProfessorship2InfoProfessorship(professorship);
			infoProfessorShip.getInfoExecutionCourse().getNome();
			infoProfessorshipList.add(infoProfessorShip);
		}
		creditsView.setInfoProfessorshipList(infoProfessorshipList);
	}
}
