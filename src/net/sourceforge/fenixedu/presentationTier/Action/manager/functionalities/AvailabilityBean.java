package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

public class AvailabilityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Module> module;

    public AvailabilityBean(Module module) {
        super();

        this.module = new DomainReference<Module>(module);
    }

    private ExpressionGroupAvailability getGroupAvailability() {
        return (ExpressionGroupAvailability) getModule().getAvailabilityPolicy();
    }

    public Functionality getModule() {
        return this.module.getObject();
    }
    
    public String getExpression() {
        ExpressionGroupAvailability groupAvailability = getGroupAvailability();
        
        if (groupAvailability == null) {
            return null;
        }
        else {
            return groupAvailability.getExpression();
        }
    }

}
