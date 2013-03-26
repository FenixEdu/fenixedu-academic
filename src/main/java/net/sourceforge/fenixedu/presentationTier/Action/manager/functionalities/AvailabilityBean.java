package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;

public class AvailabilityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Module module;

    public AvailabilityBean(Module module) {
        super();

        this.module = module;
    }

    private ExpressionGroupAvailability getGroupAvailability() {
        return (ExpressionGroupAvailability) getModule().getAvailabilityPolicy();
    }

    public Container getModule() {
        return this.module;
    }

    public String getExpression() {
        ExpressionGroupAvailability groupAvailability = getGroupAvailability();

        if (groupAvailability == null) {
            return null;
        } else {
            return groupAvailability.getExpression();
        }
    }

}
