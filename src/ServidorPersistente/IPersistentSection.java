/*
 * ISeccaoPersistente.java
 *
 * Created on 23 de Agosto de 2002, 16:42
 */

package ServidorPersistente;

import Dominio.ISection;
import Dominio.ISite;

/**
 *
 * @author  ars
 */
public interface IPersistentSection {
    ISection readBySiteAndSectionAndName(ISite site, ISection superiorSection, String name) throws ExcepcaoPersistencia;
    void lockWrite(ISection section) throws ExcepcaoPersistencia;
    void delete(ISection section) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
}
