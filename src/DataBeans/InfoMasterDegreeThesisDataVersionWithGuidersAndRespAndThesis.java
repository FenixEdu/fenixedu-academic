package DataBeans;

import Dominio.IMasterDegreeThesisDataVersion;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 *  
 */
public class InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis extends
        InfoMasterDegreeThesisDataVersionWithGuidersAndResp {

    public void copyFromDomain(IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setInfoMasterDegreeThesis(InfoMasterDegreeThesis
                    .newInfoFromDomain(masterDegreeThesisDataVersion.getMasterDegreeThesis()));

        }
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis = new InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis();
            infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
                    .copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
    }
}