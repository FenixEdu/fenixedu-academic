package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;

/**
 *
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class ListStudentThesis {

    private static final Logger logger = LoggerFactory.getLogger(ListStudentThesis.class);

    private String degreeCurricularPlanID;

    public ListStudentThesis() {
    }

    public String getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;

        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).setAttribute(
                "degreeCurricularPlanID", degreeCurricularPlanID);

    }

    public List getMasterDegreeThesisDataVersions() {

        try {
            return run(degreeCurricularPlanID);
        } catch (IllegalDataAccessException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

        return null;

    }

    private static List run(String degreeCurricularPlanID) throws FenixServiceException {

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