/*
 * IItemPersistente.java
 *
 * Created on 19 de Agosto de 2002, 12:05
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author ars
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;

public interface IPersistentItem extends IPersistentObject {
    public IItem readBySectionAndName(ISection section, String name) throws ExcepcaoPersistencia;

    public List readAllItemsBySection(ISection section) throws ExcepcaoPersistencia;

    public void delete(IItem item) throws ExcepcaoPersistencia;

}