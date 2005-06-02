/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Tânia Pousão
 * 
 */
public class GratuityValuesVO extends VersionedObjectsBase implements IPersistentGratuityValues {

    public IGratuityValues readGratuityValuesByExecutionDegree(Integer executionDegreeID)
            throws ExcepcaoPersistencia {

        final IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeID);
        return executionDegree.getGratuityValues();

    }

}