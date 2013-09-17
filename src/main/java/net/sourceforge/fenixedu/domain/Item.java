package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.contents.Attachment;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DuplicatedNameException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import pt.ist.fenixframework.FenixFramework;
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

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Item(Section section, MultiLanguageString name) {
        this();

        if (section == null) {
            throw new NullPointerException();
        }

        new ExplicitOrderNode(section, this);
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

        if (!isNameUnique(getSection().getAssociatedItems(), name)) {
            throw new DuplicatedNameException("site.section.item.name.duplicated");
        }

        super.setName(name);
    }

    /**
     * @return the item immediately after this item in the section or <code>null</code> if the item is the last
     */
    public Item getNextItem() {
        Item result = null;

        if (hasAnyParents()) {
            final List<Item> items = (List<Item>) getUniqueParentContainer().getOrderedChildren(Item.class);
            final Integer order = items.indexOf(this) + 1;
            result = order < items.size() ? items.get(order) : null;
        }

        return result;
    }

    /**
     * Changes the order of this item so that the given item is immediately
     * after this item. If the given item is <code>null</code> then this item
     * will be the last in the section
     * 
     * @param item
     *            the item that should appear after this item or <code>null</code> if this item should be last
     */
    public void setNextItem(Item item) {
        if (item != null) {
            setItemOrder(item.getUniqueParentExplicitOrderNode().getNodeOrder());
        } else if (hasAnyParents()) {
            List<Item> items = (List<Item>) getUniqueParentContainer().getChildren(Item.class);
            setItemOrder(items.size() - 1);
        }
    }

    public Collection<FileContent> getSortedVisibleFileItems() {
        final List<FileContent> sortedFiles = new ArrayList<FileContent>();

        for (Node node : getOrderedChildrenNodes(Attachment.class)) {
            if (node.isNodeVisible()) {
                sortedFiles.add(((Attachment) node.getChild()).getFile());
            }
        }

        return sortedFiles;
    }

    public Collection<FileContent> getSortedFileItems() {
        final List<FileContent> sortedFiles = new ArrayList<FileContent>();

        for (Attachment attachment : getOrderedChildren(Attachment.class)) {
            sortedFiles.add(attachment.getFile());
        }

        return sortedFiles;
    }

    public Collection<Node> getSortedAttachmentNodes() {
        return getOrderedChildrenNodes(Attachment.class);
    }

    @Override
    public boolean isAvailable(FunctionalityContext context) {
        if (getSection() != null && !getSection().isAvailable(context)) {
            return false;
        }

        return super.isAvailable(context);
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

    public void removeSection() {
        if (hasAnyParents()) {
            getUniqueParentExplicitOrderNode().delete();
        }
    }

    public Section getSection() {
        return (Section) getUniqueParentContainer();
    }

    private ExplicitOrderNode getUniqueParentExplicitOrderNode() {
        return (ExplicitOrderNode) getUniqueParentNode();
    }

    public Integer getItemOrder() {
        return getUniqueParentExplicitOrderNode().getNodeOrder();
    }

    public void setItemOrder(Integer nodeOrder) {
        getUniqueParentExplicitOrderNode().setNodeOrder(nodeOrder);
    }

    public void setVisible(Boolean visible) {
        getUniqueParentExplicitOrderNode().setVisible(visible);
    }

    public Boolean getVisible() {
        return getUniqueParentExplicitOrderNode().getVisible();
    }

    @Override
    protected Node createChildNode(Content childContent) {
        final Section section = getSection();
        final Site site = section.getSite();
        site.logAddFileToItem(this, section, childContent);
        return new ExplicitOrderNode(this, childContent, Boolean.TRUE);
    }

    public Collection<FileContent> getFileItems() {
        Collection<FileContent> result = new ArrayList<FileContent>();
        for (Attachment attachment : getChildren(Attachment.class)) {
            result.add(attachment.getFile());
        }
        return result;
    }

    @Override
    public boolean isChildAccepted(Content child) {
        return child instanceof Attachment;
    }

    @Override
    public Content getInitialContent() {
        return null;
    }

    public void logCreateItemtoSection() {
        final Site site = getSection().getSite();
        site.logCreateItemtoSection(this);
    }

    public void logEditItemtoSection() {
        final Site site = getSection().getSite();
        site.logEditItemtoSection(this);
    }

    @Override
    protected void disconnectContent() {
        Section section = getSection();
        if (section != null) {
            Site site = section.getSite();
            if (site != null) {
                site.logDeleteItemtoSection(this);
            }
        }
        super.disconnectContent();
    }

    public void logEditItemPermission() {
        final Site site = getSection().getSite();
        site.logEditItemPermission(this);
    }

    @Deprecated
    public boolean hasEnabled() {
        return getEnabled() != null;
    }

    @Deprecated
    public boolean hasShowName() {
        return getShowName() != null;
    }

}
