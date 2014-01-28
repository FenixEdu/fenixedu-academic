package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DuplicatedNameException;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * An Item represents a piece of text a user can add to a section of a site. It
 * contains a title and a body text: the item's information.
 * 
 * @author ars
 */
public class Item extends Item_Base {

    public static final Comparator<Item> COMPARATOR_BY_ORDER = new Comparator<Item>() {

        @Override
        public int compare(Item o1, Item o2) {
            final int co = o1.getItemOrder().compareTo(o2.getItemOrder());
            if (co != 0) {
                return co;
            }
            final int cn = o1.getName().compareTo(o2.getName());
            if (cn != 0) {
                return cn;
            }
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }
    };

    protected Item() {
        super();
    }

    public Item(Section section, MultiLanguageString name) {
        this();

        if (section == null) {
            throw new NullPointerException();
        }

        setSection(section);
        setName(name);
    }

    private void setSection(Section section) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Section getSection() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Integer getItemOrder() {
        throw new UnsupportedOperationException("Not implemented");
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

        if (!isNameUnique(getSection().getAssociatedItems(), name)) {
            throw new DuplicatedNameException("site.section.item.name.duplicated");
        }

        super.setName(name);
    }

    protected boolean isNameUnique(List<Item> siblings, MultiLanguageString name) {
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

    public void setNextItem(Item item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Set<FileContent> getFileSet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isAvailable() {
        throw new UnsupportedOperationException("Not implemented");
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

        Section section = getSection();

        String sectionName = section.getName().getContent();
        String itemName = getName().getContent();

        if (!sectionName.equals(itemName)) {
            return true;
        }

        return section.getAssociatedItems().size() > 1;
    }

    public void delete() {
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasEnabled() {
        return getEnabled() != null;
    }

    @Deprecated
    public boolean hasShowName() {
        return getShowName() != null;
    }

    public String getReversePath() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isVisible() {
        throw new UnsupportedOperationException("Not implemented");
    }

}
