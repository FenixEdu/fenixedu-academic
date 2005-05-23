/*
 * ItemOJB.java
 *
 * Created on 21 de Agosto de 2002, 16:36
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author ars
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;

import org.apache.ojb.broker.query.Criteria;

public class ItemOJB extends PersistentObjectOJB implements IPersistentItem {

    public IItem readBySectionAndName(Integer sectionID, String executionCourseSigla, String executionPeriodYear, String executionPeriodName, String itemName) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("section.idInternal", sectionID);
        crit.addEqualTo("name", itemName);
        crit.addEqualTo("section.site.executionCourse.code", executionCourseSigla);
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name", executionPeriodName);
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year", executionPeriodYear);
        return (IItem) queryObject(Item.class, crit);

    }

    public List readAllItemsBySection(Integer sectionID, String executionCourseSigla, String executionPeriodYear, String executionPeriodName) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("section.idInternal", sectionID);
        crit.addEqualTo("section.site.executionCourse.code", executionCourseSigla);
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name", executionPeriodName);
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year", executionPeriodYear);
        return queryList(Item.class, crit);

    }
}