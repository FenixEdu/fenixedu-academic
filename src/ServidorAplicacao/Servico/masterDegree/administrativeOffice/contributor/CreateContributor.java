/*
 * Created on 21/Mar/2003
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoContributor;
import DataBeans.util.Cloner;
import Dominio.Contributor;
import Dominio.IContributor;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateContributor implements IService
{

    /**
     * The actor of this class.
     */
    public CreateContributor()
    {
    }

    public void run(InfoContributor newContributor) throws Exception
    {

        IContributor contributor = new Contributor();

        ISuportePersistente sp = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            sp.getIPersistentContributor().simpleLockWrite(contributor);
            contributor = Cloner.copyInfoContributor2IContributor(newContributor);

        }
        catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}