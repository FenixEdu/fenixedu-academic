/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import sun.security.krb5.internal.util.o;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditOtherTypeCreditLineService extends EditDomainObjectService {

    @Override
	protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoOtherTypeCreditLine infoOtherTypeCreditLine = (InfoOtherTypeCreditLine)infoObject;
		IOtherTypeCreditLine otherTypeCreditLine = (OtherTypeCreditLine)domainObject;
		
		otherTypeCreditLine.setCredits(infoOtherTypeCreditLine.getCredits());
		
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		IExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class,infoOtherTypeCreditLine.getInfoExecutionPeriod().getIdInternal());
		otherTypeCreditLine.setExecutionPeriod(executionPeriod);
		otherTypeCreditLine.setKeyExecutionPeriod(executionPeriod.getIdInternal());
		
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
		ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class,infoOtherTypeCreditLine.getInfoTeacher().getIdInternal());
		otherTypeCreditLine.setKeyTeacher(teacher.getIdInternal());
		otherTypeCreditLine.setTeacher(teacher);
		
		otherTypeCreditLine.setReason(infoOtherTypeCreditLine.getReason());
		
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		return new OtherTypeCreditLine();
	}

	@Override
	protected Class getDomainObjectClass() {
		return OtherTypeCreditLine.class;
	}

	/*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(net.sourceforge.fenixedu.dataTransferObject.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        
    	
    	return Cloner.copyInfoOtherTypeCreditLine2IOtherCreditLine((InfoOtherTypeCreditLine) infoObject);
    }
 

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentOtherTypeCreditLine();
    }
}