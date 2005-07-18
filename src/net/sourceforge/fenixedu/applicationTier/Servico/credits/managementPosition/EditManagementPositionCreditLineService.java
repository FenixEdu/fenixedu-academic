/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditManagementPositionCreditLineService extends EditDomainObjectService {

    @Override
	protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoManagementPositionCreditLine infoManagementPositionCreditLine = (InfoManagementPositionCreditLine)infoObject;
		IManagementPositionCreditLine managementPositionCreditLine = (ManagementPositionCreditLine)domainObject;
		managementPositionCreditLine.setCredits(infoManagementPositionCreditLine.getCredits());
		managementPositionCreditLine.setEnd(infoManagementPositionCreditLine.getEnd());
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
		ITeacher teacher = (Teacher)persistentTeacher.readByOID(Teacher.class,infoManagementPositionCreditLine.getInfoTeacher().getIdInternal());
		
		managementPositionCreditLine.setKeyTeacher(teacher.getIdInternal());
		
		managementPositionCreditLine.setPosition(infoManagementPositionCreditLine.getPosition());
		managementPositionCreditLine.setStart(infoManagementPositionCreditLine.getStart());
		
		managementPositionCreditLine.setTeacher(teacher);
		
		
		
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		// TODO Auto-generated method stub
		return new ManagementPositionCreditLine();
	}

	@Override
	protected Class getDomainObjectClass() {
		// TODO Auto-generated method stub
		return ManagementPositionCreditLine.class;
	}

	/*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(net.sourceforge.fenixedu.dataTransferObject.InfoObject)
     */
//    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
//        return Cloner
//                .copyInfoManagementPositionCreditLine2IManagementPositionCreditLine((InfoManagementPositionCreditLine) infoObject);
//    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentManagementPositionCreditLine();
    }
}