/*
 * Created on 07/11/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import DataBeans.InfoObject;
import DataBeans.person.InfoQualification;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IQualification;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditQualification extends EditDomainObjectService
{

    private static EditQualification service = new EditQualification();

    public static EditQualification getService()
    {
        return service;
    }

    private EditQualification()
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
        return Cloner.copyInfoQualification2IQualification((InfoQualification) infoObject);
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

    /**
	 * This method invokes a persistent method to read an IDomainObject from database
	 * 
	 * @param domainObject
	 * @return
	 */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentQualification persistentQualification = sp.getIPersistentQualification();
        IQualification oldQualification = (IQualification) domainObject;
        IQualification newQualification =
            persistentQualification.readByYearAndSchoolAndPerson(
                oldQualification.getYear(),
                oldQualification.getSchool(),
                oldQualification.getPerson());
        return newQualification;
    }
}
