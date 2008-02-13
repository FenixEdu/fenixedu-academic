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
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * An Item represents a piece of text a user can add to a section of a site. It
 * contains a title and a body text: the item's information.
 * 
 * @author ars
 */
public class Item extends Item_Base {

    public static final Comparator<Item> COMPARATOR_BY_ORDER = new Comparator<Item>() {

	private ComparatorChain chain = null;

	public int compare(Item one, Item other) {
	    if (this.chain == null) {
		chain = new ComparatorChain();

		chain.addComparator(new BeanComparator("itemOrder"));
		chain.addComparator(new BeanComparator("name"));
		chain.addComparator(DomainObject.COMPARATOR_BY_ID);
	    }

	    return chain.compare(one, other);
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

    public Item(Section section, MultiLanguageString name, MultiLanguageString information, Integer itemOrder) {
	this(section, name);

	setBody(information);
    }

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
     * @return the item immediately after this item in the section or
     *         <code>null</code> if the item is the last
     */
    public Item getNextItem() {
	List<Item> items = (List<Item>) getParents().get(0).getParent().getChildren(Item.class);
	Integer order = getItemOrder() + 1;
	return order < items.size() ? items.get(order) : null;
    }

    /**
     * Changes the order of this item so that the given item is immediately
     * after this item. If the given item is <code>null</code> then this item
     * will be the last in the section
     * 
     * @param item
     *                the item that should appear after this item or
     *                <code>null</code> if this item should be last
     */
    public void setNextItem(Item item) {
	if (item != null) {
	    setItemOrder(((ExplicitOrderNode) item.getParents().get(0)).getNodeOrder());
	} else {
	    List<Item> items = (List<Item>) getParents().get(0).getParent().getChildren(Item.class);
	    setItemOrder(items.size() - 1);
	}
    }

    public Collection<FileItem> getSortedVisibleFileItems() {
	final List<FileItem> sortedFileItems = new ArrayList<FileItem>();

	for (Node node : getOrderedChildrenNodes(Attachment.class)) {
	    if (node.isVisible()) {
		sortedFileItems.add(((Attachment) node.getChild()).getFileItem());
	    }
	}

	return sortedFileItems;
    }

    public Collection<FileItem> getSortedFileItems() {
	final List<FileItem> sortedFileItems = new ArrayList<FileItem>();

	for (Attachment attachment : getOrderedChildren(Attachment.class)) {
	    sortedFileItems.add(attachment.getFileItem());
	}

	return sortedFileItems;
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

    public void setFileItemsOrder(List<FileItem> files) {

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
	getParents().get(0).delete();
    }

    public Section getSection() {
	return (Section) getParents().get(0).getParent();
    }

    public Integer getItemOrder() {
	return ((ExplicitOrderNode) getParents().get(0)).getNodeOrder();
    }

    public void setItemOrder(Integer nodeOrder) {
	((ExplicitOrderNode) getParents().get(0)).setNodeOrder(nodeOrder);
    }

    public void setVisible(Boolean visible) {
	getParents().get(0).setVisible(visible);
    }

    public Boolean getVisible() {
	return getParents().get(0).getVisible();
    }

    @Override
    protected Node createChildNode(Content childContent) {
	return new ExplicitOrderNode(this, childContent, Boolean.TRUE);
    }

    public void addFileItem(final FileItem fileItem) {
	addChild(new Attachment(fileItem));
    }

    public Collection<FileItem> getFileItems() {
	Collection<FileItem> result = new ArrayList<FileItem>();
	for (Attachment attachment : getChildren(Attachment.class)) {
	    result.add(attachment.getFileItem());
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
}
