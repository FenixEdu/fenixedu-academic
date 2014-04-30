package net.sourceforge.fenixedu.presentationTier.Action.publico;

import net.sourceforge.fenixedu.domain.Item;

public class ProtectedItem {

    private final Item item;
    private final boolean available;

    public ProtectedItem(Item item) {
        super();

        this.item = item;
        this.available = item.isAvailable();
    }

    public Item getItem() {
        return this.item;
    }

    public boolean isAvailable() {
        return this.available;
    }

}
