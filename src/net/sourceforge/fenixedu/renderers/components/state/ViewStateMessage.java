package net.sourceforge.fenixedu.renderers.components.state;

import java.io.Serializable;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class ViewStateMessage implements Serializable {
    
    private MetaSlot slot;
    private String message;
    
    public ViewStateMessage(MetaSlot slot, String message) {
        super();
        this.slot = slot;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public MetaSlot getSlot() {
        return this.slot;
    }
}
