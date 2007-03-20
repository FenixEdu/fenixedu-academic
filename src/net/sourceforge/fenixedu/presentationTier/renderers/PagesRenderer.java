package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.CollectionRenderer;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlSubmitButton;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlSubmitButtonController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class PagesRenderer extends InputRenderer {
    
    private String subSchema;
    
    private String sortBy;
    
    private String objectsPerPage;
    
    private String paged;
    
    private boolean pagedValue = true;
    
    private int defaultObjectsPerPage = 100;
    
    private String buttonLabel;
    
    private boolean key = true;
    
    private String bundle;

    
    private CollectionRenderer renderer;
    
    private PageContainerBean pageContainerBean;
    
    public PagesRenderer() {
	super();
	this.renderer = new CollectionRenderer();
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
	PageContainerBean bean = (PageContainerBean) object;
	setPageContainerBean(bean);
	
	List<DomainObject> objects = getPageObjects(bean.getObjects());
	
	Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
	MetaObject listMetaObject = MetaObjectFactory.createObject(objects, schema);
	
	PresentationContext context = getContext().createSubContext(listMetaObject);
	context.setRenderMode(RenderMode.getMode("output"));
	
	HtmlTable table = (HtmlTable) RenderKit.getInstance().renderUsing(getRenderer(), context, objects, objects.getClass());
	return decorateTable(table, objects);
    }

    private List<DomainObject> getPageObjects(List<DomainObject> objects) {
	if(!this.pagedValue) {
	    return getPageContainerBean().getAllObjects();
	}
	return getPageContainerBean().getPageByPageSize(getDefaultObjectsPerPage());
    }

    private HtmlComponent decorateTable(HtmlTable table, List<DomainObject> objects) {
	MetaObject bean = getContext().getMetaObject();
	
	HtmlHiddenField page = new HtmlHiddenField();
	HtmlHiddenField selected = new HtmlHiddenField();

	page.setValue(getCurrentPage());
	
	page.bind(bean, "page");
	selected.bind(bean, "selected");
	selected.setConverter(new DomainObjectKeyConverter());
	
	HtmlContainer container = new HtmlBlockContainer();
	container.addChild(page);
	container.addChild(selected);
	
	addPreviousNextButtons(container, page, "previous", "next");
	
	container.addChild(table);
	
	if(table.getHeader() != null) {
	    for (HtmlTableRow row : table.getHeader().getRows()) {
		row.createCell("");
	    }
	}
	
	String buttonText = getButtonText();
	int i = 0;
	for (HtmlTableRow row : table.getRows()) {
	    HtmlTableCell cell = row.createCell();
	    HtmlSubmitButton htmlSubmitButton = new HtmlSubmitButton();
	    htmlSubmitButton.setName(getLocalName("select" + i));
	    htmlSubmitButton.setText(buttonText);
	    htmlSubmitButton.setController(new ButtonsController(MetaObjectFactory.createObject(objects.get(i), null), selected));
	    cell.setBody(htmlSubmitButton);
	    i++;
	}

	addPreviousNextButtons(container, page, "previousAfter", "nextAfter");
		
	return container;
    }

    private void addPreviousNextButtons(HtmlContainer container, HtmlHiddenField page, String previousName, String nextName) {
	if(this.pagedValue && getPageContainerBean().getNumberOfPages(getDefaultObjectsPerPage()) > 0) {
	    if(getPageContainerBean().hasPreviousPage(getDefaultObjectsPerPage())) {
		HtmlSubmitButton previousButton = new HtmlSubmitButton();
		previousButton.setText(RenderUtils.getResourceString(getBundle(), "button.previous"));
		previousButton.setName(getLocalName(previousName));
		previousButton.setController(new PreviousController(page));
		container.addChild(previousButton);
	    }

	    container.addChild(new HtmlText(getPageContainerBean().getPage() + " / " + getPageContainerBean().getNumberOfPages(getDefaultObjectsPerPage())));
	    
	    if(getPageContainerBean().hasNextPage(getDefaultObjectsPerPage())) {
		HtmlSubmitButton nextButton = new HtmlSubmitButton();
		nextButton.setText(RenderUtils.getResourceString(getBundle(), "button.next"));
		nextButton.setName(getLocalName(nextName));
		nextButton.setController(new NextController(page));
		container.addChild(nextButton);
	    }
	}
    }

    private String getButtonText() {
	if(isKey()) {
	    return RenderUtils.getResourceString(getBundle(), getButtonLabel());
	} else {
	    return getButtonLabel();
	}
    }

    private String getLocalName(String string) {
	return getContext().getViewState().getId() + "renderers.page." + string;
    }

    public static class PreviousController extends HtmlSubmitButtonController {

	private HtmlSimpleValueComponent component;
	
	public PreviousController(HtmlSimpleValueComponent component) {
	    super();
	    
	    this.component = component;
	}

	@Override
	protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
	    viewState.setSkipUpdate(false);
	    
	    ViewDestination destination = viewState.getDestination("input");
	    if (destination == null) {
		destination = viewState.getInputDestination();
	    }
	    
	    viewState.setCurrentDestination(destination);


	    Integer previous = getCurrentValue() - 1;
	    this.component.setValue(previous.toString());
	}
	
	public int getCurrentValue() {
	    String value = this.component.getValue();
	    
	    if (value == null) {
		return 0;
	    }
	    else {
		return Integer.parseInt(value);
	    }
	}
    }
    
    public static class NextController extends HtmlSubmitButtonController {

	private HtmlSimpleValueComponent component;
	
	public NextController(HtmlSimpleValueComponent component) {
	    super();
	    this.component = component;
	}

	@Override
	protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
	    viewState.setSkipUpdate(false);
	    
	    ViewDestination destination = viewState.getDestination("input");
	    if (destination == null) {
		destination = viewState.getInputDestination();
	    }
	    
	    viewState.setCurrentDestination(destination);

	    Integer next = getCurrentValue() + 1;
	    this.component.setValue(next.toString());
	}
	
	public int getCurrentValue() {
	    String value = this.component.getValue();
	    
	    if (value == null) {
		return 0;
	    }
	    else {
		return Integer.parseInt(value);
	    }
	}
    }
    
    public static class ButtonsController extends HtmlSubmitButtonController {
	
	private HtmlSimpleValueComponent component;
	private MetaObject metaObject;

	public ButtonsController(MetaObject metaObject, HtmlSimpleValueComponent component) {
	    this.component = component;
	    this.metaObject = metaObject;
	}
	
	@Override
	protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
	    viewState.setSkipUpdate(false);
	    this.component.setValue(this.metaObject.getKey().toString());
	}
	
    }

    private String getCurrentPage() {
	MetaObject metaObject = getContext().getMetaObject();
	PageContainerBean bean = (PageContainerBean) metaObject.getObject();
	
	return bean.getPage() == null ? null : bean.getPage().toString(); 
    }


    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }
    
    private Schema translateSchema(String name) {
	return RenderKit.getInstance().findSchema(name);
    }

    public String getClasses() {
        return this.renderer.getClasses();
    }

    public void setClasses(String classes) {
        this.renderer.setClasses(classes);
    }

    public String getColumnClasses() {
        return this.renderer.getColumnClasses();
    }

    public void setColumnClasses(String columnClasses) {
        this.renderer.setColumnClasses(columnClasses);
    }

    public String getHeaderClasses() {
        return this.renderer.getHeaderClasses();
    }

    public void setHeaderClasses(String headerClasses) {
        this.renderer.setHeaderClasses(headerClasses);
    }

    public String getRowClasses() {
        return this.renderer.getRowClasses();
    }

    public void setRowClasses(String rowClasses) {
        this.renderer.setRowClasses(rowClasses);
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getStyle() {
        return this.renderer.getStyle();
    }

    public void setStyle(String style) {
        this.renderer.setStyle(style);
    }

    public String getObjectsPerPage() {
        return objectsPerPage;
    }

    public void setObjectsPerPage(String objectsPerPage) {
        this.objectsPerPage = objectsPerPage;
        if(objectsPerPage != null && objectsPerPage.length() != 0) {
            defaultObjectsPerPage = Integer.valueOf(objectsPerPage).intValue();
        }
    }
    
    private int getDefaultObjectsPerPage() {
	return defaultObjectsPerPage;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return null;
    }

    public String getPaged() {
        return paged;
    }

    public void setPaged(String paged) {
        this.paged = paged;
        this.pagedValue = Boolean.valueOf(getPaged()).booleanValue();
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public PageContainerBean getPageContainerBean() {
        return pageContainerBean;
    }

    public void setPageContainerBean(PageContainerBean pageContainerBean) {
        this.pageContainerBean = pageContainerBean;
    }

    public CollectionRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(CollectionRenderer renderer) {
        this.renderer = renderer;
    }
    

}
