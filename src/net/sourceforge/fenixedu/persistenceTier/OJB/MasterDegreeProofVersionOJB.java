/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class MasterDegreeProofVersionOJB extends PersistentObjectOJB implements
        IPersistentMasterDegreeProofVersion {


    public IMasterDegreeProofVersion readActiveByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("masterDegreeThesis.studentCurricularPlan.idInternal", studentCurricularPlan
                .getIdInternal());
        criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
        IMasterDegreeProofVersion storedMasterDegreeProofVersion = (IMasterDegreeProofVersion) queryObject(
                MasterDegreeProofVersion.class, criteria);

        return storedMasterDegreeProofVersion;
    }

    public List readNotActiveByStudentCurricularPlan(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("masterDegreeThesis.studentCurricularPlan.idInternal", studentCurricularPlanID);
        criteria.addNotEqualTo("currentState", new Integer(State.ACTIVE));
        List result = queryList(MasterDegreeProofVersion.class, criteria, "lastModification", false);

        return result;
    }
}