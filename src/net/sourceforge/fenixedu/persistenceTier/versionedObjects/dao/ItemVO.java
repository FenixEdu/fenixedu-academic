/*
 * Created on May 23, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author mrsp and jdnf
 */
public class ItemVO extends VersionedObjectsBase implements IPersistentItem {

    public IItem readBySectionAndName(Integer sectionID, String executionCourseSigla,
            String executionPeriodYear, String executionPeriodName, String itemName)
            throws ExcepcaoPersistencia {
        Collection<ISection> sections = readAll(Section.class);
        for (ISection section : sections) {
            if ((section.getIdInternal().equals(sectionID))
                    && (section.getSite().getExecutionCourse().getSigla().equals(executionCourseSigla))
                    && (section.getSite().getExecutionCourse().getExecutionPeriod().getName()
                            .equals(executionPeriodName))
                    && (section.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear()
                            .getYear().equals(executionPeriodYear))) {

                List<IItem> items = section.getAssociatedItems();

                for (IItem item : items) {
                    if (item.getName().equals(itemName))
                        return item;
                }
            }
        }
        return null;
    }

    public List<IItem> readAllItemsBySection(Integer sectionID, String executionCourseSigla,
            String executionPeriodYear, String executionPeriodName) throws ExcepcaoPersistencia {
        Collection<ISection> sections = readAll(Section.class);
        for (ISection section : sections) {
            if ((section.getIdInternal().equals(sectionID))
                    && (section.getSite().getExecutionCourse().getSigla().equals(executionCourseSigla))
                    && (section.getSite().getExecutionCourse().getExecutionPeriod().getName()
                            .equals(executionPeriodName))
                    && (section.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear()
                            .getYear().equals(executionPeriodYear))) {

                return section.getAssociatedItems();
            }
        }
        return new ArrayList();
    }
}
