package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * Created on 2004/08/30
 * 
 * @author Luis Cruz
 *  
 */
public interface IPersistentFAQEntries extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public List readEntriesInSection(Integer sectionId) throws ExcepcaoPersistencia;

}