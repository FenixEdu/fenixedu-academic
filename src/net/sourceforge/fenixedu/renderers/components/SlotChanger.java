package net.sourceforge.fenixedu.renderers.components;

import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

public interface SlotChanger {
    public void setTargetSlot(MetaSlotKey key);
    public boolean hasTargetSlot();
    public MetaSlotKey getTargetSlot();
}
