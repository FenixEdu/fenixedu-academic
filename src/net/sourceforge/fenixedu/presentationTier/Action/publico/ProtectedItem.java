package net.sourceforge.fenixedu.presentationTier.Action.publico;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

public class ProtectedItem {

    private Item item;
    private boolean available;
    
    public ProtectedItem(FunctionalityContext context, Item item) {
        super();
    
        this.item = item;
        this.available = item.isAvailable(context);
    }

    public Item getItem() {
        return this.item;
    }

    public boolean isAvailable() {
        return this.available;
    }

}
