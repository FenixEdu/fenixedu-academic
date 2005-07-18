/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
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

    private static EditExternalActivity service = new EditExternalActivity();

    public static EditExternalActivity getService() {
        return service;
    }

    /**
     *  
     */
    private EditExternalActivity() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditExternalActivity";
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        return persistentExternalActivity;
    }

//    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
//        IExternalActivity externalActivity = Cloner
//                .copyInfoExternalActivity2IExternalActivity((InfoExternalActivity) infoObject);
//        return externalActivity;
//    }

	@Override
	protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoExternalActivity infoExternalActivity = (InfoExternalActivity)infoObject;
		IExternalActivity externalActivity = (ExternalActivity) domainObject;
		externalActivity.setActivity(infoExternalActivity.getActivity());
		
		externalActivity.setLastModificationDate(infoExternalActivity.getLastModificationDate());

    	IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readByNumber(infoExternalActivity.getInfoTeacher().getTeacherNumber());
		externalActivity.setTeacher(teacher);
		externalActivity.setKeyTeacher(teacher.getIdInternal());
		
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		// TODO Auto-generated method stub
		return new ExternalActivity();
	}

	@Override
	protected Class getDomainObjectClass() {
		// TODO Auto-generated method stub
		return ExternalActivity.class;
	}
}