package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ItemCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString name;
    private MultiLanguageString information;

    private DomainReference<Section> section;
    private DomainReference<Item> nextItem;

    private Group permittedGroup;
    
    public ItemCreator(Section section) {
        super();

        this.section = new DomainReference<Section>(section);
        this.nextItem = new DomainReference<Item>(null);
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

    public Item getNextItem() {
        return this.nextItem.getObject();
    }

    public void setNextItem(Item nextItem) {
        this.nextItem = new DomainReference<Item>(nextItem);
    }

    public Section getSection() {
        return this.section.getObject();
    }

    public Group getPermittedGroup() {
        return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public void createItem() {
        Item item = new Item(getSection(), getName());

        item.setInformation(getInformation());
        item.setNextItem(getNextItem());
        item.setPermittedGroup(getPermittedGroup());
    }
}
