/*
 * IItemPersistente.java
 *
 * Created on 19 de Agosto de 2002, 12:05
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author ars
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IItem;

public interface IPersistentItem extends IPersistentObject {

    public IItem readBySectionAndName(Integer sectionID, String executionCourseSigla, String executionCourseYear, String executionPeriodName, String itemName) throws ExcepcaoPersistencia;

    public List readAllItemsBySection(Integer sectionID, String executionCourseSigla, String executionPeriodYear, String executionPeriodName) throws ExcepcaoPersistencia;
}