/*
 * Created on 17/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.guide;

import java.util.List;

import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 17/Nov/2003
 */
public class ReimbursementGuideOJB extends PersistentObjectOJB implements IPersistentReimbursementGuide {

    public Integer generateReimbursementGuideNumber() throws ExcepcaoPersistencia {
        Integer reimbursementGuideNumber = new Integer(1);
        Criteria crit = new Criteria();
        List reimbursementGuides = queryList(ReimbursementGuide.class, crit, "number", false);

        if (reimbursementGuides != null && !reimbursementGuides.isEmpty()) {
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) reimbursementGuides.get(0);
            reimbursementGuideNumber = new Integer(reimbursementGuide.getNumber().intValue() + 1);
        }
        return reimbursementGuideNumber;
    }

}