/*
 * IItemPersistente.java
 *
 * Created on 19 de Agosto de 2002, 12:05
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */
import Dominio.IItem;
import Dominio.ISection;

public interface IPersistentItem {
    public IItem readBySectionAndName(ISection section, String name) throws ExcepcaoPersistencia;
    public void lockWrite(ISection item) throws ExcepcaoPersistencia;
    public void delete(IItem item) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
