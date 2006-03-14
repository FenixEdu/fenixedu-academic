package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.query.Criteria;

public class FinalDegreeWorkOJB extends PersistentObjectOJB implements IPersistentFinalDegreeWork {

    public List readFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scheduleing.executionDegrees.idInternal", executionDegreeOID);
        return queryList(Proposal.class, criteria);
    }

    public Scheduleing readFinalDegreeWorkScheduleing(Integer executionDegreeOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegrees.idInternal", executionDegreeOID);
        return (Scheduleing) queryObject(Scheduleing.class, criteria);
    }

    public List readFinalDegreeWorkProposalsByTeacher(Integer teacherOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("orientator.idInternal", teacherOID);
        Criteria criteriaCorientator = new Criteria();
        criteriaCorientator.addEqualTo("coorientator.idInternal", teacherOID);
        criteria.addOrCriteria(criteriaCorientator);
        return queryList(Proposal.class, criteria);
    }

    public List readAprovedFinalDegreeWorkProposals(Integer executionDegreeOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scheduleing.executionDegrees.idInternal", executionDegreeOID);
        criteria.addEqualTo("status", FinalDegreeWorkProposalStatus.APPROVED_STATUS);
        return queryList(Proposal.class, criteria);
    }

    public List readPublishedFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scheduleing.executionDegrees.idInternal", executionDegreeOID);
        criteria.addEqualTo("status", FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
        return queryList(Proposal.class, criteria);
    }

    public Group readFinalDegreeWorkGroupByUsername(String username) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("groupStudents.student.person.user.username", username);
        criteria.addEqualTo("executionDegree.executionYear.state", PeriodState.CURRENT);
        return (Group) queryObject(Group.class, criteria);
    }

    public Proposal readFinalDegreeWorkAttributedToGroupByTeacher(Integer groupOid)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("groupAttributedByTeacher.idInternal", groupOid);
        return (Proposal) queryObject(Proposal.class, criteria);
    }

}