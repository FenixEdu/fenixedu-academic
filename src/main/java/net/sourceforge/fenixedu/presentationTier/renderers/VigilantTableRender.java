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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.AttendingStatus;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.layouts.TabularLayout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.schemas.SchemaSlotDescription;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import com.google.common.base.Predicate;

public class VigilantTableRender extends OutputRenderer {

    private String convoke;

    private String convokeTitleKey;

    private String convokeTitleBundle;

    private String classes;

    private String style;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    private String sortBy;

    private String emptyMessageKey;

    private String emptyMessageBundle;

    private String emptyMessageClasses;

    private String deletedConvokeClasses;

    private String ownCourseClasses;

    private boolean showIncompatibilities = Boolean.FALSE;

    private boolean showUnavailables = Boolean.FALSE;

    private boolean showBoundsJustification = Boolean.FALSE;

    private boolean showStartPoints = Boolean.FALSE;

    private boolean showNotActiveConvokes = Boolean.FALSE;

    private boolean showPointsWeight = Boolean.FALSE;

    private boolean showOwnVigilancies = Boolean.FALSE;

    private String warningClass;

    private VigilantGroup group;

    public String getWarningClass() {
        return warningClass;
    }

    public void setWarningClass(String warningClass) {
        this.warningClass = warningClass;
    }

    public VigilantGroup getGroup() {
        return group;
    }

    public void setGroup(VigilantGroup group) {
        this.group = group;
    }

    public boolean isShowOwnVigilancies() {
        return showOwnVigilancies;
    }

    public void setShowOwnVigilancies(boolean showOwnVigilancies) {
        this.showOwnVigilancies = showOwnVigilancies;
    }

    public boolean isShowPointsWeight() {
        return showPointsWeight;
    }

    public void setShowPointsWeight(boolean showPointsWeight) {
        this.showPointsWeight = showPointsWeight;
    }

    public String getEmptyMessageClasses() {
        return emptyMessageClasses;
    }

    public void setEmptyMessageClasses(String emptyMessageClasses) {
        this.emptyMessageClasses = emptyMessageClasses;
    }

    public String getEmptyMessageBundle() {
        return emptyMessageBundle;
    }

    public void setEmptyMessageBundle(String emptyMessageBundle) {
        this.emptyMessageBundle = emptyMessageBundle;
    }

    public String getEmptyMessageKey() {
        return emptyMessageKey;
    }

    public void setEmptyMessageKey(String emptyMessageKey) {
        this.emptyMessageKey = emptyMessageKey;
    }

    public String getConvokeSchema() {
        return convoke;
    }

    public void setConvokeSchema(String convoke) {
        this.convoke = convoke;
    }

    public String getConvokeTitleBundle() {
        return convokeTitleBundle;
    }

    public void setConvokeTitleBundle(String convokeTitleBundle) {
        this.convokeTitleBundle = convokeTitleBundle;
    }

    public String getConvokeTitleKey() {
        return convokeTitleKey;
    }

    public void setConvokeTitleKey(String convokeTitleKey) {
        this.convokeTitleKey = convokeTitleKey;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getConvoke() {
        return convoke;
    }

    public void setConvoke(String convoke) {
        this.convoke = convoke;
    }

    public String getHeaderClasses() {
        return headerClasses;
    }

    public void setHeaderClasses(String headerClasses) {
        this.headerClasses = headerClasses;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    private Schema translateSchema(String name) {
        return RenderKit.getInstance().findSchema(name);
    }

    public boolean isShowBoundsJustification() {
        return showBoundsJustification;
    }

    public void setShowBoundsJustification(boolean showBoundsJustification) {
        this.showBoundsJustification = showBoundsJustification;
    }

    public boolean isShowIncompatibilities() {
        return showIncompatibilities;
    }

    public void setShowIncompatibilities(boolean showIncompatibilities) {
        this.showIncompatibilities = showIncompatibilities;
    }

    public boolean isShowStartPoints() {
        return showStartPoints;
    }

    public void setShowStartPoints(boolean showStartPoints) {
        this.showStartPoints = showStartPoints;
    }

    public boolean isShowUnavailables() {
        return showUnavailables;
    }

    public void setShowUnavailables(boolean showUnavailables) {
        this.showUnavailables = showUnavailables;
    }

    private SchemaSlotDescription getSlot(String slot, String key) {
        SchemaSlotDescription slotDescription = new SchemaSlotDescription(slot);
        slotDescription.setKey(key);
        slotDescription.setBundle("VIGILANCY_RESOURCES");

        return slotDescription;
    }

    private Schema getVigilantSchema() {

        Schema schema = new Schema(VigilantWrapper.class);

        schema.addSlotDescription(getSlot("teacherCategoryCode", "label.vigilancy.category.header"));
        schema.addSlotDescription(getSlot("person.username", "label.vigilancy.username"));
        schema.addSlotDescription(getSlot("person.name", "label.vigilancy.vigilant"));

        if (isShowPointsWeight()) {
            schema.addSlotDescription(getSlot("pointsWeight", "label.vigilancy.pointsWeight"));
        }

        if (isShowStartPoints()) {
            schema.addSlotDescription(getSlot("startPoints", "label.vigilancy.startPoints.header"));
        }

        schema.addSlotDescription(getSlot("points", "label.vigilancy.totalpoints.header"));

        if (isShowUnavailables()) {
            schema.addSlotDescription(getSlot("unavailablePeriodsAsString", "label.vigilancy.unavailablePeriodsShortLabel"));
        }
        if (isShowIncompatibilities()) {
            schema.addSlotDescription(getSlot("incompatiblePersonName", "label.vigilancy.displayIncompatibleInformation"));
        }
        if (isShowBoundsJustification()) {
            schema.addSlotDescription(getSlot("justificationforNotConvokable", "label.vigilancy.boundsJustification"));
        }

        return schema;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isShowNotActiveConvokes() {
        return showNotActiveConvokes;
    }

    public void setShowNotActiveConvokes(boolean showNotActiveConvokes) {
        this.showNotActiveConvokes = showNotActiveConvokes;
    }

    public String getDeletedConvokeClasses() {
        return deletedConvokeClasses;
    }

    public void setDeletedConvokeClasses(String deletedConvokeClasses) {
        this.deletedConvokeClasses = deletedConvokeClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
        chain.addComparator(VigilantWrapper.USERNAME_COMPARATOR);

        this.group = (VigilantGroup) object;
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>(this.group.getVigilantWrappers());

        Collections.sort(vigilants, chain);

        return new VigilantTableRenderLayout(vigilants);
    }

    private class VigilantTableRenderLayout extends TabularLayout {

        private final ArrayList<VigilantWrapper> vigilants;

        private final ArrayList<MetaObject> objects;

        private final Schema vigilantSchema;

        private final Schema convokeSchema;

        private boolean empty;

        private Integer numberOfColumnsCache = null;

        private Integer numberOfConvokeSlotCache = null;

        private MetaObject vigilantWithConvokesMetaObjectCache = null;

        private MetaObject convokeMetaObjectCache = null;

        public VigilantTableRenderLayout(Collection<VigilantWrapper> vigilants) {
            this.objects = new ArrayList<MetaObject>();
            this.vigilantSchema = getVigilantSchema();
            this.convokeSchema = translateSchema(getConvokeSchema());

            for (VigilantWrapper vigilant : vigilants) {
                this.objects.add(MetaObjectFactory.createObject(vigilant, vigilantSchema));
            }

            this.vigilants = new ArrayList<VigilantWrapper>(vigilants);
        }

        @Override
        protected boolean hasHeader() {
            return true;
        }

        @Override
        protected boolean hasHeaderGroups() {
            return true;
        }

        private MetaObject getVigilantForRow(int rowIndex) {
            return this.objects.get(rowIndex);
        }

        @Override
        protected String getHeaderGroup(int columnIndex) {
            MetaObject one = getVigilantWithConvokes();

            if (isInformationColumn(columnIndex, one)) {
                return null;
            } else if (isVigilancy(columnIndex, one)) {
                String convokeHeadPrefix = getConvokeTitle();
                int numberOfColumns =
                        (this.getNumberOfColumns() - this.getNumberOfVigilantsSlots()) / this.getNumberOfConvokeSlots();
                int index = numberOfColumns - getConvokeIndex(one, columnIndex);

                return convokeHeadPrefix + " " + index;
            } else {
                return null;
            }
        }

        private String getConvokeTitle() {
            String message = RenderUtils.getResourceString(getConvokeTitleBundle(), getConvokeTitleKey());

            if (message == null) {
                return getConvokeTitleKey();
            } else {
                return message;
            }
        }

        private int getConvokeIndex(MetaObject metaObject, int column) {
            column = column - metaObject.getSlots().size();

            return column / getNumberOfConvokeSlots();
        }

        private int getConvokeSlotIndex(MetaObject vigilantMetaObject, int columnIndex) {
            columnIndex = columnIndex - vigilantMetaObject.getSlots().size();

            return columnIndex % getNumberOfConvokeSlots();
        }

        private List<Vigilancy> getConvokes(VigilantWrapper vigilant) {
            List<Vigilancy> vigilancies;
            if (showNotActiveConvokes) {
                if (showOwnVigilancies) {
                    vigilancies = new ArrayList<Vigilancy>(vigilant.getVigilanciesSet());
                } else {
                    vigilancies = vigilant.getOtherCourseVigilancies();
                }
            } else {
                if (showOwnVigilancies) {
                    vigilancies = vigilant.getActiveVigilancies();
                } else {
                    vigilancies = vigilant.getActiveOtherCourseVigilancies();
                }
            }
            Collections.sort(vigilancies, Vigilancy.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
            return vigilancies;
        }

        private MetaObject getConvokeMetaObjectToPutInTable(MetaObject vigilantMetaObject, int index) {
            VigilantWrapper vigilant = (VigilantWrapper) vigilantMetaObject.getObject();

            List<Vigilancy> convokes = getConvokes(vigilant);
            int size = convokes.size();
            int numberOfColumns = (this.getNumberOfColumns() - this.getNumberOfVigilantsSlots()) / this.getNumberOfConvokeSlots();

            if (numberOfColumns - size <= index) {
                Vigilancy oneConvoke = convokes.get(numberOfColumns - index - 1);

                MetaObject convokeMetaObject = MetaObjectFactory.createObject(oneConvoke, this.convokeSchema);

                return convokeMetaObject;
            } else {
                return null;
            }
        }

        private MetaObject getConvokeMetaObject(MetaObject vigilantMetaObject) {

            if (convokeMetaObjectCache == null) {
                Collection<Vigilancy> convokes = ((VigilantWrapper) getVigilantWithConvokes().getObject()).getVigilanciesSet();
                Vigilancy oneConvoke = convokes.iterator().next();

                convokeMetaObjectCache = MetaObjectFactory.createObject(oneConvoke, this.convokeSchema);
            }
            return convokeMetaObjectCache;
        }

        @Override
        protected boolean isHeader(int rowIndex, int columnIndex) {
            MetaObject one = getVigilantWithConvokes();
            if (isInformationColumn(columnIndex, one)) {
                return false;
            } else {
                // we are now asking if a column of a convoke is an header, so
                // let's
                // check if it's partial index is 0
                return getConvokeSlotIndex(one, columnIndex) == 0;
            }
        }

        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            MetaObject one = getVigilantWithConvokes();

            if (isInformationColumn(columnIndex, one)) {
                return new HtmlText(one.getSlots().get(columnIndex).getLabel());
            } else if (isVigilancy(columnIndex, one)) {
                int index = getConvokeSlotIndex(one, columnIndex);
                MetaObject convokeMetaObject = getConvokeMetaObject(one);
                return (convokeMetaObject == null) ? findVigilantWithConvokesToGetLabels(index) : new HtmlText(
                        getConvokeMetaObject(one).getSlots().get(index).getLabel());
            } else {
                return new HtmlText(RenderUtils.getResourceString(one.getSlots().iterator().next().getBundle(),
                        "label.avgVigilancies"));
            }

        }

        private boolean isInformationColumn(int columnIndex, MetaObject one) {
            return columnIndex < one.getSlots().size();
        }

        private MetaObject getVigilantWithConvokes() {
            if (vigilantWithConvokesMetaObjectCache != null) {
                return vigilantWithConvokesMetaObjectCache;
            } else {
                for (MetaObject vigilantMetaObject : this.objects) {
                    VigilantWrapper vigilant = (VigilantWrapper) vigilantMetaObject.getObject();
                    if (!getConvokes(vigilant).isEmpty()) {
                        return vigilantWithConvokesMetaObjectCache = vigilantMetaObject;
                    }
                }
                return vigilantWithConvokesMetaObjectCache = getVigilantForRow(0);
            }
        }

        private HtmlText findVigilantWithConvokesToGetLabels(int index) {
            for (int i = 1; i < this.objects.size(); i++) {
                MetaObject object = getVigilantForRow(i);
                MetaObject convokeMetaObject = getConvokeMetaObject(object);
                if (convokeMetaObject != null) {
                    return new HtmlText(convokeMetaObject.getSlots().get(index).getLabel());
                }
            }
            return new HtmlText("");
        }

        private int getNumberOfConvokeSlots() {
            if (numberOfConvokeSlotCache != null) {
                return numberOfConvokeSlotCache;
            } else {
                VigilantWrapper vigilant = (VigilantWrapper) this.getVigilantWithConvokes().getObject();
                List<Vigilancy> convokes = getConvokes(vigilant);
                if (convokes.isEmpty()) {
                    return numberOfConvokeSlotCache = 0;
                } else {
                    MetaObject convokeMetaObject = MetaObjectFactory.createObject(convokes.iterator().next(), this.convokeSchema);
                    return numberOfConvokeSlotCache = convokeMetaObject.getSlots().size();
                }
            }

        }

        private int getNumberOfVigilantsSlots() {
            MetaObject vigilantMetaObject = this.objects.iterator().next();
            return vigilantMetaObject.getSlots().size();
        }

        @Override
        protected int getNumberOfColumns() {
            if (numberOfColumnsCache != null) {
                return numberOfColumnsCache;
            } else {
                int columns = -1;
                int convokesSlots = getNumberOfConvokeSlots();

                for (MetaObject vigilantMetaObject : this.objects) {
                    VigilantWrapper vigilant = (VigilantWrapper) vigilantMetaObject.getObject();

                    columns =
                            Math.max(columns, vigilantMetaObject.getSlots().size() + convokesSlots * getConvokes(vigilant).size());
                }
                return numberOfColumnsCache = columns;
            }
        }

        @Override
        protected int getNumberOfRows() {
            return this.vigilants.size();
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {

            HtmlBlockContainer block = new HtmlBlockContainer();

            if (group.getVigilants().size() != 0) {
                block.addChild(super.createComponent(object, type));

                NumberFormat formatter = new DecimalFormat("##0.0");
                String avg = formatter.format(group.getPointsAverage());

                block.addChild(new HtmlText("<b>" + RenderUtils.getResourceString("VIGILANCY_RESOURCES", "label.avgVigilancies")
                        + ":</b> " + avg, false));
            } else {
                block.addChild(new HtmlText("<b>"
                        + RenderUtils.getResourceString("VIGILANCY_RESOURCES", "label.vigilancy.noVigilantsInGroup") + "</b>",
                        false));
            }
            return block;
        }

        @Override
        public void applyStyle(HtmlComponent component) {
            if (this.empty) {
                component.setClasses(getEmptyMessageClasses());
            } else {
                HtmlBlockContainer block = (HtmlBlockContainer) component;
                if (block.getChildren().size() > 1) {
                    super.applyStyle(block.getChild(new Predicate<HtmlComponent>() {
                        @Override
                        public boolean apply(HtmlComponent input) {
                            return input instanceof HtmlTable;
                        }
                    }));
                }
            }
        }

        @Override
        protected void costumizeCell(HtmlTableCell cell, int rowIndex, int columnIndex) {
            MetaObject metavigilant = getVigilantForRow(rowIndex);

            if (!isInformationColumn(columnIndex, metavigilant)) {

                int index = getConvokeIndex(metavigilant, columnIndex);
                MetaObject convoke = getConvokeMetaObjectToPutInTable(metavigilant, index);

                if (convoke != null) {
                    Vigilancy v = (Vigilancy) convoke.getObject();

                    if (v.getStatus() == AttendingStatus.NOT_ATTENDED && v.getWrittenEvaluation().getEndDateTime().isBeforeNow()) {
                        cell.setClasses(getWarningClass());
                    }

                    if (!v.getWrittenEvaluation().getVigilantsReport() && v.getWrittenEvaluation().getEndDateTime().isBeforeNow()) {
                        cell.setClasses(getWarningClass());
                    }
                }
            }
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            MetaObject vigilant = getVigilantForRow(rowIndex);

            if (isInformationColumn(columnIndex, vigilant)) {
                getContext().setMetaObject(vigilant);

                return renderSlot(vigilant.getSlots().get(columnIndex));
            } else {// if (isVigilancy(columnIndex, vigilant)) {
                int index = getConvokeIndex(vigilant, columnIndex);
                MetaObject convoke = getConvokeMetaObjectToPutInTable(vigilant, index);

                if (convoke == null) {
                    return new HtmlText();
                }

                int slotIndex = getConvokeSlotIndex(vigilant, columnIndex);

                getContext().setMetaObject(convoke);
                HtmlComponent component = renderSlot(convoke.getSlots().get(slotIndex));

                Vigilancy vigilancy = (Vigilancy) convoke.getObject();
                if (!vigilancy.isActive()) {
                    component.setClasses(getDeletedConvokeClasses());
                }

                if (vigilancy.isOwnCourseVigilancy()) {
                    component.setClasses(getOwnCourseClasses());
                }
                return component;
            }
        }

        private boolean isVigilancy(int columnIndex, MetaObject vigilant) {
            return columnIndex >= vigilant.getSlots().size();
        }
    }

    public String getOwnCourseClasses() {
        return ownCourseClasses;
    }

    public void setOwnCourseClasses(String ownCourseClasses) {
        this.ownCourseClasses = ownCourseClasses;
    }
}
