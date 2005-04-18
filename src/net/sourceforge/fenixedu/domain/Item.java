package net.sourceforge.fenixedu.domain;

/*
 * Item.java
 *
 * Created on 19 de Agosto de 2002, 13:21
 */


import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * 
 * @author ars
 */

public class Item extends Item_Base {

    public Item() {
    }

    public Item(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Item(String name, ISection section, Integer itemOrder, String information, Boolean urgent) {
        setName(name);
        setSection(section);
        setItemOrder(itemOrder);
        setInformation(information);
        setUrgent(urgent);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof Item) {
            Item item = (Item) obj;
            resultado = (getName().equals(item.getName())) && getSection().equals(item.getSection());
        }
        return resultado;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getSlideName()
     */
    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/I" + getIdInternal();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getParentNode()
     */
    public INode getParentNode() {
        ISection section = getSection();
        return section;
    }
}