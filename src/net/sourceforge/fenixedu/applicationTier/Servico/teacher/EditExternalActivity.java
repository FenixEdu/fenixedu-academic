/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.IExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditExternalActivity extends EditDomainObjectService {

    @Override
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        return persistentExternalActivity;
    }

	@Override
	protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoExternalActivity infoExternalActivity = (InfoExternalActivity)infoObject;
		IExternalActivity externalActivity = (IExternalActivity) domainObject;
		externalActivity.setActivity(infoExternalActivity.getActivity());
		
		externalActivity.setLastModificationDate(infoExternalActivity.getLastModificationDate());

    	IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readByNumber(infoExternalActivity.getInfoTeacher().getTeacherNumber());
		externalActivity.setTeacher(teacher);
		externalActivity.setKeyTeacher(teacher.getIdInternal());
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		return DomainFactory.makeExternalActivity();
	}

	@Override
	protected Class getDomainObjectClass() {
		return ExternalActivity.class;
	}

}
