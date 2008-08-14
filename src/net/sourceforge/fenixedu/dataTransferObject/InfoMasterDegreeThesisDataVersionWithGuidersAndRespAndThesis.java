package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis extends
	InfoMasterDegreeThesisDataVersionWithGuidersAndResp {

    public void copyFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
	super.copyFromDomain(masterDegreeThesisDataVersion);
	if (masterDegreeThesisDataVersion != null) {
	    setInfoMasterDegreeThesis(InfoMasterDegreeThesis.newInfoFromDomain(masterDegreeThesisDataVersion
		    .getMasterDegreeThesis()));

	}
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
	InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis = null;
	if (masterDegreeThesisDataVersion != null) {
	    infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis = new InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis();
	    infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis.copyFromDomain(masterDegreeThesisDataVersion);
	}
	return infoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
    }
}