/*
 * Created on 20/Nov/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import DataBeans.util.Cloner;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 * 
 * <br>
 * <strong>Description:</strong><br>
 * Standard reading service using the ID to identify the object
 *
 * 
 */
public class ViewReimbursementGuide implements IServico
{

    private static ViewReimbursementGuide servico = new ViewReimbursementGuide();

    /**
     * The singleton access method of this class.
     **/
    public static ViewReimbursementGuide getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     **/
    private ViewReimbursementGuide()
    {
    }

    /**
     * Returns The Service Name */

    public final String getNome()
    {
        return "ViewReimbursementGuide";
    }
    /**
     *  @throws FenixServiceException
     */

    public InfoReimbursementGuide run(Integer reimbursementGuideId) throws FenixServiceException
    {
        try
        {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentReimbursementGuide persistentReimbursementGuide =
                ps.getIPersistentReimbursementGuide();
            IReimbursementGuide reimbursementGuide = new ReimbursementGuide(reimbursementGuideId);
            reimbursementGuide =
                (IReimbursementGuide) persistentReimbursementGuide.readByOId(reimbursementGuide, false);
            InfoReimbursementGuide infoReimbursementGuide =
                Cloner.copyIReimbursementGuide2InfoReimbursementGuide(reimbursementGuide);

            List guideSituations = reimbursementGuide.getReimbursementGuideSituations();
            CollectionUtils.transform(guideSituations, new Transformer()
            {

                public Object transform(Object arg0)
                {
                    InfoReimbursementGuideSituation infoReimbursementGuideSituation =
                        Cloner.copyIReimbursementGuideSituation2InfoReimbursementGuideSituation(
                            (IReimbursementGuideSituation) arg0);
                    return infoReimbursementGuideSituation;
                }

            });
            infoReimbursementGuide.setInfoReimbursementGuideSituations(guideSituations);
            return infoReimbursementGuide;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

}
