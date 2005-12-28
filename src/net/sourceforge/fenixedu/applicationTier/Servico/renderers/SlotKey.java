package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

public class SlotKey {
    private final ObjectKey objectKey;
    private final String slotName;

    public SlotKey(ObjectKey objectKey, String slotName) {
        this.objectKey = objectKey;
        this.slotName = slotName;
    }

    public ObjectKey getObjectKey() {
        return objectKey;
    }

    public String getSlotName() {
        return slotName;
    }
}
