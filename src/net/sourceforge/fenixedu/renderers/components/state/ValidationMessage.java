package net.sourceforge.fenixedu.renderers.components.state;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class ValidationMessage extends SlotMessage {
    
    public ValidationMessage(MetaSlot slot, String message) {
        super(Message.Type.VALIDATION, slot, message);
    }
    
}
