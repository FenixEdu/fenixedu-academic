package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * Created on 2004/08/30
 * 
 * @author Luis Cruz
 *  
 */
public interface IPersistentFAQSection extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public List readSubSectionsInSection(Integer sectionId) throws ExcepcaoPersistencia;

}