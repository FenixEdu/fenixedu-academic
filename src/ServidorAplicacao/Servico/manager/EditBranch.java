/*
 * Created on 18/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import Dominio.Branch;
import Dominio.IBranch;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class EditBranch implements IService
{

   
    

    /**
     * The constructor of this class.
     */
    public EditBranch()
    {
    }

   

    /**
     * Executes the service.
     */

    public void run(InfoBranch infoBranch) throws FenixServiceException
    {

        ISuportePersistente persistentSuport = null;
        IPersistentBranch persistentbranch = null;
        IBranch branch = null;
        String code = infoBranch.getCode();

        try
        {

            persistentSuport = SuportePersistenteOJB.getInstance();
            persistentbranch = persistentSuport.getIPersistentBranch();
            IBranch helpBranch = new Branch();
            helpBranch.setIdInternal(infoBranch.getIdInternal());
            branch = (IBranch) persistentbranch.readByOId(helpBranch, false);

            if (branch == null) { throw new NonExistingServiceException(); }
            persistentbranch.simpleLockWrite(branch);
            branch.setName(infoBranch.getName());
            branch.setCode(code);


        }
        catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException();
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}