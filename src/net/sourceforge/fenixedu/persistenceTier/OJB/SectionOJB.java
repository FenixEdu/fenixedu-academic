/*
 * SectionOJB.java
 * 
 * Created on 23 de Agosto de 2002, 16:58
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author ars
 * @author lmac1
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;

import org.apache.ojb.broker.query.Criteria;

public class SectionOJB extends PersistentObjectOJB implements IPersistentSection {

    public SectionOJB() {}

    public List readBySiteAndSection(String executionCourseSigla, String executionPeriodName, String year, Integer superiorSectionID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("site.executionCourse.code", executionCourseSigla);
        crit.addEqualTo("site.executionCourse.executionPeriod.name", executionPeriodName);
        crit.addEqualTo("site.executionCourse.executionPeriod.executionYear.year", year);
        if (superiorSectionID == null) {
            crit.addIsNull("keySuperiorSection");
        } else {
            crit.addEqualTo("keySuperiorSection", superiorSectionID);
        }
        return queryList(Section.class, crit);

    }

    public List readBySite(String executionCourseSigla, String executionPeriodName, String year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("site.executionCourse.code", executionCourseSigla);
        crit.addEqualTo("site.executionCourse.executionPeriod.name", executionPeriodName);
        crit.addEqualTo("site.executionCourse.executionPeriod.executionYear.year", year);
        return queryList(Section.class, crit);

    }
}