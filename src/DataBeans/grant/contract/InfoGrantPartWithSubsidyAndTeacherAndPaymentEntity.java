/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import Dominio.grant.contract.IGrantPart;


/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity extends InfoGrantPart
{
	public void copyFromDomain(IGrantPart grantPart)
	{
		super.copyFromDomain(grantPart);
		if(grantPart != null){
			//setInfoGrantSubsidy()
			//setInfoGrantPaymentEntity()
			//setInfoResponsibleTeacher()
		}
	}
	
	public static InfoGrantPart newInfoFromDomain(IGrantPart grantPart)
	{
		InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity infoGrantPartWithSubsidyAndTeacherAndPaymentEntity = null;
		if(grantPart != null){
			infoGrantPartWithSubsidyAndTeacherAndPaymentEntity = new InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity();
			infoGrantPartWithSubsidyAndTeacherAndPaymentEntity.copyFromDomain(grantPart);
		}
		return infoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
	}
	
}
