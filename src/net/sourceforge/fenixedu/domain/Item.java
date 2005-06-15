package net.sourceforge.fenixedu.domain;

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
        if (obj instanceof Item) {
            Item item = (Item) obj;
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

}
