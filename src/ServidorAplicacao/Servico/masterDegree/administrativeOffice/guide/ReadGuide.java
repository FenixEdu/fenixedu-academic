/*
 * Created on 24/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import DataBeans.InfoGuide;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.IGuide;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 */
public class ReadGuide implements IServico
{

    private static ReadGuide servico = new ReadGuide();

    /**
	 * The singleton access method of this class.
	 *  
	 */
    public static ReadGuide getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadGuide()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "ReadGuide";
    }

    public InfoGuide run(Integer guideId) throws FenixServiceException
    {

        ISuportePersistente sp = null;
        IGuide guide = new Guide(guideId);
        InfoGuide infoGuide = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            guide = (IGuide) sp.getIPersistentGuide().readByOId(guide, false);
            if (guide == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            infoGuide = Cloner.copyIGuide2InfoGuide(guide);

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return infoGuide;
    }

}
