/*
 * Created on 29/Jul/2003, 15:29:12
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.Seminaries;

import java.util.List;

import Dominio.Seminaries.ICaseStudyChoice;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

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