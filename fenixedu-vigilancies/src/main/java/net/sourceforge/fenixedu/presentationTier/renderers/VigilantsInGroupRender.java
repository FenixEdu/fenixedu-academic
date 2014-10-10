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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantBoundBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.layouts.TabularLayout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectCollection;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class VigilantsInGroupRender extends InputRenderer {

    private String personSchema;

    private String labelStyle;

    public String getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getPersonSchema() {
        return personSchema;
    }

    public void setPersonSchema(String personSchema) {
        this.personSchema = personSchema;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {

        List<VigilantBoundBean> bounds = new ArrayList<VigilantBoundBean>((Collection) object);

        // Collections.sort(bounds,new OrderComparator());
        return new BoundTableLayout(bounds);

    }

    private class BoundTableLayout extends TabularLayout {

        private final List<MetaObject> metaObjects;

        private final List<MetaObject> personMetaObjects;

        private final List<VigilantGroup> groups;

        private final Schema personSchema;

        public BoundTableLayout(List<VigilantBoundBean> bounds) {

            this.groups = new ArrayList<VigilantGroup>();
            this.metaObjects = new ArrayList<MetaObject>();
            this.personMetaObjects = new ArrayList<MetaObject>();
            personSchema = RenderKit.getInstance().findSchema(getPersonSchema());

            for (VigilantBoundBean bound : bounds) {
                metaObjects.add(MetaObjectFactory
                        .createObject(bound, RenderKit.getInstance().findSchema("editVigilantBoundBean")));
                Person person = bound.getPerson();
                if (!isInMetaObjects(person)) {
                    personMetaObjects.add(MetaObjectFactory.createObject(bound.getPerson(), personSchema));
                }
                VigilantGroup group = bound.getVigilantGroup();
                if (!groups.contains(group)) {
                    groups.add(group);
                }
            }
            Collections.sort(personMetaObjects, new OrderComparator());
        }

        private boolean isInMetaObjects(Person person) {
            for (MetaObject metaObject : this.personMetaObjects) {
                Person onePerson = (Person) metaObject.getObject();
                if (onePerson.equals(person)) {
                    return true;
                }
            }
            return false;
        }

        private MetaObject getMetaObject(Person person, VigilantGroup group) {
            for (MetaObject metaObject : ((MetaObjectCollection) getInputContext().getMetaObject()).getAllMetaObjects()) {
                Person onePerson = ((VigilantBoundBean) metaObject.getObject()).getPerson();
                VigilantGroup oneGroup = ((VigilantBoundBean) metaObject.getObject()).getVigilantGroup();
                if (onePerson.equals(person) && oneGroup.equals(group)) {
                    return metaObject;
                }
            }
            return null;
        }

        @Override
        protected boolean hasHeader() {
            return true;
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {

            List<MetaSlot> slots = this.personMetaObjects.get(rowIndex).getSlots();
            if (columnIndex < slots.size()) {
                MetaSlot slot = slots.get(columnIndex);
                slot.setReadOnly(true);
                return renderSlot(slot);
            } else {

                HtmlContainer container = new HtmlInlineContainer();

                HtmlCheckBox checkBox = new HtmlCheckBox();

                Person person = (Person) this.personMetaObjects.get(rowIndex).getObject();
                VigilantGroup group = this.groups.get(columnIndex - slots.size());
                MetaObject objectToBound = getMetaObject(person, group);
                checkBox.bind(objectToBound.getSlot("bounded"));
                VigilantBoundBean bean = (VigilantBoundBean) objectToBound.getObject();
                checkBox.setChecked(bean.isBounded());
                checkBox.setTitle(bean.getVigilantGroup().getName());

                container.addChild(checkBox);

                /*
                 * VigilantBound bound = null; if (bean.isBounded()) { bound =
                 * bean.getVigilantGroup().getBounds(
                 * bean.getPerson().getCurrentVigilant()); }
                 */
                if (bean.isBounded()) {
                    if (bean.getVigilantGroup().equals(group)) {
                        VigilantWrapper vigilantWrapper = bean.getVigilantGroup().hasPerson(bean.getPerson());
                        HtmlText text =
                                new HtmlText(
                                        RenderUtils
                                                .getResourceString(
                                                        "VIGILANCY_RESOURCES",
                                                        ((vigilantWrapper.getConvokable())) ? "label.vigilancy.convokable.abbr" : "label.vigilancy.notConvokable.abbr"),
                                        false);
                        if (getLabelStyle() != null) {
                            text.setStyle(getLabelStyle());
                        }

                        container.addChild(text);
                    }
                }
                return container;
            }
        }

        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            List<MetaSlot> slots = this.personMetaObjects.iterator().next().getSlots();
            if (columnIndex < slots.size()) {
                return new HtmlText(slots.get(columnIndex).getLabel());
            } else {
                return new HtmlText(this.groups.get(columnIndex - slots.size()).getName());
            }

        }

        @Override
        protected int getNumberOfColumns() {
            return (this.metaObjects == null || this.metaObjects.size() == 0) ? 0 : this.metaObjects.iterator().next().getSlots()
                    .size()
                    + groups.size();
        }

        @Override
        protected int getNumberOfRows() {
            return personMetaObjects.size();
        }

    }

    private class CategoryComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {

            Person p1 = (Person) o1;
            Person p2 = (Person) o2;

            Employee e1 = p1.getEmployee();
            Employee e2 = p2.getEmployee();

            if (e1 == null && e2 == null) {
                return 0;
            }
            if (e1 == null) {
                return -1;
            }
            if (e2 == null) {
                return 1;
            }

            Teacher t1 = e1.getPerson().getTeacher();
            Teacher t2 = e2.getPerson().getTeacher();

            ProfessionalCategory c1 = (t1 != null) ? t1.getCategory() : null;
            ProfessionalCategory c2 = (t2 != null) ? t2.getCategory() : null;

            if (c1 == null && c2 == null) {
                return 0;
            }
            if (c1 == null) {
                return -1;
            }
            if (c2 == null) {
                return 1;
            }

            return -c1.compareTo(c2);
        }

    }

    private class OrderComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {

            Person p1 = (Person) ((MetaObject) o1).getObject();
            Person p2 = (Person) ((MetaObject) o2).getObject();
            ComparatorChain chain = new ComparatorChain();
            chain.addComparator(new CategoryComparator());
            chain.addComparator(new ReverseComparator(new BeanComparator("username")));

            return chain.compare(p1, p2);
        }

    }
}
