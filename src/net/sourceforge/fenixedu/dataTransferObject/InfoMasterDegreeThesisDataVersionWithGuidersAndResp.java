package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 *  
 */
public class InfoMasterDegreeThesisDataVersionWithGuidersAndResp extends
        InfoMasterDegreeThesisDataVersionWithGuiders {

    public void copyFromDomain(IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setInfoResponsibleEmployee(InfoEmployeeWithPerson
                    .newInfoFromDomain(masterDegreeThesisDataVersion.getResponsibleEmployee()));
        }
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuidersAndResp infoMasterDegreeThesisDataVersionWithGuidersAndResp = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuidersAndResp = new InfoMasterDegreeThesisDataVersionWithGuidersAndResp();
            infoMasterDegreeThesisDataVersionWithGuidersAndResp
                    .copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuidersAndResp;
    }
}