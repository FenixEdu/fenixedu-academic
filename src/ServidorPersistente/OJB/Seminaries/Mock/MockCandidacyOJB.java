/*
 * Created on 31/Jul/2003, 14:46:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries.Mock;
import java.util.List;
import Dominio.Seminaries.ICandidacy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 14:46:18
 * 
 */
public class MockCandidacyOJB extends ObjectFenixOJB implements IPersistentSeminaryCandidacy
{
	public ICandidacy readByName(String name) throws ExcepcaoPersistencia
	{
		throw new ExcepcaoPersistencia();
	}
	public List readAll() throws ExcepcaoPersistencia
	{
		throw new ExcepcaoPersistencia();
	}
	public void delete(ICandidacy candidacy) throws ExcepcaoPersistencia
	{
		throw new ExcepcaoPersistencia();
	}
	public List readByStudentID(Integer id) throws ExcepcaoPersistencia
	{
		throw new ExcepcaoPersistencia();
	}
	public List readByUserInput(
		Integer modalityID,
		Integer seminaryID,
		Integer themeID,
		Integer case1Id,
		Integer case2Id,
		Integer case3Id,
		Integer case4Id,
		Integer case5Id,
		Integer curricularCourseID,
		Integer degreeID,
        Boolean approved)
		throws ExcepcaoPersistencia
	{
		throw new ExcepcaoPersistencia();
	}
	public List readByStudentIDAndSeminaryID(Integer studentID, Integer seminaryID) throws ExcepcaoPersistencia
	{
		throw new ExcepcaoPersistencia();
	}
}
