/*
 * Created on 28/Jul/2003, 15:20:37
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.Seminaries;

import java.util.List;

import Dominio.Seminaries.ICaseStudy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:20:37
 *  
 */
public interface IPersistentSeminaryCaseStudy extends IPersistentObject {
    ICaseStudy readByName(String name) throws ExcepcaoPersistencia;

    List readAll() throws ExcepcaoPersistencia;

    List readByThemeID(Integer themeID) throws ExcepcaoPersistencia;

    void delete(ICaseStudy caseStudy) throws ExcepcaoPersistencia;
}