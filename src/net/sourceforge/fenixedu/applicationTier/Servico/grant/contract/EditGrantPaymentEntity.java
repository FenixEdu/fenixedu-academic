/*
 * Created on 23/Jan/2004
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantProject;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPaymentEntity extends Service {



    public void run(InfoGrantCostCenter infoObject) throws FenixServiceException,
    				ExcepcaoPersistencia{
        IPersistentGrantPaymentEntity persistentGrantPaymentEntity= persistentSupport.getIPersistentGrantPaymentEntity();
        GrantCostCenter grantCostCenter = (GrantCostCenter)persistentGrantPaymentEntity.readByOID(GrantPaymentEntity.class,infoObject.getIdInternal() );
        if(grantCostCenter == null){
        	grantCostCenter = DomainFactory.makeGrantCostCenter();
        } 
        grantCostCenter.setDesignation(infoObject.getDesignation());
        grantCostCenter.setNumber(infoObject.getNumber());
//      ResponsibleTeacher
    	if (infoObject.getInfoResponsibleTeacher() != null) {
            IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readByNumber(infoObject
                    .getInfoResponsibleTeacher().getTeacherNumber());
            if (teacher == null)
                throw new GrantOrientationTeacherNotFoundException();
            grantCostCenter.setResponsibleTeacher(teacher);
        }
    }

    public void run(InfoGrantProject infoObject) throws FenixServiceException,
    				ExcepcaoPersistencia {
        IPersistentGrantPaymentEntity persistentGrantPaymentEntity= persistentSupport.getIPersistentGrantPaymentEntity();
        IPersistentGrantCostCenter persistentGrantCostCenter= persistentSupport.getIPersistentGrantCostCenter();
        GrantProject grantProject = (GrantProject)persistentGrantPaymentEntity.readByOID(GrantPaymentEntity.class,infoObject.getIdInternal() );
        if(grantProject == null){
        	grantProject = DomainFactory.makeGrantProject();
        } 
    	grantProject.setDesignation(infoObject.getDesignation());
    	grantProject.setNumber(infoObject.getNumber());
    	//Grant Cost Center
    	if (infoObject.getInfoGrantCostCenter() != null ){
	    	final GrantCostCenter  grantCostCenter = (GrantCostCenter) persistentGrantCostCenter.readByOID(GrantCostCenter.class,infoObject.getInfoGrantCostCenter().getIdInternal());
	    	if(grantCostCenter == null)
	    		throw new GrantOrientationTeacherNotFoundException();
	    	grantProject.setGrantCostCenter(grantCostCenter);
	    	
    	}
    	
    	//ResponsibleTeacher
    	if (infoObject.getInfoResponsibleTeacher() != null) {
            IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readByNumber(infoObject
                    .getInfoResponsibleTeacher().getTeacherNumber());
            if (teacher == null)
                throw new GrantOrientationTeacherNotFoundException();
            grantProject.setResponsibleTeacher(teacher);
        }

    	
     }
      
}
