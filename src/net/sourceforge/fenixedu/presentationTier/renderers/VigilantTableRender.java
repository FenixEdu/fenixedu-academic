package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.comparators.ComparatorChain;

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

	public String getClasses() {
		return classes;
	}

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

	public String getStyle() {
		return style;
	}

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

		Schema schema = new Schema(Vigilant.class);

		schema.addSlotDescription(getSlot("teacherCategoryCode", "label.vigilancy.category.header"));
		schema.addSlotDescription(getSlot("person.username", "label.vigilancy.username"));
		schema.addSlotDescription(getSlot("person.name", "label.vigilancy.vigilant"));

		if (isShowPointsWeight()) {
			schema.addSlotDescription(getSlot("pointsWeight", "label.vigilancy.pointsWeight"));
		}

		if (isShowUnavailables()) {
			schema.addSlotDescription(getSlot("unavailablePeriodsAsString",
					"label.vigilancy.unavailablePeriodsShortLabel"));
		}
		if (isShowIncompatibilities()) {
			schema.addSlotDescription(getSlot("incompatiblePersonName",
					"label.vigilancy.displayIncompatibleInformation"));
		}
		if (isShowBoundsJustification()) {
			schema.addSlotDescription(getSlot("boundsAsString", "label.vigilancy.boundsJustification"));
		}

		if (isShowStartPoints()) {
			schema.addSlotDescription(getSlot("startPoints", "label.vigilancy.startPoints.header"));
		}

		schema.addSlotDescription(getSlot("points", "label.vigilancy.totalpoints.header"));

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
		chain.addComparator(Vigilant.CATEGORY_COMPARATOR);
		chain.addComparator(Vigilant.USERNAME_COMPARATOR);

		List<Vigilant> vigilants = new ArrayList<Vigilant>((Collection) object);
		Collections.sort(vigilants, chain);

		return new VigilantTableRenderLayout(vigilants);
	}

	private class VigilantTableRenderLayout extends TabularLayout {

		private ArrayList<Vigilant> vigilants;

		private ArrayList<MetaObject> objects;

		private Schema vigilantSchema;

		private Schema convokeSchema;

		private boolean empty;

		private Integer numberOfColumnsCache = null;

		private Integer numberOfConvokeSlotCache = null;

		private MetaObject vigilantWithConvokesMetaObjectCache = null;

		private MetaObject convokeMetaObjectCache = null;

		public VigilantTableRenderLayout(Collection<Vigilant> vigilants) {
			this.objects = new ArrayList<MetaObject>();
			this.vigilantSchema = getVigilantSchema();
			this.convokeSchema = translateSchema(getConvokeSchema());

			for (Vigilant vigilant : vigilants) {
				this.objects.add(MetaObjectFactory.createObject(vigilant, vigilantSchema));
			}

			this.vigilants = new ArrayList<Vigilant>(vigilants);
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

			if (columnIndex < one.getSlots().size()) {
				return null;
			} else {
				String convokeHeadPrefix = getConvokeTitle();
				int numberOfColumns = (this.getNumberOfColumns() - this.getNumberOfVigilantsSlots())
						/ this.getNumberOfConvokeSlots();
				int index = numberOfColumns - getConvokeIndex(one, columnIndex);

				return convokeHeadPrefix + " " + index;
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

		private List<Vigilancy> getConvokes(Vigilant vigilant) {
			List<Vigilancy> vigilancies;
			vigilancies = (showNotActiveConvokes) ? new ArrayList<Vigilancy>(vigilant.getVigilancies()) : vigilant.getActiveVigilancies();
			Collections.sort(vigilancies, Vigilancy.COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING);
			return vigilancies;
		}

		private MetaObject getConvokeMetaObjectToPutInTable(MetaObject vigilantMetaObject, int index) {
			Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();

			List<Vigilancy> convokes = getConvokes(vigilant);
			int size = convokes.size();
			int numberOfColumns = (this.getNumberOfColumns() - this.getNumberOfVigilantsSlots())
					/ this.getNumberOfConvokeSlots();

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
				List<Vigilancy> convokes = ((Vigilant) getVigilantWithConvokes().getObject()).getVigilancies();
				Vigilancy oneConvoke = convokes.get(0);

				convokeMetaObjectCache = MetaObjectFactory.createObject(oneConvoke, this.convokeSchema);
			}
			return convokeMetaObjectCache;
		}

		@Override
		protected boolean isHeader(int rowIndex, int columnIndex) {
			MetaObject one = getVigilantWithConvokes();
			if (columnIndex < one.getSlots().size()) {
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

			if (columnIndex < one.getSlots().size()) {
				return new HtmlText(one.getSlots().get(columnIndex).getLabel());
			} else {
				int index = getConvokeSlotIndex(one, columnIndex);
				MetaObject convokeMetaObject = getConvokeMetaObject(one);
				return (convokeMetaObject == null) ? findVigilantWithConvokesToGetLabels(index)
						: new HtmlText(getConvokeMetaObject(one).getSlots().get(index).getLabel());
			}
		}

		private MetaObject getVigilantWithConvokes() {
			if (vigilantWithConvokesMetaObjectCache != null) {
				return vigilantWithConvokesMetaObjectCache;
			} else {
				for (MetaObject vigilantMetaObject : this.objects) {
					Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();
					if (!getConvokes(vigilant).isEmpty())
						return vigilantWithConvokesMetaObjectCache = vigilantMetaObject;
				}
				return vigilantWithConvokesMetaObjectCache = getVigilantForRow(0);
			}
		}

		private HtmlText findVigilantWithConvokesToGetLabels(int index) {
			for (int i = 1; i < this.objects.size(); i++) {
				MetaObject object = getVigilantForRow(i);
				MetaObject convokeMetaObject = getConvokeMetaObject(object);
				if (convokeMetaObject != null)
					return new HtmlText(convokeMetaObject.getSlots().get(index).getLabel());
			}
			return new HtmlText("");
		}

		private int getNumberOfConvokeSlots() {
			if (numberOfConvokeSlotCache != null) {
				return numberOfConvokeSlotCache;
			} else {
				Vigilant vigilant = (Vigilant) this.getVigilantWithConvokes().getObject();
				List<Vigilancy> convokes = getConvokes(vigilant);
				if (convokes.isEmpty()) {
					return numberOfConvokeSlotCache = 0;
				} else {
					MetaObject convokeMetaObject = MetaObjectFactory.createObject(convokes.get(0),
							this.convokeSchema);
					return numberOfConvokeSlotCache = convokeMetaObject.getSlots().size();
				}
			}

		}

		private int getNumberOfVigilantsSlots() {
			MetaObject vigilantMetaObject = this.objects.get(0);
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
					Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();

					columns = Math.max(columns, vigilantMetaObject.getSlots().size() + convokesSlots
							* getConvokes(vigilant).size());
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
			return super.createComponent(object, type);
		}

		@Override
		public void applyStyle(HtmlComponent component) {
			if (this.empty) {
				component.setClasses(getEmptyMessageClasses());
			} else {
				super.applyStyle(component);
			}
		}

		@Override
		protected HtmlComponent getComponent(int rowIndex, int columnIndex) {

			MetaObject vigilant = getVigilantForRow(rowIndex);

			if (columnIndex < vigilant.getSlots().size()) {
				getContext().setMetaObject(vigilant);
				return renderSlot(vigilant.getSlots().get(columnIndex));
			} else {
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
				
				if(vigilancy.isOwnCourseVigilancy()) {
					component.setClasses(getOwnCourseClasses());
				}
				return component;
			}
		}
	}

	public String getOwnCourseClasses() {
		return ownCourseClasses;
	}

	public void setOwnCourseClasses(String ownCourseClasses) {
		this.ownCourseClasses = ownCourseClasses;
	}
}
