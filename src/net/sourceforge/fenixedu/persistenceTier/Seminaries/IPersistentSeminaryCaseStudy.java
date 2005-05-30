/*
 * Created on 28/Jul/2003, 15:20:37
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:20:37
 *  
 */
public interface IPersistentSeminaryCaseStudy extends IPersistentObject {

    List readAll() throws ExcepcaoPersistencia;

    List readByThemeID(Integer themeID) throws ExcepcaoPersistencia;

}