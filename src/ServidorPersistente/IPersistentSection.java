/*
 * ISeccaoPersistente.java
 *
 * Created on 23 de Agosto de 2002, 16:42
 */

package ServidorPersistente;

import java.util.List;

import Dominio.ISection;
import Dominio.ISite;

/**
 *
 * @author  ars
 * @author lmac1
 */
public interface IPersistentSection extends IPersistentObject{
    ISection readBySiteAndSectionAndName(ISite site, ISection superiorSection, String name) throws ExcepcaoPersistencia;
	List readBySiteAndSection(ISite site,ISection superiorSection)throws ExcepcaoPersistencia;
    void lockWrite(ISection section) throws ExcepcaoPersistencia;
    void delete(ISection section) throws ExcepcaoPersistencia;
 
	List readBySite(ISite site) throws ExcepcaoPersistencia;
	List readAll() throws ExcepcaoPersistencia;     
}
