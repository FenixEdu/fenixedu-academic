/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.Professorship;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

/**
 * @author jpvl
 */
public class ReadProfessorshipByOID extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoProfessorship.newInfoFromDomain((Professorship) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
        return rootDomainObject.readProfessorshipByOID(idInternal);
    }

    // Service Invokers migrated from Berserk

    private static final ReadProfessorshipByOID serviceInstance = new ReadProfessorshipByOID();

    @Service
    public static InfoObject runReadProfessorshipByOID(Integer idInternal) {
        return serviceInstance.run(idInternal);
    }

}