/*
 * Created on 17/Nov/2003
 *
 */
package ServidorPersistente.OJB.guide;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IGuide;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.ReimbursementGuide;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *17/Nov/2003
 *
 */
public class ReimbursementGuideOJB extends ObjectFenixOJB implements IPersistentReimbursementGuide
{

    /**
     * 
     */
    public ReimbursementGuideOJB()
    {
        super();
    }

    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("paymentGuide.idInternal", guide.getIdInternal());
        return queryList(ReimbursementGuide.class, crit);
    }

    public Integer generateReimbursementGuideNumber() throws ExcepcaoPersistencia
    {
        Integer reimbursementGuideNumber = new Integer(1);
        Criteria crit = new Criteria();
        crit.addOrderBy("number", true);
        List reimbursementGuides = queryList(ReimbursementGuide.class,crit);
        if (reimbursementGuides!=null) {
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) reimbursementGuides.get(0);
            reimbursementGuideNumber = new Integer(reimbursementGuide.getNumber().intValue()+1);
        }
        return reimbursementGuideNumber;
    }

}
