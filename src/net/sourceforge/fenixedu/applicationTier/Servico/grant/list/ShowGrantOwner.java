/*
 * Created on Jun 21, 2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerComplete;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Pica
 * @author Barbosa
 */
public class ShowGrantOwner implements IService {

    public ShowGrantOwner() {
    }

    private void buildInfoListGrantOwnerComplete(InfoListGrantOwnerComplete infoListGrantOwnerComplete,
            IGrantOwner grantOwner, ISuportePersistente sp) throws FenixServiceException {
        List listInfoListGrantContracts = new ArrayList();
        try {
            //set grantOwner info
            IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
            infoListGrantOwnerComplete.setInfoGrantOwner(InfoGrantOwnerWithPerson
                    .newInfoFromDomain(grantOwner));
            List grantContractsList = persistentGrantContract.readAllContractsByGrantOwner(grantOwner
                    .getIdInternal());
            Iterator contractsIter = grantContractsList.iterator();
            //set list of qualifications
            IPersistentQualification persistentQualification = sp.getIPersistentQualification();
            List infoQualificationsList = persistentQualification.readQualificationsByPerson(grantOwner
                    .getPerson());
            if (infoQualificationsList != null)
                infoListGrantOwnerComplete.setInfoQualifications(infoQualificationsList);
            while (contractsIter.hasNext()) {
                InfoListGrantContract infoListGrantContract = buildInfoListGrantContract(
                        (IGrantContract) contractsIter.next(), sp);
                listInfoListGrantContracts.add(infoListGrantContract);
            }
            Collections.reverse(listInfoListGrantContracts);
            infoListGrantOwnerComplete.setInfoListGrantContracts(listInfoListGrantContracts);
        } catch (Exception e) {
            throw new FenixServiceException();
        }
    }

    private InfoListGrantContract buildInfoListGrantContract(IGrantContract grantContract,
            ISuportePersistente sp) throws FenixServiceException {
        InfoListGrantContract newInfoListGrantContract = new InfoListGrantContract();
        List listInfoListGrantSubsidies = new ArrayList();
        List infoContractRegimes = new ArrayList();
        List infoSubsidyParts = new ArrayList();
        try {
            //Set the grant contract
            newInfoListGrantContract.setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantContract));
            //Set the grant orientation teacher for the contract
            IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = sp
                    .getIPersistentGrantOrientationTeacher();
            IGrantOrientationTeacher grantOrientationTeacher = persistentGrantOrientationTeacher
                    .readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
            newInfoListGrantContract.getInfoGrantContract().setGrantOrientationTeacherInfo(
                    InfoGrantOrientationTeacherWithTeacherAndGrantContract
                            .newInfoFromDomain(grantOrientationTeacher));
            //Read the contract regimes
            IPersistentGrantContractRegime persistentGrantContractRegime = sp
                    .getIPersistentGrantContractRegime();
            List contractRegimes = persistentGrantContractRegime
                    .readGrantContractRegimeByGrantContract(grantContract.getIdInternal());
            Iterator regimesIter = contractRegimes.iterator();
            while (regimesIter.hasNext()) {
                InfoGrantContractRegime newInfoGrantContractRegime = InfoGrantContractRegimeWithTeacherAndContract
                        .newInfoFromDomain((IGrantContractRegime) regimesIter.next());
                infoContractRegimes.add(newInfoGrantContractRegime);
            }
            newInfoListGrantContract.setInfoGrantContractRegimes(infoContractRegimes);
            //read the contract subsidies
            IPersistentGrantSubsidy persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
            List contractSubsidies = persistentGrantSubsidy
                    .readAllSubsidiesByGrantContract(grantContract.getIdInternal());
            Iterator subsidiesIter = contractSubsidies.iterator();
            while (subsidiesIter.hasNext()) {
                InfoListGrantSubsidy newInfoListGrantSubsidy = new InfoListGrantSubsidy();
                InfoGrantSubsidy newInfoGrantSubsidy = InfoGrantSubsidyWithContract
                        .newInfoFromDomain((IGrantSubsidy) subsidiesIter.next());
                newInfoListGrantSubsidy.setInfoGrantSubsidy(newInfoGrantSubsidy);
                //read the subsidy grant parts
                IPersistentGrantPart persistentGrantPart = sp.getIPersistentGrantPart();
                List subsidyParts = persistentGrantPart
                        .readAllGrantPartsByGrantSubsidy(newInfoGrantSubsidy.getIdInternal());
                Iterator partsIter = subsidyParts.iterator();
                while (partsIter.hasNext()) {
                    InfoGrantPart newInfoGrantPart = InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
                            .newInfoFromDomain((IGrantPart) partsIter.next());
                    infoSubsidyParts.add(newInfoGrantPart);
                }
                newInfoListGrantSubsidy.setInfoGrantParts(infoSubsidyParts);
                listInfoListGrantSubsidies.add(newInfoListGrantSubsidy);
            }
            newInfoListGrantContract.setInfoListGrantSubsidys(listInfoListGrantSubsidies);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException();
        }
        return newInfoListGrantContract;
    }

    public InfoListGrantOwnerComplete run(Integer grantOwnerId) throws FenixServiceException {
        ISuportePersistente sp = null;
        InfoListGrantOwnerComplete infoListGrantOwnerComplete = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGrantOwner persistentGrantOwner = sp.getIPersistentGrantOwner();
            IGrantOwner grantOwner = (IGrantOwner) persistentGrantOwner.readByOID(GrantOwner.class,
                    grantOwnerId);
            if (grantOwner != null) {
                infoListGrantOwnerComplete = new InfoListGrantOwnerComplete();
                buildInfoListGrantOwnerComplete(infoListGrantOwnerComplete, grantOwner, sp);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException();
        }
        return infoListGrantOwnerComplete;
    }
}