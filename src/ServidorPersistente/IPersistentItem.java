/*
 * IItemPersistente.java
 *
 * Created on 19 de Agosto de 2002, 12:05
 */

package ServidorPersistente;

/**
 * 
 * @author ars
 */
import java.util.List;

import Dominio.IItem;
import Dominio.ISection;

public interface IPersistentItem extends IPersistentObject {
    public IItem readBySectionAndName(ISection section, String name) throws ExcepcaoPersistencia;

    public List readAllItemsBySection(ISection section) throws ExcepcaoPersistencia;

    public void delete(IItem item) throws ExcepcaoPersistencia;

}