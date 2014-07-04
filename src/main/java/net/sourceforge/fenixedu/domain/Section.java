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
/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.cms.TemplatedSectionInstance;

import org.joda.time.DateTime;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    protected Section() {
        setCreationDate(new DateTime());
        setShowSubSections(true);
        setVisible(true);
    }

    protected Section(MultiLanguageString name) {
        this();
        setName(name);
    }

    public Section(Site site, MultiLanguageString name) {
        this(name);
        setSite(Objects.requireNonNull(site, "site"));
        setOrder(site.getAssociatedSectionSet().size() + 1);
    }

    public Section(Section parent, MultiLanguageString name) {
        this(name);
        setParent(Objects.requireNonNull(parent, "parent"));
        setOrder(parent.getChildSet().size() + 1);
    }

    public Section(Section parent, MultiLanguageString name, Integer sectionOrder) {
        this(parent, name);
        setOrder(sectionOrder);
    }

    public Collection<Section> getChildrenSections() {
        return FluentIterable.from(getChildSet()).filter(Section.class).toList();
    }

    public Collection<Item> getChildrenItems() {
        return FluentIterable.from(getChildSet()).filter(Item.class).toList();
    }

    public Collection<TemplatedSectionInstance> getChildrenTemplatedSections() {
        return FluentIterable.from(getChildSet()).filter(TemplatedSectionInstance.class).toList();
    }

    public int getChildrenItemsCount() {
        return FluentIterable.from(getChildSet()).filter(Item.class).size();
    }

    public void edit(MultiLanguageString name, Section nextSection) {
        setModificationDate(new DateTime());
        setName(name);
        setNextSection(nextSection);
        Site st = getOwnerSite();
        st.logEditSection(this);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException();
        }
        super.setName(name);
    }

    public void setNextSection(Section section) {
        List<CmsContent> ordered = getOrderedSiblings();

        ordered.remove(this);

        if (section != null) {
            ordered.add(ordered.indexOf(section), this);
        } else {
            ordered.add(this);
        }

        IntStream.range(0, ordered.size()).forEach(i -> ordered.get(i).setOrder(i));
    }


    private ArrayList<CmsContent> getOrderedSiblings() {
        return getSiblings().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
    }
    
    private Collection<? extends CmsContent> getSiblings() {
        return getSite() != null ? getSite().getAssociatedSectionSet() : getParent().getChildSet();
    }

    public void insertItem(MultiLanguageString itemName, MultiLanguageString itemInformation, Integer insertItemOrder,
            Boolean showName) {
        new Item(this, itemName, itemInformation, insertItemOrder, showName);
    }

    public void copyItemsFrom(Section sectionFrom) {
        for (final Item item : sectionFrom.getChildrenItems()) {
            this.insertItem(item.getName(), item.getBody(), item.getOrder(), item.getShowName());
        }
    }

    public void copySubSectionsAndItemsFrom(Section sectionFrom) {
        for (final Section subSectionFrom : sectionFrom.getChildrenSections()) {
            if (subSectionFrom.getSuperiorSection() != null) {
                Section subSectionTo =
                        this.getOwnerSite().createSection(subSectionFrom.getName(), this, subSectionFrom.getOrder());
                subSectionTo.copyItemsFrom(subSectionFrom);
                subSectionTo.copySubSectionsAndItemsFrom(subSectionFrom);
            }
        }
    }

    public List<Section> getOrderedSubSections() {
        return FluentIterable.from(getChildSet()).filter(Section.class).toSortedList(Ordering.natural());
    }

    public List<Item> getOrderedChildItems() {
        return FluentIterable.from(getChildSet()).filter(Item.class).toSortedList(Ordering.natural());
    }

    public List<CmsContent> getOrderedChildren() {
        return FluentIterable.from(getChildSet()).toSortedList(Ordering.natural());
    }

    public List<Section> getOrderedVisibleSubSections() {
        return FluentIterable.from(getChildSet()).filter(Section.class).filter(new Predicate<Section>() {
            @Override
            public boolean apply(Section input) {
                return input.getVisible();
            }
        }).toSortedList(Ordering.natural());
    }

    public boolean getShowSubSectionTree() {
        return getShowSubSections() != null && getShowSubSections() && !getOrderedVisibleSubSections().isEmpty();
    }

    /**
     * A section is always visible to the user as an entry in the menu. The
     * content of the section may not be available and that will be checked when
     * the user tries to access the section.
     */

    public boolean isVisible() {
        return getVisible();
    }

    public boolean isSubSectionAllowed() {
        return true;
    }

    public boolean isItemAllowed() {
        return true;
    }

    public Section getSuperiorSection() {
        return getParent();
    }

    public List<Section> getSubSections() {
        List<Section> sections = new ArrayList<Section>();
        for (Section section : getChildrenSections()) {
            sections.addAll(section.getSubSections());
        }
        return sections;
    }

    public boolean hasSuperiorSection() {
        return getSuperiorSection() != null;
    }

    public boolean isDeletable() {
        return true;
    }

    public Section getNextSection() {
        List<Section> others = FluentIterable.from(getSiblings()).filter(Section.class).toSortedList(Ordering.natural());
        int index = others.indexOf(this);
        return index == others.size() - 1 ? null : others.get(index + 1);
    }

    @Override
    public void delete() {
        setSite(null);
        for (CmsContent child : getChildSet()) {
            child.delete();
        }
        super.delete();
    }

    public Collection<? extends DomainObject> getEverythingForTree() {
        if (getFileContentSet().isEmpty()) {
            return getOrderedChildren();
        }
        List<DomainObject> objects = new ArrayList<DomainObject>(getOrderedChildren());
        objects.addAll(getFileContentSet());
        return objects;
    }

    @Override
    public Site getOwnerSite() {
        return getSite() != null ? getSite() : super.getOwnerSite();
    }

    public void logEditSectionPermission() {
        getOwnerSite().logEditSectionPermission(this);
    }

    @ConsistencyPredicate
    public boolean checkCorrectParent() {
        return !(getParent() != null && getSite() != null);
    }

    @Override
    public void applyStructureModifications(Section newParent, int order) {
        if (newParent == null) {
            setSite(getOwnerSite());
            setParent(null);
        } else {
            setParent(newParent);
        }
        setOrder(order);
    }

}
