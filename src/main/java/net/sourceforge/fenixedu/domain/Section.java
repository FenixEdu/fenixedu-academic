/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Attachment;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    public static final Comparator<Section> COMPARATOR_BY_ORDER = new Comparator<Section>() {

        @Override
        public int compare(Section o1, Section o2) {
            final int co = o1.getSectionOrder().compareTo(o2.getSectionOrder());
            if (co != 0) {
                return co;
            }
            final int cn = o1.getName().compareTo(o2.getName());
            return cn == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : cn;
        }

    };

    protected Section() {
        super();

        setCreationDate(new DateTime());
        setShowSubSections(true);
        setRootDomainObject(Bennu.getInstance());
    }

    public Section(Container parent, MultiLanguageString name) {
        this();

        if (parent == null) {
            throw new NullPointerException();
        }

        setName(name);
        new ExplicitOrderNode(parent, this);
        setVisible(true);
    }

    public Section(Container parent, MultiLanguageString name, Integer sectionOrder) {
        this();
        setName(name);
        new ExplicitOrderNode(parent, this, sectionOrder);
        setVisible(true);
    }

    // TODO: check were lastModifiedDate is used and if this edit is really
    // required
    public void edit(MultiLanguageString name, Section nextSection, Group permittedGroup) {
        setModificationDate(new YearMonthDay());
        setName(name);
        setNextSection(nextSection);
        setPermittedGroup(permittedGroup);
        Site st = getSite();
        st.logEditSection(this);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException();
        }

        // NOTE: removed restriction because it introduces techinal problems
        // that are not really necessary or good for any of the parts: developer
        // and user

        // if (! isNameUnique(getSiblings(), name)) {
        // throw new DuplicatedNameException("site.section.name.duplicated");
        // }

        super.setName(name);
    }

    public List<Section> getSiblings() {
        return getSite().getAssociatedSections(getSuperiorSection());
    }

    /**
     * @return the section immediately after this section in the superior
     *         section or <code>null</code> if the section is the last
     */
    public Section getNextSection() {
        final ExplicitOrderNode node = getUniqueParentExplicitOrderNode();
        Integer order = node.getNodeOrder() + 1;
        Container container = node.getParent();
        List<Section> sections = new ArrayList<Section>(container.getOrderedChildren(Section.class));
        return order < sections.size() ? sections.get(order) : null;

    }

    /**
     * Changes the order of this section so that the given section is
     * immediately after this section. If the given section is <code>null</code> then this section will be the last
     * 
     * @param section
     *            the section that should appear after this section or <code>null</code> if this section should be last
     */
    public void setNextSection(Section section) {
        if (section != null) {
            setSectionOrder(section.getUniqueParentExplicitOrderNode().getNodeOrder());
        } else {
            Collection<Section> sections = getUniqueParentExplicitOrderNode().getParent().getOrderedChildren(Section.class);
            setSectionOrder(sections.size() - 1);
        }
    }

    public void insertItem(MultiLanguageString itemName, MultiLanguageString itemInformation, Integer insertItemOrder,
            Boolean showName) {
        new Item(this, itemName, itemInformation, insertItemOrder, showName);
    }

    public void copyItemsFrom(Section sectionFrom) {
        for (final Item item : sectionFrom.getAssociatedItems()) {
            this.insertItem(item.getName(), item.getBody(), item.getItemOrder(), item.getShowName());
        }
    }

    public void copySubSectionsAndItemsFrom(Section sectionFrom) {
        for (final Section subSectionFrom : sectionFrom.getAssociatedSections()) {
            if (subSectionFrom.getSuperiorSection() != null) {
                Section subSectionTo =
                        this.getSite().createSection(subSectionFrom.getName(), this, subSectionFrom.getSectionOrder());
                subSectionTo.copyItemsFrom(subSectionFrom);
                subSectionTo.copySubSectionsAndItemsFrom(subSectionFrom);
            }
        }
    }

    public SortedSet<Section> getOrderedSubSections() {
        final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
        sections.addAll(getAssociatedSections());
        return sections;
    }

    public SortedSet<Section> getOrderedVisibleSubSections() {
        final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
        for (Section section : getAssociatedSections()) {
            if (section.getVisible()) {
                sections.add(section);
            }
        }
        return sections;
    }

    public boolean getShowSubSectionTree() {
        return getShowSubSections() != null && getShowSubSections() && !getOrderedVisibleSubSections().isEmpty();
    }

    public SortedSet<Item> getOrderedItems() {
        final SortedSet<Item> items = new TreeSet<Item>(Item.COMPARATOR_BY_ORDER);
        items.addAll(getAssociatedItems());
        return items;
    }

    /**
     * A section is always visible to the user as an entry in the menu. The
     * content of the section may not be available and that will be checked when
     * the user tries to access the section.
     */

    public boolean isVisible(FunctionalityContext context) {
        return getVisible();
    }

    @Override
    public boolean isAvailable(FunctionalityContext context) {
        if (getSuperiorSection() != null && !getSuperiorSection().isAvailable(context)) {
            return false;
        }

        return super.isAvailable(context);
    }

    public boolean isSubSectionAllowed() {
        return true;
    }

    public boolean isItemAllowed() {
        return true;
    }

    public List<Item> getAssociatedItems() {
        return (List<Item>) getChildren(Item.class);
    }

    public List<Section> getAssociatedSections() {
        return (List<Section>) getChildren(Section.class);
    }

    public List<FileContent> getOrderedAssociatedFiles() {
        List<FileContent> contents = new ArrayList<FileContent>();
        for (Attachment attachment : getOrderedChildren(Attachment.class)) {
            contents.add(attachment.getFile());
        }
        return contents;
    }

    public List<FileContent> getAssociatedFiles() {
        List<FileContent> contents = new ArrayList<FileContent>();
        for (Attachment attachment : getChildren(Attachment.class)) {
            contents.add(attachment.getFile());
        }
        return contents;
    }

    public boolean hasAnyAssociatedSections() {
        return !getAssociatedSections().isEmpty();
    }

    public boolean hasAssociatedSections(Section section) {
        return getAssociatedSections().contains(section);
    }

    public int getAssociatedSectionsCount() {
        return getAssociatedSections().size();
    }

    public int getAssociatedItemsCount() {
        return getAssociatedItems().size();
    }

    private ExplicitOrderNode getUniqueParentExplicitOrderNode() {
        return (ExplicitOrderNode) getUniqueParentNode();
    }

    public Integer getSectionOrder() {
        return !hasAnyParents() ? null : getUniqueParentExplicitOrderNode().getNodeOrder();
    }

    public void setSectionOrder(Integer order) {
        getUniqueParentExplicitOrderNode().setNodeOrder(order);
    }

    public Section getSuperiorSection() {
        return !hasAnyParents() || !(getUniqueParentContainer() instanceof Section) ? null : (Section) getUniqueParentContainer();
    }

    public void removeSuperiorSection() {
        if (!hasAnyParents()) {
            getUniqueParentExplicitOrderNode().delete();
        }
    }

    public List<Section> getChildrenSections() {
        return (List<Section>) getChildren(Section.class);
    }

    public List<Attachment> getVisibleFiles() {
        List<Attachment> visibleFiles = new ArrayList<Attachment>();
        for (Attachment attachment : getOrderedChildrenFiles()) {
            if (attachment.getParentNode(this).isNodeVisible()) {
                visibleFiles.add(attachment);
            }
        }
        return visibleFiles;
    }

    public List<Attachment> getOrderedChildrenFiles() {
        return (List<Attachment>) getOrderedChildren(Attachment.class);
    }

    public List<Attachment> getChildrenFiles() {
        return (List<Attachment>) getChildren(Attachment.class);
    }

    public List<Section> getSubSections() {
        List<Section> sections = new ArrayList<Section>();
        for (Section section : getChildren(Section.class)) {
            sections.addAll(section.getSubSections());
        }
        return sections;
    }

    public boolean hasSuperiorSection() {
        return getSuperiorSection() != null;
    }

    public void setSuperiorSection(Section section) {
        getParents().clear();
        new ExplicitOrderNode(section, this);
    }

    private Node getSiteNode() {
        for (Node node : getParents()) {
            if (node.getParent() instanceof Site) {
                return node;
            }
            for (Section section : getParents(Section.class)) {
                Node siteNode = section.getSiteNode();
                if (siteNode != null) {
                    return siteNode;
                }
            }
        }
        return null;
    }

    public Site getSite() {
        Node node = getSiteNode();
        return (node == null) ? null : (Site) node.getParent();
    }

    public void setSite(Site site) {
        Node node = getSiteNode();
        if (node != null) {
            node.setParent(site);
        } else {
            new ExplicitOrderNode(site, this);
        }

    }

    public void removeSite() {
        Node node = getSiteNode();
        final Content child = node.getChild();
        node.delete();
        if (!child.hasAnyParents()) {
            child.delete();
        }
    }

    public void setFirstParent(Container container) {
        container.addChild(this);
    }

    @Override
    public boolean isAbleToSpecifyInitialContent() {
        return true;
    }

    public void setVisible(Boolean visible) {
        getUniqueParentExplicitOrderNode().setVisible(visible);
    }

    public Boolean getVisible() {
        return getUniqueParentExplicitOrderNode().getVisible();
    }

    @Override
    public boolean hasInitialContent() {
        return super.getInitialContent() != null;
    }

    @Override
    public String getPath() {
        return (isContainerMaximizable() && getInitialContent() != null) ? getInitialContent().getPath() : null;
    }

    public Collection<FunctionalityCall> getAssociatedFunctionalities() {
        return getChildren(FunctionalityCall.class);
    }

    @Override
    protected Node createChildNode(Content childContent) {
        if (getSite() != null) {
            if (childContent instanceof Attachment) {
                getSite().logSectionInsertFile(childContent, this);
            } else {
                getSite().logSectionInsertInstitutional(childContent, this);
            }
        }
        return new ExplicitOrderNode(this, childContent);
    }

    @Override
    protected void disconnectContent() {
        if (getSite() != null) {
            getSite().logRemoveSection(this);
        }
        super.disconnectContent();
    }

    @Override
    public void logAddFile(Attachment attachment) {
        if (getSite() != null) {
            getSite().logAddFile(attachment);
        }
    }

    public void logEditSectionPermission() {
        getSite().logEditSectionPermission(this);
    }

    @Deprecated
    public boolean hasEnabled() {
        return getEnabled() != null;
    }

    @Deprecated
    public boolean hasShowSubSections() {
        return getShowSubSections() != null;
    }

    @Deprecated
    public boolean hasModificationDate() {
        return getModificationDate() != null;
    }

}
