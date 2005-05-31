/*
 * ISeccaoPersistente.java
 *
 * Created on 23 de Agosto de 2002, 16:42
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * 
 * @author ars
 * @author lmac1
 */
public interface IPersistentSection extends IPersistentObject {
   
    List readBySiteAndSection(String executionCourseSigla, String executionPeriodName, String year, Integer superiorSectionID) throws ExcepcaoPersistencia;

    List readBySite(String executionCourseSigla, String executionPeriodName, String year) throws ExcepcaoPersistencia;
}