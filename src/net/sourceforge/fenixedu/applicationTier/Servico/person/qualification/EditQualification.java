/*
 * Created on 07/11/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditQualification extends EditDomainObjectService {

    public EditQualification() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        return persistentSuport.getIPersistentQualification();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(net.sourceforge.fenixedu.dataTransferObject.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoQualificationWithPersonAndCountry.newDomainFromInfo((InfoQualification) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditQualification";
    }

    //	 Qualification UNIQUE was drop (by PICA). At this point all qualifications
    // are valid!
    //    /**
    //	 * This method invokes a persistent method to read an IDomainObject from
    // database
    //	 *
    //	 * @param domainObject
    //	 * @return
    //	 */
    //    protected IDomainObject readObjectByUnique(IDomainObject domainObject,
    // ISuportePersistente sp)
    //        throws ExcepcaoPersistencia
    //    {
    //        IPersistentQualification persistentQualification =
    // sp.getIPersistentQualification();
    //        IQualification oldQualification = (IQualification) domainObject;
    //        IQualification newQualification =
    //            persistentQualification.readByDateAndSchoolAndPerson(
    //                oldQualification.getDate(),
    //                oldQualification.getSchool(),
    //                oldQualification.getPerson());
    //        return newQualification;
    //    }
}