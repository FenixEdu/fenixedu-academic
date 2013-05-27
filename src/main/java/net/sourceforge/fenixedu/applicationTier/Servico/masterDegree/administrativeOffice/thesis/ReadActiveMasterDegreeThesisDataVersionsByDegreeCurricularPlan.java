package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesisDataVersionsByDegreeCurricularPlan {

    @Checked("RolePredicates.COORDINATOR_PREDICATE")
    @Service
    public static List run(Integer degreeCurricularPlanID) throws FenixServiceException {

        DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);

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