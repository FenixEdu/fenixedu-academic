/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import DataBeans.util.Cloner;
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
		    setInfoGrantPaymentEntity(Cloner.copyIGrantPaymentEntity2InfoGrantPaymentEntity(grantPart.getGrantPaymentEntity()));
			setInfoResponsibleTeacher(InfoTeacherWithPerson.newInfoFromDomain(grantPart.getResponsibleTeacher()));
			setInfoGrantSubsidy(InfoGrantSubsidyWithContract.newInfoFromDomain(grantPart.getGrantSubsidy()));
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
