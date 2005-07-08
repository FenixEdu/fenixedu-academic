/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 10:12:40 AM
 *  
 */
public class Theme extends Theme_Base {

    public String toString() {
        String retorno;
        retorno = "[Theme:";
        retorno += "ID=" + this.getIdInternal();
        retorno += "Name=" + this.getName();
        retorno += ",Description=" + this.getDescription();
        retorno += ",Short Name=" + this.getShortName() + "]";
        return retorno;
    }

}
