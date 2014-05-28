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
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;

public class FileContentCreationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private CmsContent content;
    private AnnouncementBoard fileHolder;
    private Site site;

    private String fileName;
    private Long fileSize;

    private String displayName;
    private Group permittedGroup;

    transient private InputStream file;
    private EducationalResourceType educationalLearningResourceType;
    private String authorsName;

    public String getAuthorsName() {
        return authorsName;
    }

    public AnnouncementBoard getFileHolder() {
        return fileHolder;
    }

    public void setFileHolder(AnnouncementBoard fileHolder) {
        this.fileHolder = fileHolder;
    }

    public CmsContent getContent() {
        return content;
    }

    public void setContent(CmsContent content) {
        this.content = content;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public void setAuthorsName(String authorsName) {
        this.authorsName = authorsName;
    }

    public FileContentCreationBean(CmsContent content, Site site) {
        super();
        setSite(site);
        setContent(content);

        this.permittedGroup = AnyoneGroup.get();
    }

    public FileContentCreationBean(AnnouncementBoard container, Site site) {
        super();
        setSite(site);
        setFileHolder(container);

        this.permittedGroup = AnyoneGroup.get();
    }

    public Group getPermittedGroup() {
        return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public InputStream getFile() {
        return this.file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public EducationalResourceType getEducationalLearningResourceType() {
        return educationalLearningResourceType;
    }

    public void setEducationalLearningResourceType(EducationalResourceType educationalLearningResourceType) {
        this.educationalLearningResourceType = educationalLearningResourceType;
    }

}