/*
 * Created on 4/Ago/2003, 13:12:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.ICourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

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