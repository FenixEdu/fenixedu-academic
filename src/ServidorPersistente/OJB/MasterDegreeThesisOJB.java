/*
 * Created on Oct 10, 2003
 *
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IMasterDegreeThesis;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeThesis;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeThesis;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class MasterDegreeThesisOJB extends PersistentObjectOJB implements IPersistentMasterDegreeThesis {

    /** Creates a new instance of MasterDegreeCandidateOJB */
    public MasterDegreeThesisOJB() {
    }

    public IMasterDegreeThesis readByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
        IMasterDegreeThesis storedMasterDegreeThesis = (IMasterDegreeThesis) queryObject(
                MasterDegreeThesis.class, criteria);

        return storedMasterDegreeThesis;
    }

}