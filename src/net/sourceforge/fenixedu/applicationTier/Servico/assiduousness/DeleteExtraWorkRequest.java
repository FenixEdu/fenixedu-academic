package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;

public class DeleteExtraWorkRequest extends Service {

    public void run(Integer extraWorkRequestID) {
        ExtraWorkRequest extraWorkRequest = (ExtraWorkRequest) RootDomainObject.readDomainObjectByOID(
                ExtraWorkRequest.class, extraWorkRequestID);
        extraWorkRequest.delete();
    }

}
