/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoShift;
import DataBeans.InfoSiteShifts;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IAula;
import Dominio.IGroupProperties;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class ReadGroupPropertiesShifts implements IServico {

	private static ReadGroupPropertiesShifts service =
		new ReadGroupPropertiesShifts();

	/**
		* The singleton access method of this class.
		*/
	public static ReadGroupPropertiesShifts getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private ReadGroupPropertiesShifts() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "ReadGroupPropertiesShifts";
	}

	/**
	 * Executes the service.
	 */
	public ISiteComponent run(Integer groupPropertiesCode,String username)
		throws FenixServiceException {
		
		
		List infoShifts = new ArrayList();
		IGroupProperties groupProperties = null;
		InfoSiteShifts infoSiteShifts = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
		
			groupProperties = (IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode),false);
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
			
			List usernames = new ArrayList();
			usernames.add(username);
			
			boolean result = strategy.checkAlreadyEnroled(groupProperties,usernames);
			if(result)
			{
				
				List executionCourseShifts =sp.getITurnoPersistente().readByExecutionCourse(groupProperties.getExecutionCourse());
			
				List shifts = strategy.checkShiftsType(groupProperties,executionCourseShifts);
			
			
				if (shifts == null || shifts.isEmpty()) {

				} else {

					for (int i = 0; i < shifts.size(); i++) {
					ITurno shift = (ITurno) shifts.get(i);
					InfoShift infoShift =
						new InfoShift(shift.getNome(),shift.getTipo(),shift.getLotacao(),Cloner.copyIExecutionCourse2InfoExecutionCourse(groupProperties.getExecutionCourse()));


					List lessons =
						sp.getITurnoAulaPersistente().readByShift(shift);
					List infoLessons = new ArrayList();
					List classesShifts =
						sp.getITurmaTurnoPersistente().readClassesWithShift(
							shift);
					List infoClasses = new ArrayList();

					for (int j = 0; j < lessons.size(); j++)
						infoLessons.add(
							Cloner.copyILesson2InfoLesson(
								(IAula) lessons.get(j)));

					infoShift.setInfoLessons(
						infoLessons);

					for (int j = 0; j < classesShifts.size(); j++)
						infoClasses.add(
							Cloner.copyClass2InfoClass(
								((ITurmaTurno) classesShifts.get(j))
									.getTurma()));

					infoShift.setInfoClasses(infoClasses);
					infoShift.setIdInternal(shift.getIdInternal());

					infoShifts.add(infoShift);
				}
			}
			infoSiteShifts = new InfoSiteShifts();
			infoSiteShifts.setShifts(infoShifts);
			infoSiteShifts.setInfoExecutionPeriodName(groupProperties.getExecutionCourse().getExecutionPeriod().getName());
			infoSiteShifts.setInfoExecutionYearName(groupProperties.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
		
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoSiteShifts;
	}

}
