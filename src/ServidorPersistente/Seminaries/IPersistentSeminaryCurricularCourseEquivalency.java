/*
 * Created on 4/Ago/2003, 13:12:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.Seminaries;

import java.util.List;

import Dominio.Seminaries.ICourseEquivalency;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Ago/2003, 13:12:10
 *  
 */
public interface IPersistentSeminaryCurricularCourseEquivalency extends IPersistentObject {
    public List readAll() throws ExcepcaoPersistencia;

    public void delete(ICourseEquivalency courseEquivalency) throws ExcepcaoPersistencia;
}