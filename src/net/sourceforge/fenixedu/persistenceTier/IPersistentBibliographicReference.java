package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author PTRLV
 *  
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IExecutionCourse;

public interface IPersistentBibliographicReference extends IPersistentObject {

    public IBibliographicReference readBibliographicReference(IExecutionCourse executionCourse,
            String title, String authors, String reference, String year) throws ExcepcaoPersistencia;

    public void delete(IBibliographicReference bibliographicReference) throws ExcepcaoPersistencia;

    public List readBibliographicReference(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
}