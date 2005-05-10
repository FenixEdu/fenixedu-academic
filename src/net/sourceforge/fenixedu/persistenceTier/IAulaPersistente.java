/*
 * IAulaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 20:55
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.List;

public interface IAulaPersistente extends IPersistentObject {

    public List readByRoomAndExecutionPeriod(Integer roomOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia;

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia;

}