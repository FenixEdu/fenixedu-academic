package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * 
 * @author ars
 */

public class Item extends Item_Base {

    public String toString() {
        String result = "[ITEM";
        result += ", codInt=" + getIdInternal();
        result += ", nome=" + getName();
        result += ", seccao=" + getSection();
        result += ", ordem=" + getItemOrder();
        result += ", informacao=" + getInformation();
        result += ", urgente=" + getUrgent();
        result += ", chaveSeccao=" + getKeySection();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IItem) {
            IItem item = (IItem) obj;
            resultado = (getName().equals(item.getName())) && getSection().equals(item.getSection());
        }
        return resultado;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/I" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        ISection section = getSection();
        return section;
    }

    public void deleteItem() throws notAuthorizedServiceDeleteException {

        IFileSuport fileSuport = FileSuport.getInstance();
        long size = fileSuport.getDirectorySize(this.getSlideName());
        if (size > 0) {
            throw new notAuthorizedServiceDeleteException();
        }
        
        if (this.getSection() != null && this.getSection().getAssociatedItems() != null) {
           
            List<IItem> items = this.getSection().getAssociatedItems();

            items.remove(this);
            this.setSection(null);

            int associatedItemOrder;
            for (IItem item : items) {
                associatedItemOrder = item.getItemOrder().intValue();
                if (associatedItemOrder > this.getItemOrder().intValue()) {
                    item.setItemOrder(new Integer(associatedItemOrder - 1));
                }
            }
        }
    }

    public void editItem(String newItemName, String newItemInformation, Boolean newItemUrgent,
            Integer newOrder) {
                
        if (newOrder.intValue() != this.getItemOrder().intValue()) {
            newOrder = organizeItemsOrder(newOrder, this.getItemOrder(), this.getSection());
        }        
               
        this.setInformation(newItemInformation);
        this.setItemOrder(newOrder);
        this.setName(newItemName);
        this.setUrgent(newItemUrgent);        
    }

    private Integer organizeItemsOrder(Integer newOrder, Integer oldOrder, ISection section) {
        
        if (newOrder.intValue() == -2) {
            newOrder = new Integer(section.getAssociatedItems().size() - 1);
        }

        List<IItem> items = section.getAssociatedItems();
        
        if (newOrder.intValue() - oldOrder.intValue() > 0)
            for(IItem item : items) {
                int iterItemOrder = item.getItemOrder().intValue();
                if (iterItemOrder > oldOrder.intValue() && iterItemOrder <= newOrder.intValue()) {
                    item.setItemOrder(new Integer(iterItemOrder - 1));
                }
            }
        else
            for(IItem item : items) {                
                int iterItemOrder = item.getItemOrder().intValue();
                if (iterItemOrder >= newOrder.intValue() && iterItemOrder < oldOrder.intValue()) {
                    item.setItemOrder(new Integer(iterItemOrder + 1));
                }
            }
        
        return newOrder;
    }
}
