/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries.Mock;
import java.util.List;

import Dominio.Seminaries.ITheme;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryTheme;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 * 
 */
public class MockThemeOJB extends ObjectFenixOJB implements IPersistentSeminaryTheme
{
	public ITheme readByName(String name) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
	public List readAll() throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
	public void delete(ITheme theme) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
}
