/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.util;

public enum LegalRegimenType {

    PROVISIONS_ADMINISTRATIVE_CONTRACT,
    PROVISIONS_ADMINISTRATIVE_CONTRACT_SUSPENDED,
    TERM_CONTRACT,
    TEMPORARY_SUBSTITUTION,
    DEFINITIVE_PROVISORY,
    DEFINITIVE_NOMINATION;
        
    public String getName() {
        return name();
    } 
}
