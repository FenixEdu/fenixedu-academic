package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantTypeNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateOrEditGrantContract extends Service {

    public void run(InfoGrantContract infoGrantContract) throws FenixServiceException,
            ExcepcaoPersistencia {

        final GrantOwner grantOwner = rootDomainObject.readGrantOwnerByOID(infoGrantContract.getGrantOwnerInfo().getIdInternal());
        GrantContract grantContract;
        if (infoGrantContract.getContractNumber() == null) {
            GrantContract maxGrantContract = grantOwner.readGrantContractWithMaximumContractNumber();
            final Integer maxNumber = (maxGrantContract != null) ? maxGrantContract.getContractNumber() : 0;
            final Integer newContractNumber = Integer.valueOf(maxNumber + 1);
            grantContract = new GrantContract(grantOwner,newContractNumber);
            infoGrantContract.setContractNumber(newContractNumber);
        } else {
            grantContract = grantOwner.readGrantContractByNumber(infoGrantContract.getContractNumber()); 
        }

        GrantType grantType = GrantType.readBySigla(infoGrantContract.getGrantTypeInfo().getSigla());
        if (grantType == null) {
            throw new GrantTypeNotFoundException();
        }

        GrantOrientationTeacher grantOrientationTeacher = rootDomainObject
                .readGrantOrientationTeacherByOID(infoGrantContract.getGrantOrientationTeacherInfo()
                        .getIdInternal());
        if (grantOrientationTeacher == null) {
            if (infoGrantContract.getIdInternal() != null
                    || !infoGrantContract.getIdInternal().equals(Integer.valueOf(0))) {                
                grantOrientationTeacher = grantContract.readActualGrantOrientationTeacher();
            }

            if (grantOrientationTeacher == null) {
                grantOrientationTeacher = createNewGrantOrientationTeacher(infoGrantContract
                        .getGrantOrientationTeacherInfo(), grantContract);
            } else {
                final Teacher teacher = Teacher
                        .readByNumber(infoGrantContract.getGrantOrientationTeacherInfo()
                                .getOrientationTeacherInfo().getTeacherNumber());

                grantOrientationTeacher.setOrientationTeacher(teacher);
            }
        }

        if (infoGrantContract.getGrantCostCenterInfo() != null
                && infoGrantContract.getGrantCostCenterInfo().getNumber() != null) {
            GrantCostCenter grantCostCenter = GrantCostCenter
                    .readGrantCostCenterByNumber(infoGrantContract.getGrantCostCenterInfo().getNumber());
            if (grantCostCenter == null) {
                throw new InvalidGrantPaymentEntityException();
            }
            grantContract.setGrantCostCenter(grantCostCenter);
        } else {
            grantContract.setGrantCostCenter(null);
        }
        grantContract.setGrantType(grantType);
        grantContract.addGrantOrientationTeachers(grantOrientationTeacher);
        grantContract.setDateAcceptTerm(infoGrantContract.getDateAcceptTerm());
        grantContract.setEndContractMotive(infoGrantContract.getEndContractMotive());
    }

    private GrantOrientationTeacher createNewGrantOrientationTeacher(
            InfoGrantOrientationTeacher grantOrientationTeacherInfo, GrantContract grantContract)
            throws FenixServiceException, ExcepcaoPersistencia {

        final Teacher teacher = Teacher.readByNumber(grantOrientationTeacherInfo
                .getOrientationTeacherInfo().getTeacherNumber());

        final GrantOrientationTeacher newGrantOrientationTeacher;
        if (teacher == null) {
            throw new GrantOrientationTeacherNotFoundException();
        }

        newGrantOrientationTeacher = new GrantOrientationTeacher();
        newGrantOrientationTeacher.setBeginDate(grantOrientationTeacherInfo.getBeginDate());
        newGrantOrientationTeacher.setEndDate(grantOrientationTeacherInfo.getEndDate());
        newGrantOrientationTeacher.setGrantContract(grantContract);
        newGrantOrientationTeacher.setOrientationTeacher(teacher);

        return newGrantOrientationTeacher;
    }

}
