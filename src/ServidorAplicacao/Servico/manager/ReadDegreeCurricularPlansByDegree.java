/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansByDegree implements IServico
{

	private static ReadDegreeCurricularPlansByDegree service = new ReadDegreeCurricularPlansByDegree();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadDegreeCurricularPlansByDegree getService()
	{
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadDegreeCurricularPlansByDegree()
	{
	}

	/**
	 * Service name
	 */
	public final String getNome()
	{
		return "ReadDegreeCurricularPlansByDegree";
	}

	/**
	 * Executes the service. Returns the current collection of infoDegreeCurricularPlans.
	 */
	public List run(Integer idDegree) throws FenixServiceException
	{
		ISuportePersistente sp;
		List allDegreeCurricularPlans = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			ICurso degree = sp.getICursoPersistente().readByIdInternal(idDegree);
			allDegreeCurricularPlans = sp.getIPersistentDegreeCurricularPlan().readByDegree(degree);
		}
		catch (ExcepcaoPersistencia excepcaoPersistencia)
		{
			throw new FenixServiceException(excepcaoPersistencia);
		}

		if (allDegreeCurricularPlans == null || allDegreeCurricularPlans.isEmpty())
			return allDegreeCurricularPlans;

		// build the result of this service
		Iterator iterator = allDegreeCurricularPlans.iterator();
		List result = new ArrayList(allDegreeCurricularPlans.size());

		while (iterator.hasNext())
		{
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
			result.add(Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan));
		}
		return result;
	}
}