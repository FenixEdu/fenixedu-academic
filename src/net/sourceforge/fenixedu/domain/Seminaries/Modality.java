/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 10:15:55 AM
 *  
 */
public class Modality extends DomainObject implements IModality {

    private String description;

    private String name;

    public Modality() {
    }

    public Modality(Integer idInternal) {
        super(idInternal);
    }

    public Modality(String name, String description) {
        this.setDescription(description);
        this.setName(name);
    }

    /**
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  
     */
    public String toString() {
        String retorno;
        retorno = "[Modality:";
        retorno += "ID=" + this.getIdInternal();
        retorno += "Name=" + this.getName();
        retorno += ",Description=" + this.getDescription() + "]";
        return retorno;
    }

    /**
     * true if the names are equals
     */
    public boolean equals(Object obj) {
        boolean equalsResult = false;
        if (obj instanceof IModality) {
            IModality modality = (IModality) obj;
            if (modality.getName() == null)
                equalsResult = (this.getName() == null);
            else
                equalsResult = this.getName().equalsIgnoreCase(modality.getName());
        }
        return equalsResult;
    }
}