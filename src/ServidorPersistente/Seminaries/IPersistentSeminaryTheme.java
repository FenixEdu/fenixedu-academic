/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.Seminaries;

import java.util.List;

import Dominio.Seminaries.ITheme;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public interface IPersistentSeminaryTheme extends IPersistentObject {
    ITheme readByName(String name) throws ExcepcaoPersistencia;

    List readAll() throws ExcepcaoPersistencia;

    void delete(ITheme theme) throws ExcepcaoPersistencia;
}