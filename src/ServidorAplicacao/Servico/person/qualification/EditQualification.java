/*
 * Created on 07/11/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import DataBeans.InfoObject;
import DataBeans.person.InfoQualification;
import DataBeans.person.InfoQualificationWithPersonAndCountry;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditQualification extends EditDomainObjectService
{

    public EditQualification()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        return persistentSuport.getIPersistentQualification();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return InfoQualificationWithPersonAndCountry.newDomainFromInfo((InfoQualification) infoObject);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditQualification";
    }

//	 Qualification UNIQUE was drop (by PICA). At this point all qualifications are valid!
//    /**
//	 * This method invokes a persistent method to read an IDomainObject from database
//	 * 
//	 * @param domainObject
//	 * @return
//	 */
//    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
//        throws ExcepcaoPersistencia
//    {
//        IPersistentQualification persistentQualification = sp.getIPersistentQualification();
//        IQualification oldQualification = (IQualification) domainObject;
//        IQualification newQualification =
//            persistentQualification.readByDateAndSchoolAndPerson(
//                oldQualification.getDate(),
//                oldQualification.getSchool(),
//                oldQualification.getPerson());
//        return newQualification;
//    }
}
