/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.Gratuity;
import net.sourceforge.fenixedu.domain.IGratuity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuity;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GratuityOJB extends PersistentObjectOJB implements IPersistentGratuity {

    public GratuityOJB() {
    }

    public List readByStudentCurricularPlanID(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyStudentCurricularPlan", studentCurricularPlanID);
        return queryList(Gratuity.class, crit);

    }

    public IGratuity readByStudentCurricularPlanIDAndState(Integer studentCurricularPlanID, State state)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyStudentCurricularPlan", studentCurricularPlanID);
        crit.addEqualTo("state", state);
        return (IGratuity) queryObject(Gratuity.class, crit);

    }

}