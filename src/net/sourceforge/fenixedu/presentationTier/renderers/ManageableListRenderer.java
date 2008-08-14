package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlActionLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlActionLinkController;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.layouts.TabularLayout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderMode;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * This renderer allows you get a list as input but delegates part of the list's
 * management to an external action. Each one of the slot's values will be
 * presented in an html list. The renderer manages the removal of values from
 * the list. So a delete action link will appear next to each object's
 * presentation. An add action link will allow the user to start the external
 * <em>use-case</em> that will add a new value to the list.
 * 
 * <p>
 * Example:
 * <ul>
 * <li><em>&lt;Object A presentation &gt;</em> <a href="#">Remove</a>
 * <li><em>&lt;Object B presentation &gt;</em> <a href="#">Remove</a>
 * <li><em>&lt;Object C presentation &gt;</em> <a href="#">Remove</a>
 * </ul>
 * <a href="#">Add</a>
 * 
 * @author cfgi
 */
public class ManageableListRenderer extends InputRenderer {

    public static final String MANAGED_SLOT_NAME = ManageableListRenderer.class.getName() + "/slot/name";
    public static final String MANAGED_SLOT_KEY = ManageableListRenderer.class.getName() + "/slot/key";

    private String destination;

    private String eachSchema;

    private String eachLayout;

    public String getEachLayout() {
	return this.eachLayout;
    }

    /**
     * The layout to be used when presenting each object.
     * 
     * @property
     */
    public void setEachLayout(String eachLayout) {
	this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
	return this.eachSchema;
    }

    /**
     * The schema to be used when presenting each object.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
	this.eachSchema = eachSchema;
    }

    public String getDestination() {
	return this.destination;
    }

    /**
     * The destination for were control will be forwarded whn pressing the add
     * button. The destiny action is responsible for keeping the current
     * <tt>viewstate</tt> during the interaction. The action is also responsible
     * for changing the viewstate to reflect the intended changes, that is,
     * adding a new value to the managed list, before sending the control back
     * to the inicial page.
     * 
     * <p>
     * The action can obtain a representation of the viewstate suitable for
     * including in a hidden field by doing:
     * 
     * <pre>
     * IViewState viewState = RenderUtils.getViewState();
     * String encoded = ViewState.encodeToBase64(viewState);
     * </pre>
     * 
     * Retrieving the viewstate from it's encoded state and preparing before
     * sending control to the inicial page can be done with:
     * 
     * <pre>
     *   IViewState viewState = ViewState.decodeFromBase64(&lt;encoded viewstate&gt;);  
     *   viewState.setUser(UserIdentityFactory.create(request)); 
     *   // update slot in viewstate
     *   RenderUtils.setViewState(viewState);
     * </pre>
     * 
     * Updating the viewstate consists of adding a new value to the slot's. This
     * must only be done through the meta-object contained in the viewstate:
     * 
     * <pre>
     *   MetaSlot slot = &lt;find slot in viewState.getMetaObject().getSlots()&gt;;
     *   List values = new ArrayList((List) slot.getObject());
     *   values.add(&lt;the new value&gt;);
     *   slot.setObject(values);
     * </pre>
     * 
     * The slot that was beeing managed is available in viewstate attributes.
     * You can get information about the slot with:
     * 
     * <pre>
     * String slotName = viewState.getAttribute(ManageableListRenderer.MANAGED_SLOT_NAME);
     * MetaSlotKey slotKey = viewState.getAttribute(ManageableListRenderer.MANAGED_SLOT_KEY);
     * </pre>
     * 
     * @property
     */
    public void setDestination(String destination) {
	this.destination = destination;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	MetaObject metaObject = getInputContext().getMetaObject();

	return new ManageableListLayout((Collection) metaObject.getObject());
    }

    private class ManageableListLayout extends TabularLayout {

	private List objects;
	private HtmlMultipleHiddenField hiddenValues;

	public ManageableListLayout(Collection collection) {
	    this.objects = new ArrayList(collection);

	    this.hiddenValues = new HtmlMultipleHiddenField();
	    this.hiddenValues.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());

	    // HACK: severe dependecy with fénix project
	    this.hiddenValues.setConverter(new DomainObjectKeyArrayConverter());
	}

	@Override
	protected int getNumberOfColumns() {
	    return 2;
	}

	@Override
	protected int getNumberOfRows() {
	    return this.objects == null ? 0 : this.objects.size();
	}

	@Override
	protected HtmlComponent getHeaderComponent(int columnIndex) {
	    return null;
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlComponent component = super.createComponent(object, type);

	    HtmlContainer container = new HtmlBlockContainer();

	    container.addChild(component);
	    container.addChild(this.hiddenValues);

	    HtmlActionLink link = new HtmlActionLink();
	    link.setName(getInputContext().getMetaObject().getKey().toString() + "/add");
	    link.setText(RenderUtils.getResourceString("renderers.list.management.add"));
	    link.setController(new FollowDestinationController((MetaSlot) getInputContext().getMetaObject()));

	    container.addChild(link);

	    return container;
	}

	@Override
	public void applyStyle(HtmlComponent component) {
	    HtmlContainer container = (HtmlContainer) component;

	    super.applyStyle(container.getChildren().get(0));
	}

	@Override
	protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
	    Object object = this.objects.get(rowIndex);

	    Schema schema = RenderKit.getInstance().findSchema(getEachSchema());
	    String layout = getEachLayout();

	    MetaObject metaObject = MetaObjectFactory.createObject(object, schema);

	    if (columnIndex == 0) {
		this.hiddenValues.addValue(metaObject.getKey().toString());

		PresentationContext newContext = getContext().createSubContext(metaObject);
		newContext.setLayout(layout);
		newContext.setRenderMode(RenderMode.getMode("output"));

		RenderKit kit = RenderKit.getInstance();
		return kit.render(newContext, object);
	    } else {
		HtmlActionLink link = new HtmlActionLink();

		String prefix = getInputContext().getMetaObject().getKey().toString();
		link.setName(prefix + "/delete/" + rowIndex);
		link.setText(RenderUtils.getResourceString("renderers.list.management.delete"));

		HtmlTableRow row = getTable().getRows().get(rowIndex);
		link.setController(new RemoveLineController(getTable(), this.hiddenValues, row, prefix));
		return link;
	    }

	}

    }

    class RemoveLineController extends HtmlActionLinkController {

	private HtmlTable table;
	private HtmlTableRow row;
	private HtmlMultipleHiddenField values;
	private String prefix;

	public RemoveLineController(HtmlTable table, HtmlMultipleHiddenField values, HtmlTableRow row, String prefix) {
	    this.table = table;
	    this.values = values;
	    this.row = row;
	    this.prefix = prefix;
	}

	@Override
	public void linkPressed(IViewState viewState, HtmlActionLink link) {
	    int index = this.table.getRows().indexOf(row);

	    this.table.removeRow(this.row);
	    this.values.removeValue(index);

	    renameLinks();
	}

	private void renameLinks() {
	    List<HtmlComponent> links = this.table.getChildren(new Predicate() {

		public boolean evaluate(Object object) {
		    return object instanceof HtmlActionLink;
		}

	    });

	    int pos = 0;
	    for (HtmlComponent component : links) {
		((HtmlActionLink) component).setName(this.prefix + "/delete/" + pos++);
	    }
	}

    }

    class FollowDestinationController extends HtmlActionLinkController {

	private MetaSlot slot;

	public FollowDestinationController(MetaSlot slot) {
	    this.slot = slot;
	}

	@Override
	public void linkPressed(IViewState viewState, HtmlActionLink link) {
	    if (getDestination() != null) {
		viewState.setAttribute(ManageableListRenderer.MANAGED_SLOT_NAME, slot.getName());
		viewState.setAttribute(ManageableListRenderer.MANAGED_SLOT_KEY, slot.getKey());

		viewState.setCurrentDestination(getDestination());
	    }
	}

    }
}
