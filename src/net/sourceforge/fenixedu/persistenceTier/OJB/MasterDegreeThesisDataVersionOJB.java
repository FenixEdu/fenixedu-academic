/*
 * Created on Oct 13, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class MasterDegreeThesisDataVersionOJB extends PersistentObjectOJB implements
        IPersistentMasterDegreeThesisDataVersion {

    /** Creates a new instance of MasterDegreeProofVersionOJB */
    public MasterDegreeThesisDataVersionOJB() {
    }

    public IMasterDegreeThesisDataVersion readActiveByMasterDegreeThesis(
            IMasterDegreeThesis masterDegreeThesis) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("masterDegreeThesis.idInternal", masterDegreeThesis.getIdInternal());
        criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
        IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) queryObject(
                MasterDegreeThesisDataVersion.class, criteria);

        return storedMasterDegreeThesisDataVersion;
    }

    public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("masterDegreeThesis.studentCurricularPlan.idInternal", studentCurricularPlan
                .getIdInternal());
        criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
        IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) queryObject(
                MasterDegreeThesisDataVersion.class, criteria);

        return storedMasterDegreeThesisDataVersion;
    }

    public IMasterDegreeThesisDataVersion readActiveByDissertationTitle(String dissertationTitle)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("dissertationTitle", dissertationTitle);
        criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
        IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) queryObject(
                MasterDegreeThesisDataVersion.class, criteria);

        return storedMasterDegreeThesisDataVersion;
    }

    public List readNotActivesVersionsByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("masterDegreeThesis.studentCurricularPlan.idInternal", studentCurricularPlan
                .getIdInternal());
        criteria.addNotEqualTo("currentState", new Integer(State.ACTIVE));
        List result = queryList(MasterDegreeThesisDataVersion.class, criteria, "lastModification", false);

        return result;
    }

}