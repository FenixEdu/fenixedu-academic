/*
 * Created on 31/Jul/2003, 14:48:11
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries.Mock;
import java.util.List;

import Dominio.Seminaries.ICaseStudyChoice;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudyChoice;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Jul/2003, 14:48:11
 * 
 */
public class MockCaseStudyChoiceOJB extends ObjectFenixOJB implements IPersistentSeminaryCaseStudyChoice
{
	public List readByCandidacyIdInternal(Integer id) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
	public List readByCaseStudyIdInternal(Integer id) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
	public ICaseStudyChoice readByCaseStudyIdInternalAndCandidacyIdInternal(
		Integer idCaseStudy,
		Integer idCandidacy)
		throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
	public List readAll() throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
	public void delete(ICaseStudyChoice choice) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
}
