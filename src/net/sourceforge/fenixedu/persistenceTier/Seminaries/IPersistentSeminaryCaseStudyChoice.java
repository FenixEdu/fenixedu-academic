/*
 * Created on 29/Jul/2003, 15:29:12
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 15:29:12
 *  
 */
public interface IPersistentSeminaryCaseStudyChoice extends IPersistentObject {
    List readByCandidacyIdInternal(Integer id) throws ExcepcaoPersistencia;

    List readByCaseStudyIdInternal(Integer id) throws ExcepcaoPersistencia;

    ICaseStudyChoice readByCaseStudyIdInternalAndCandidacyIdInternal(Integer idCaseStudy,
            Integer idCandidacy) throws ExcepcaoPersistencia;

    List readAll() throws ExcepcaoPersistencia;

    void delete(ICaseStudyChoice choice) throws ExcepcaoPersistencia;
}