package net.sourceforge.fenixedu.renderers.components.state;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class SlotMessage extends Message {

    private MetaSlot slot;
    
    protected SlotMessage(Type type, MetaSlot slot, String message) {
        super(type, message);
        
        setSlot(slot);
    }

    public MetaSlot getSlot() {
        return this.slot;
    }

    protected void setSlot(MetaSlot slot) {
        this.slot = slot;
    }
    
}
