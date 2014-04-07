package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DuplicatedNameException;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * An Item represents a piece of text a user can add to a section of a site. It
 * contains a title and a body text: the item's information.
 * 
 * @author ars
 */
public class Item extends Item_Base {

    protected Item() {
        super();
    }

    public Item(Section section, MultiLanguageString name) {
        this();

        if (section == null) {
            throw new NullPointerException();
        }

        setParent(section);
        setName(name);
    }

    public Item(Section section, MultiLanguageString name, MultiLanguageString information, Integer itemOrder, Boolean showName) {
        this(section, name);
        setBody(information);
        setShowName(showName);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (name == null) {
            throw new NullPointerException();
        }

        if (!isNameUnique(getParent().getChildrenItems(), name)) {
            throw new DuplicatedNameException("site.section.item.name.duplicated");
        }

        super.setName(name);
    }

    protected boolean isNameUnique(Collection<Item> siblings, MultiLanguageString name) {
        for (Item sibling : siblings) {
            if (sibling == this) {
                continue;
            }
            if (sibling.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public Section getSection() {
        return getParent();
    }

    public void setNextItem(Item item) {
        if (item != null) {
            setOrder(item.getOrder());
            item.setOrder(item.getOrder() + 1);
        } else {
            setOrder(getParent().getChildSet().size() + 1);
        }
    }

    /**
     * The item's title is visible unless the manager chooses to hide it or when
     * the item's sections has the same title as the item and the item has no
     * siblings.
     * 
     * @return <code>true</code> if the item's title is to be presented
     */
    public boolean isNameVisible() {
        Boolean show = getShowName();

        if (show != null && !show) {
            return false;
        }

        Section section = getParent();

        String sectionName = section.getName().getContent();
        String itemName = getName().getContent();

        if (!sectionName.equals(itemName)) {
            return true;
        }

        return section.getChildrenItemsCount() > 1;
    }

    @Deprecated
    public boolean hasEnabled() {
        return getEnabled() != null;
    }

    @Deprecated
    public boolean hasShowName() {
        return getShowName() != null;
    }

    public boolean isDeletable() {
        return true;
    }

    public Item getNextItem() {
        List<Item> others = getParent().getOrderedChildItems();
        int index = others.indexOf(this);
        return index == others.size() - 1 ? null : others.get(index + 1);
    }

    public void logCreateItemtoSection() {
        getOwnerSite().logCreateItemtoSection(this);
    }

    public void logEditItemtoSection() {
        getOwnerSite().logEditItemtoSection(this);
    }

    public void logEditItemPermission() {
        getOwnerSite().logEditItemPermission(this);
    }

}
