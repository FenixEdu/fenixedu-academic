package net.sourceforge.fenixedu.renderers.components.state;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class ConversionMessage extends SlotMessage {
    
    public ConversionMessage(MetaSlot slot, String message) {
        super(Message.Type.CONVERSION, slot, message);
    }
    
}
