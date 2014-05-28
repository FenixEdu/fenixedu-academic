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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.cms.CmsContent;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.FenixFramework;

public class FileContent extends FileContent_Base {

    public enum EducationalResourceType {
        EXERCISE("exercise"), SIMULATION("simulation"), QUESTIONNARIE("questionnaire"), FIGURE("figure"), SLIDE("slide"), TABLE(
                "table"), EXAM("exam"), TEST("test"), INFORMATIONS("informations"), MARKSHEET("marksheet"), PROJECT_SUBMISSION(
                "projectSubmission"), LABORATORY_GUIDE("laboratoryGuide"), DIDACTIL_TEXT("didactilText"),
        STUDY_BOOK("studyBook"), SITE_CONTENT("siteContent"), PROGRAM("program"), SUPPORT_TEXT("supportText");

        private String type;

        private EducationalResourceType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

    protected FileContent() {
        super();
        setVisible(true);
    }

    public FileContent(String filename, String displayName, byte[] content, Group group, EducationalResourceType type) {
        this();
        init(filename, displayName, content, group);
        setResourceType(type);
    }

    public static FileContent readByOID(String externalId) {
        return FenixFramework.getDomainObject(externalId);
    }

    private String processDisplayName(String name) {
        return name.replace('\\', '-').replace('/', '-');
    }

    public Site getSite() {
        return getCmsContent() == null ? null : getCmsContent().getOwnerSite();
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        if (getAnnouncementBoard() != null) {
            setAnnouncementBoard(null);
        }
        if (getCmsContent() != null) {
            setCmsContent(null);
        }
    }

    public void logEditFile() {
        final CmsContent content = getCmsContent();
        if (content != null) {
            content.getOwnerSite().logEditFile(this);
        }
    }

    public void logItemFilePermittedGroup() {
        final CmsContent content = getCmsContent();
        if (content != null) {
            content.getOwnerSite().logItemFilePermittedGroup(this, content);
        }
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(processDisplayName(displayName));
    }

}
