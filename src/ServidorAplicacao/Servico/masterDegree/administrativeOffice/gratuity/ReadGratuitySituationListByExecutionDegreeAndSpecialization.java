/*
 * Created on 22/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGratuitySituation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.GratuitySituationType;
import Util.Specialization;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuitySituationListByExecutionDegreeAndSpecialization implements IService
{
	/**
	 * Constructor
	 */
	public ReadGratuitySituationListByExecutionDegreeAndSpecialization()
	{
	}

	public Object run(
		Integer executionDegreeId,
		String executionYearName,
		String specializationName,
		String gratuitySituationTypeName)
		throws FenixServiceException
	{
		//at least one of the arguments it's obligator
		if (executionDegreeId == null && executionYearName == null)
		{
			throw new FenixServiceException("error.masterDegree.gatuyiuty.impossible.studentsGratuityList");
		}

		if (specializationName == null)
		{
			throw new FenixServiceException("error.masterDegree.gatuyiuty.impossible.studentsGratuityList");
		}
		
		if (gratuitySituationTypeName == null)
		{
			throw new FenixServiceException("error.masterDegree.gatuyiuty.impossible.studentsGratuityList");
		}

		List infoGratuitySituationList = null;
		ISuportePersistente sp = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();

			//EXECUTION DEGREE
			//Fill a execution degree with it's id or execution year chosen
			ICursoExecucao executionDegree = new CursoExecucao();
			if (executionDegreeId != null)
			{
				executionDegree.setIdInternal(executionDegreeId); //atention it can be null
			}
			IExecutionYear executionYear = new ExecutionYear();
			if (executionYearName != null)
			{
				IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
				executionYear = persistentExecutionYear.readExecutionYearByName(executionYearName);

				executionDegree.setExecutionYear(executionYear); //atention it can be null
			}

			//SPECIALIZATION
			Specialization specialization = new Specialization(specializationName);

			//GRATUITY SITUATION
			GratuitySituationType gratuitySituationType = GratuitySituationType.getEnum(gratuitySituationTypeName);
			
			//Read all gratuity situation desired
			IPersistentGratuitySituation persistentGratuitySituation =
				sp.getIPersistentGratuitySituation();
			List gratuitySituationList = null;
			gratuitySituationList =
				persistentGratuitySituation.readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(
					executionDegree,
					specialization,
					gratuitySituationType);

			if (gratuitySituationList != null && gratuitySituationList.size() > 0)
			{
				ListIterator listIterator = gratuitySituationList.listIterator();
				infoGratuitySituationList = new ArrayList();
				while (listIterator.hasNext())
				{
					IGratuitySituation gratuitySituation = (IGratuitySituation) listIterator.next();

					InfoGratuitySituation infoGratuitySituation =
						Cloner.copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

					infoGratuitySituationList.add(infoGratuitySituation);
				}
			}			
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();

			throw new FenixServiceException();
		}

		return infoGratuitySituationList;
	}
}
