/*
 * ISeccaoPersistente.java
 *
 * Created on 23 de Agosto de 2002, 16:42
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;

/**
 * 
 * @author ars
 * @author lmac1
 */
public interface IPersistentSection extends IPersistentObject {
    ISection readBySiteAndSectionAndName(ISite site, ISection superiorSection, String name)
            throws ExcepcaoPersistencia;

    List readBySiteAndSection(ISite site, ISection superiorSection) throws ExcepcaoPersistencia;

    void delete(ISection section) throws ExcepcaoPersistencia;

    List readBySite(ISite site) throws ExcepcaoPersistencia;

    List readAll() throws ExcepcaoPersistencia;
}