package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditGrantPart extends FenixService {

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static void run(InfoGrantPart infoGrantPart) throws FenixServiceException {

	GrantPart grantPart = rootDomainObject.readGrantPartByOID(infoGrantPart.getIdInternal());
	if (grantPart == null) {
	    grantPart = new GrantPart();
	}

	final GrantPaymentEntity grantPaymentEntity = rootDomainObject.readGrantPaymentEntityByOID(infoGrantPart
		.getInfoGrantPaymentEntity().getIdInternal());
	if (grantPaymentEntity == null) {
	    throw new InvalidGrantPaymentEntityException();
	}
	grantPart.setGrantPaymentEntity(grantPaymentEntity);

	final GrantSubsidy grantSubsidy = rootDomainObject.readGrantSubsidyByOID(infoGrantPart.getInfoGrantSubsidy()
		.getIdInternal());
	grantPart.setGrantSubsidy(grantSubsidy);

	grantPart.setPercentage(infoGrantPart.getPercentage());

	if (infoGrantPart.getInfoResponsibleTeacher() != null) {
	    final Teacher teacher = Teacher.readByIstId(infoGrantPart.getInfoResponsibleTeacher().getTeacherId());
	    if (teacher == null) {
		throw new InvalidPartResponsibleTeacherException();
	    }
	    grantPart.setResponsibleTeacher(teacher);

	} else {
	    grantPart.setResponsibleTeacher(null);
	}
    }

}