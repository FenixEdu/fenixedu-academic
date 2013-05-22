package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID extends FenixService {

    protected List run(Integer candidateID) throws FenixServiceException {
        List result = new ArrayList();

        MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);

        if (masterDegreeCandidate == null) {
            throw new NonExistingServiceException();
        }

        List candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();

        if (candidateEnrolments == null) {
            throw new NonExistingServiceException();
        }

        for (final Iterator candidateEnrolmentIterator = candidateEnrolments.iterator(); candidateEnrolmentIterator.hasNext();) {
            CandidateEnrolment candidateEnrolmentTemp = (CandidateEnrolment) candidateEnrolmentIterator.next();
            InfoCandidateEnrolment infoCandidateEnrolment =
                    InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree
                            .newInfoFromDomain(candidateEnrolmentTemp);
            result.add(infoCandidateEnrolment);
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCandidateEnrolmentsByCandidateID serviceInstance = new ReadCandidateEnrolmentsByCandidateID();

    @Service
    public static List runReadCandidateEnrolmentsByCandidateID(Integer candidateID) throws FenixServiceException,
            NotAuthorizedException {
        ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter.instance.execute(candidateID);
        return serviceInstance.run(candidateID);
    }

}