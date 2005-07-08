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
 * Created at Jul 23, 2003, 10:15:55 AM
 * 
 */
public class Modality extends Modality_Base {

    public String toString() {
        String retorno;
        retorno = "[Modality:";
        retorno += "ID=" + this.getIdInternal();
        retorno += "Name=" + this.getName();
        retorno += ",Description=" + this.getDescription() + "]";
        return retorno;
    }

}
