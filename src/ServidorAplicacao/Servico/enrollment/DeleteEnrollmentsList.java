/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteEnrollmentsList implements IService
{
	public DeleteEnrollmentsList()
	{
	}

	// some of these arguments may be null. they are only needed for filter
	public void run(InfoStudent infoStudent, TipoCurso degreeType, List enrolmentIDList)
		throws FenixServiceException
	{
		try
		{
			if (enrolmentIDList != null && enrolmentIDList.size() > 0)
			{
				ListIterator iterator = enrolmentIDList.listIterator();
				while (iterator.hasNext())
				{
					Integer enrolmentID = (Integer) iterator.next();

					DeleteEnrolment deleteEnrolmentService = new DeleteEnrolment();
					deleteEnrolmentService.run(null, null, enrolmentID);
				}
			}
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}
}