package ServidorAplicacao.Servico.coordinator;

import DataBeans.InfoDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Tânia Pousão Created on 17/Nov/2003 
 */
public class EditDescriptionDegreeCurricularPlan implements IServico
{

    private static EditDescriptionDegreeCurricularPlan service = new EditDescriptionDegreeCurricularPlan();

    public static EditDescriptionDegreeCurricularPlan getService()
    {
        return service;
    }

    private EditDescriptionDegreeCurricularPlan()
    {
    }

    public final String getNome()
    {
        return "EditDescriptionDegreeCurricularPlan";
    }

    public void run(Integer infoExecutionDegreeId, InfoDegreeCurricularPlan newInfoDegreeCP) throws FenixServiceException
    {
        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
        IDegreeCurricularPlan oldDegreeCP = null;
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();

            oldDegreeCP = new DegreeCurricularPlan();
            oldDegreeCP.setIdInternal(newInfoDegreeCP.getIdInternal());

            oldDegreeCP =
                (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(oldDegreeCP, true);
            if (oldDegreeCP == null)
            {
                throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
            }

			//persistentDegreeCurricularPlan.simpleLockWrite(oldDegreeCP);
			
            oldDegreeCP.setDescription(newInfoDegreeCP.getDescription());
            oldDegreeCP.setDescriptionEn(newInfoDegreeCP.getDescriptionEn());

        } catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}