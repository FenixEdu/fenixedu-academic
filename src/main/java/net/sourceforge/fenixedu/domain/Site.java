/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.cms.SiteTemplate;
import net.sourceforge.fenixedu.domain.cms.TemplatedSectionInstance;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.LoggedGroup;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

/**
 * @author Ivo Brandão
 */
public abstract class Site extends Site_Base {

    public Site() {
        super();

        setBennu(Bennu.getInstance());
    }

    public Section createSection(MultiLanguageString sectionName, Section parentContainer, Integer sectionOrder) {
        return new Section(parentContainer, sectionName, sectionOrder);
    }

    public abstract Group getOwner();

    public void copySectionsAndItemsFrom(Site siteFrom) {
        for (Section sectionFrom : siteFrom.getAssociatedSectionSet()) {
            Section sectionTo = addAssociatedSections(sectionFrom.getName());
            sectionTo.copyItemsFrom(sectionFrom);
            sectionTo.copySubSectionsAndItemsFrom(sectionFrom);
        }
    }

    /**
     * Obtains a list of all the groups available in the context of this site.
     * 
     * @return
     */
    public List<Group> getContextualPermissionGroups() {
        List<Group> groups = new ArrayList<Group>();

        groups.add(AnyoneGroup.get());
        groups.add(LoggedGroup.get());

        return groups;
    }

    /**
     * If this site has quota policy or not.
     * 
     * @return <code>true</code> if we should not exceed the size in {@link #getQuota()}
     */
    public boolean hasQuota() {
        return false;
    }

    /**
     * The maximum size that can be occupied by files in this site.
     * 
     * @return the maximum combined sizes (in bytes) of all files in this site.
     */
    public long getQuota() {
        return 0;
    }

    /**
     * Computes the current size (in bytes) occupied by all the files in this
     * site.
     * 
     * @return
     */
    public long getUsedQuota() {
        long size = 0;

        for (Section section : getAssociatedSectionSet()) {
            for (FileContent attachment : section.getFileContentSet()) {
                size += attachment.getSize();
            }
            for (Item item : section.getChildrenItems()) {
                for (FileContent file : item.getFileContentSet()) {
                    size += file.getSize();
                }
            }
        }

        return size;
    }

    public List<Section> getOrderedSections() {
        List<Section> sections = new ArrayList<Section>();
        SiteTemplate template = getTemplate();
        if (template != null) {
            sections.addAll(template.getOrderedSections());
        }
        sections.addAll(getOrderedAssociatedSections());
        return sections;
    }

    public List<Section> getOrderedAssociatedSections() {
        return Ordering.natural().sortedCopy(getAssociatedSectionSet());
    }

    public List<TemplatedSectionInstance> getTemplatedSectionInstances() {
        return FluentIterable.from(getAssociatedSectionSet()).filter(TemplatedSectionInstance.class).toList();
    }

    public boolean isFileClassificationSupported() {
        return false;
    }

    public SiteTemplate getTemplate() {
        return SiteTemplate.getTemplateForSite(this);
    }

    public boolean isTemplateAvailable() {
        return getTemplate() != null;
    }

    public Section addAssociatedSections(MultiLanguageString sectionName) {
        return new Section(this, sectionName);
    }

    public MultiLanguageString getName() {
        return new MultiLanguageString().with(MultiLanguageString.pt, String.valueOf(getExternalId()));
    }

    public void delete() {
        setBennu(null);
        for (Section section : getAssociatedSectionSet()) {
            section.delete();
        }
        deleteDomainObject();
    }

    public boolean isDeletable() {
        return true;
    }

    public String getReversePath() {
        SiteTemplate template = getTemplate();
        if (template != null) {
            return template.getFunctionality().getFullPath();
        }
        return "";
    }

    public String getFullPath() {
        return CoreConfiguration.getConfiguration().applicationUrl() + getReversePath();
    }

    public CmsContent getInitialContent() {
        List<Section> sections = getOrderedSections();
        return sections.isEmpty() ? null : sections.get(0);
    }

    public static final class SiteMapper {
        @SuppressWarnings("unchecked")
        public static <T extends Site> T getSite(HttpServletRequest request) {
            return (T) request.getAttribute("actual$site");
        }
    }

    public void logCreateSection(Section section) {
    }

    public void logEditSection(Section section) {
    }

    public void logRemoveSection(Section section) {
    }

    public void logRemoveFile(FileContent fileContent) {
    }

    public void logEditFile(FileContent fileContent) {
    }

    public void logSectionInsertInstitutional(Section section) {
    }

    public void logItemFilePermittedGroup(FileContent file, CmsContent section) {
    }

    public void logEditSectionPermission(Section section) {
    }

    public void logCreateItemtoSection(Item item) {
    }

    public void logEditItemtoSection(Item item) {
    }

    public void logEditItemPermission(Item item) {
    }

}
