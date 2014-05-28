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

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;

import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ItemCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString name;
    private MultiLanguageString information;
    private boolean visible;
    private boolean showName;

    private final Section section;
    private Item nextItem;

    private Group permittedGroup;

    public ItemCreator(Section section) {
        super();

        this.section = section;
        this.nextItem = null;
        this.visible = true;
        this.showName = true;
        this.permittedGroup = AnyoneGroup.get();
    }

    public MultiLanguageString getInformation() {
        return this.information;
    }

    public void setInformation(MultiLanguageString information) {
        this.information = information;
    }

    public MultiLanguageString getName() {
        return this.name;
    }

    public void setName(MultiLanguageString name) {
        this.name = name;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public Item getNextItem() {
        return this.nextItem;
    }

    public void setNextItem(Item nextItem) {
        this.nextItem = nextItem;
    }

    public Section getSection() {
        return this.section;
    }

    public Group getPermittedGroup() {
        return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public void createItem() {
        Item item = new Item(getSection(), getName());

        item.setBody(getInformation());
        item.setNextItem(getNextItem());
        item.setVisible(isVisible());
        item.setShowName(isShowName());
        item.setPermittedGroup(getPermittedGroup());
    }
}
