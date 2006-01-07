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

import net.sourceforge.fenixedu.domain.Lesson;

public interface IAulaPersistente extends IPersistentObject {

    public List<Lesson> readByRoomAndExecutionPeriod(Integer roomOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia;

    public List<Lesson> readLessonsByStudent(String username) throws ExcepcaoPersistencia;

}
