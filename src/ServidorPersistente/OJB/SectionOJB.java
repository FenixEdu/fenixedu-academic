/*
 * SectionOJB.java
 * 
 * Created on 23 de Agosto de 2002, 16:58
 */

package ServidorPersistente.OJB;

/**
 * @author ars
 * @author lmac1
 */

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import Dominio.Section;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;

public class SectionOJB extends PersistentObjectOJB implements IPersistentSection {

    /** Creates a new instance of SeccaoOJB */
    public SectionOJB() {
    }

    public ISection readBySiteAndSectionAndName(ISite site, ISection superiorSection, String name)
            throws ExcepcaoPersistencia {

        Section section = null;
        section = (Section) superiorSection;
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        crit.addEqualTo("site.executionCourse.code", site.getExecutionCourse().getSigla());
        crit.addEqualTo("site.executionCourse.executionPeriod.name", site.getExecutionCourse()
                .getExecutionPeriod().getName());
        crit.addEqualTo("site.executionCourse.executionPeriod.executionYear.year", site
                .getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        if (section == null) {
            crit.addIsNull("keySuperiorSection");
        } else {
            crit.addEqualTo("keySuperiorSection", section.getIdInternal());
        }
        return (ISection) queryObject(Section.class, crit);

    }

    //reads imediatly inferior sections of a section
    public List readBySiteAndSection(ISite site, ISection superiorSection) throws ExcepcaoPersistencia {

        ISection section = (Section) superiorSection;
        Criteria crit = new Criteria();
        crit.addEqualTo("site.executionCourse.code", site.getExecutionCourse().getSigla());
        crit.addEqualTo("site.executionCourse.executionPeriod.name", site.getExecutionCourse()
                .getExecutionPeriod().getName());
        crit.addEqualTo("site.executionCourse.executionPeriod.executionYear.year", site
                .getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        if (section == null) {
            crit.addIsNull("keySuperiorSection");
        } else {
            crit.addEqualTo("keySuperiorSection", section.getIdInternal());
        }
        return queryList(Section.class, crit);

    }

    public List readBySite(ISite site) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("site.executionCourse.code", site.getExecutionCourse().getSigla());
        crit.addEqualTo("site.executionCourse.executionPeriod.name", site.getExecutionCourse()
                .getExecutionPeriod().getName());
        crit.addEqualTo("site.executionCourse.executionPeriod.executionYear.year", site
                .getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return queryList(Section.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Section.class, new Criteria());

    }

    public void delete(ISection section) throws ExcepcaoPersistencia {

        ISection inferiorSection = section;
        List imediatlyInferiorSections = readBySiteAndSection(section.getSite(), section);

        Iterator iterator = imediatlyInferiorSections.iterator();
        while (iterator.hasNext()) {
            inferiorSection = (ISection) iterator.next();
            delete(inferiorSection);
        }

        Criteria crit = new Criteria();
        crit.addEqualTo("section.name", section.getName());
        crit.addEqualTo("section.site.executionCourse.code", section.getSite().getExecutionCourse()
                .getSigla());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name", section.getSite()
                .getExecutionCourse().getExecutionPeriod().getName());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year", section
                .getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        IItem item = null;
        ItemOJB itemOJB = new ItemOJB();

        List result = queryList(Item.class, crit);

        Iterator iterador = result.iterator();
        while (iterador.hasNext()) {
            item = (IItem) iterador.next();
            itemOJB.delete(item);
        }

        super.delete(section);

    }

}