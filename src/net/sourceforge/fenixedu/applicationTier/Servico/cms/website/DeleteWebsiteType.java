package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteWebsiteType extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia {
        WebsiteType websiteType = rootDomainObject.readWebsiteTypeByOID(oid);
        websiteType.delete();
    }

}
