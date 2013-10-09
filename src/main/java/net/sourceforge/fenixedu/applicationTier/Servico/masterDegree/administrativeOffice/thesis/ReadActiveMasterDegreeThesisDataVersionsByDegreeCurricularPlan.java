package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesisDataVersionsByDegreeCurricularPlan {

    @Atomic
    public static List run(String degreeCurricularPlanID) throws FenixServiceException {
        check(RolePredicates.COORDINATOR_PREDICATE);

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        List masterDegreeThesisDataVersions = degreeCurricularPlan.readActiveMasterDegreeThesisDataVersions();

        if (masterDegreeThesisDataVersions == null || masterDegreeThesisDataVersions.isEmpty()) {
            throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesis");
        }

        CollectionUtils.transform(masterDegreeThesisDataVersions, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (MasterDegreeThesisDataVersion) arg0;
                return InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
                        .newInfoFromDomain(masterDegreeThesisDataVersion);
            }

        });

        return masterDegreeThesisDataVersions;
    }
}