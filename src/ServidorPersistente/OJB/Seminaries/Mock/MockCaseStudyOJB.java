/*
 * Created on 31/Jul/2003, 14:46:17
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries.Mock;

import java.util.List;

import Dominio.Seminaries.ICaseStudy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 14:46:12
 * 
 */
public class MockCaseStudyOJB extends ObjectFenixOJB implements IPersistentSeminaryCaseStudy
{
	public ICaseStudy readByName(String name) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
    }

	public List readAll() throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}

	public void delete(ICaseStudy caseStudy) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
    
    public List readByThemeID(Integer id) throws ExcepcaoPersistencia
    {
        throw new ExcepcaoPersistencia();
    }
}
