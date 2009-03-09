/*
 * Created on 23/Jan/2004
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPaymentEntity extends FenixService {

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static void run(InfoGrantCostCenter infoObject) throws FenixServiceException {
	GrantCostCenter grantCostCenter = (GrantCostCenter) rootDomainObject.readGrantPaymentEntityByOID(infoObject
		.getIdInternal());
	if (grantCostCenter == null) {
	    grantCostCenter = new GrantCostCenter();
	}
	grantCostCenter.setDesignation(infoObject.getDesignation());
	grantCostCenter.setNumber(infoObject.getNumber());

	if (infoObject.getInfoResponsibleTeacher() != null) {
	    Teacher teacher = Teacher.readByNumber(infoObject.getInfoResponsibleTeacher().getTeacherNumber());
	    if (teacher == null)
		throw new GrantOrientationTeacherNotFoundException();
	    grantCostCenter.setResponsibleTeacher(teacher);
	}
    }

}