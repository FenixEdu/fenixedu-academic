/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Seminaries.ITheme;
import Dominio.Seminaries.Theme;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.*;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 * 
 */
public class ThemeOJB extends ObjectFenixOJB implements IPersistentSeminaryTheme
{
    public ITheme readByName(String name) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (ITheme) super.queryObject(Theme.class, criteria);
    }
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return super.queryList(Theme.class, criteria);
    }
    public void delete(ITheme theme) throws ExcepcaoPersistencia
    {
        super.deleteByOID(Theme.class, theme.getIdInternal());
    }
}
