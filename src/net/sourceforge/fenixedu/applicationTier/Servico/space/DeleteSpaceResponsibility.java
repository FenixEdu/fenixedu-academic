package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;

public class DeleteSpaceResponsibility extends Service {

    public boolean run(SpaceResponsibility  spaceResponsibility) {        
        if(spaceResponsibility != null) {
            spaceResponsibility.delete();
        }        
        return true;
    }    
}
