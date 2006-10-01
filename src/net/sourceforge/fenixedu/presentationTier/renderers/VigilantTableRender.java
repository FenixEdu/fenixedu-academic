package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits;
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

public class VigilantTableRender extends OutputRenderer {

    private String convoke;

    private String convokeTitleKey;

    private String convokeTitleBundle;

    private String classes;

    private String style;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    private String convokesToShow;

    private String sortBy;

    private String emptyMessageKey;

    private String emptyMessageBundle;

    private String emptyMessageClasses;

    private boolean showIncompatibilities = Boolean.FALSE;

    private boolean showUnavailables = Boolean.FALSE;
    
    private boolean showBoundsJustification = Boolean.FALSE;

    private boolean showStartPoints = Boolean.FALSE;
    
    private boolean showNotActiveConvokes = Boolean.FALSE;
    
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

    public String getConvokesToShow() {
        return convokesToShow;
    }

    public void setConvokesToShow(String convokesToShow) {
        this.convokesToShow = convokesToShow;
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

		schema.addSlotDescription(getSlot("teacherCategoryCode","label.vigilancy.category.header"));
		schema.addSlotDescription(getSlot("person.username","label.vigilancy.username"));
		schema.addSlotDescription(getSlot("person.name","label.vigilancy.vigilant"));
		
		if(isShowUnavailables()) {
			schema.addSlotDescription(getSlot("unavailablePeriodsAsString", "label.vigilancy.unavailablePeriodsShortLabel"));
		}
		if (isShowIncompatibilities()) {
			schema.addSlotDescription(getSlot("incompatiblePersonName","label.vigilancy.displayIncompatibleInformation"));
		}
		if(isShowBoundsJustification()) {
			schema.addSlotDescription(getSlot("boundsAsString","label.vigilancy.boundsJustification"));
		}
	
		if(isShowStartPoints()) {
			schema.addSlotDescription(getSlot("startPoints","label.vigilancy.startPoints.header"));
		}
		
		schema.addSlotDescription(getSlot("points","label.vigilancy.totalpoints.header"));
    
		return schema;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        Collection sortedCollection = RenderUtils.sortCollectionWithCriteria((Collection) object,
                getSortBy());

        return new VigilantTableRenderLayout(sortedCollection);
    }		

    private class VigilantTableRenderLayout extends TabularLayout {

        private ArrayList<Vigilant> vigilants;

        private ArrayList<MetaObject> objects;

        private Schema vigilantSchema;

        private Schema convokeSchema;

        private boolean empty;

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
            return thereIsAtLeastOneConvoke();
        }

        private boolean thereIsAtLeastOneConvoke() {
            for (Vigilant vigilant : vigilants) {
                if (getConvokes(vigilant).size() > 0)
                    return true;
            }
            return false;
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
            String message = RenderUtils
                    .getResourceString(getConvokeTitleBundle(), getConvokeTitleKey());

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

        private List<VigilancyWithCredits> getConvokes(Vigilant vigilant) {
            if (getConvokesToShow().equals("past")) {
                return (showNotActiveConvokes) ? vigilant.getConvokesBeforeCurrentDate() : vigilant.getActiveConvokesBeforeCurrentDate();
            }
            if (getConvokesToShow().equals("future")) {
                return (showNotActiveConvokes) ? vigilant.getConvokesAfterCurrentDate() : vigilant.getActiveConvokesAfterCurrentDate();
            }

            else {
                return (showNotActiveConvokes) ? vigilant.getVigilancyWithCredits() : vigilant.getActiveVigilancyWithCredits();
            }
        }

        private MetaObject getConvokeMetaObjectToPutInTable(MetaObject vigilantMetaObject, int index) {
            Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();

            List<VigilancyWithCredits> convokes = getConvokes(vigilant);
            int size = convokes.size();
            int numberOfColumns = (this.getNumberOfColumns() - this.getNumberOfVigilantsSlots())
                    / this.getNumberOfConvokeSlots();

            if (numberOfColumns - size <= index) {
            	VigilancyWithCredits oneConvoke = convokes.get(numberOfColumns - index - 1);

                MetaObject convokeMetaObject = MetaObjectFactory.createObject(oneConvoke,
                        this.convokeSchema);

                return convokeMetaObject;
            } else {
                return null;
            }
        }

        private MetaObject getConvokeMetaObject(MetaObject vigilantMetaObject, int index) {
            Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();

            List<VigilancyWithCredits> convokes = getConvokes(vigilant);
            VigilancyWithCredits oneConvoke = convokes.get(0);
            MetaObject convokeMetaObject = MetaObjectFactory
                    .createObject(oneConvoke, this.convokeSchema);

            return convokeMetaObject;

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
                MetaObject convokeMetaObject = getConvokeMetaObject(one, 0);
                return (convokeMetaObject == null) ? findVigilantWithConvokesToGetLabels(index)
                        : new HtmlText(getConvokeMetaObject(one, 0).getSlots().get(index).getLabel());
            }
        }

        private MetaObject getVigilantWithConvokes() {
            for (MetaObject vigilantMetaObject : this.objects) {
                Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();
                if (!getConvokes(vigilant).isEmpty())
                    return vigilantMetaObject;
            }
            return getVigilantForRow(0);
        }

        private HtmlText findVigilantWithConvokesToGetLabels(int index) {
            for (int i = 1; i < this.objects.size(); i++) {
                MetaObject object = getVigilantForRow(i);
                MetaObject convokeMetaObject = getConvokeMetaObject(object, 0);
                if (convokeMetaObject != null)
                    return new HtmlText(convokeMetaObject.getSlots().get(index).getLabel());
            }
            return new HtmlText("");
        }

        private int getNumberOfConvokeSlots() {
            Vigilant vigilant = (Vigilant) this.getVigilantWithConvokes().getObject();
            MetaObject convokeMetaObject = MetaObjectFactory.createObject(getConvokes(vigilant).get(0),
                    this.convokeSchema);
            return convokeMetaObject.getSlots().size();

        }

        private int getNumberOfVigilantsSlots() {
            MetaObject vigilantMetaObject = this.objects.get(0);
            return vigilantMetaObject.getSlots().size();
        }

        @Override
        protected int getNumberOfColumns() {
            int columns = -1;
            int convokesSlots = getNumberOfConvokeSlots();

            for (MetaObject vigilantMetaObject : this.objects) {
                Vigilant vigilant = (Vigilant) vigilantMetaObject.getObject();

                columns = Math.max(columns, vigilantMetaObject.getSlots().size() + convokesSlots
                        * getConvokes(vigilant).size());
            }

            return columns;
        }

        @Override
        protected int getNumberOfRows() {
            for (Vigilant vigilant : this.vigilants) {
                if (!getConvokes(vigilant).isEmpty())
                    return this.vigilants.size();
            }
            return 0; // no vigilants have convokes for the given period we
            // will not display anyone.
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Collection vigilants = (Collection) object;
            HtmlComponent component;

            if (getEmptyMessageKey() != null && (vigilants.isEmpty() || !thereIsAtLeastOneConvoke())) {
                component = new HtmlText(RenderUtils.getResourceString(getEmptyMessageBundle(),
                        getEmptyMessageKey()));
                this.empty = true;
            } else {
                component = super.createComponent(object, type);
                this.empty = false;
            }
            return component;
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
                return renderSlot(convoke.getSlots().get(slotIndex));

            }

        }

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

}
