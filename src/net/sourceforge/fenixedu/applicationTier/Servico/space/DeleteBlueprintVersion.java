package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Blueprint;

public class DeleteBlueprintVersion extends Service {

    public void run(Blueprint blueprint) {
        if(blueprint == null) {
            throw new DomainException("error.delete.blueprint.no.blueprint");
        }
        blueprint.delete();
    }    
}
