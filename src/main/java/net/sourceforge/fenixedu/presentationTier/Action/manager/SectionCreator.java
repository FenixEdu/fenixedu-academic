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

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SectionCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString name;

    private boolean visible;

    private Section nextSection;

    private final Site site;

    private Section superiorSection;

    private Group permittedGroup;

    public SectionCreator(Site site) {
        super();

        this.site = site;
        this.superiorSection = null;
        this.nextSection = null;
        this.visible = true;
        this.permittedGroup = AnyoneGroup.get();
    }

    public SectionCreator(Section section) {
        this(section.getOwnerSite());

        setSuperiorSection(section);
    }

    public MultiLanguageString getName() {
        return this.name;
    }

    public void setName(MultiLanguageString name) {
        this.name = name;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Section getNextSection() {
        return this.nextSection;
    }

    public void setNextSection(Section nextSection) {
        this.nextSection = nextSection;
    }

    public Site getSite() {
        return this.site;
    }

    public Section getSuperiorSection() {
        return this.superiorSection;
    }

    public void setSuperiorSection(Section superiorSection) {
        this.superiorSection = superiorSection;
    }

    public Group getPermittedGroup() {
        return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public void createSection() {
        Section section = superiorSection == null ? new Section(site, getName()) : new Section(getSuperiorSection(), getName());
        section.setNextSection(getNextSection());
        section.setPermittedGroup(getPermittedGroup());
        section.setVisible(getVisible());
    }
}
