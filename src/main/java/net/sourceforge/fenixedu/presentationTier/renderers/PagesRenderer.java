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

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.CollectionRenderer;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlSubmitButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlSubmitButtonController;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderMode;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.DomainObject;

public class PagesRenderer extends InputRenderer {

    private String subSchema;

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

        List<? extends DomainObject> objects = getPageObjects(bean.getObjects());

        Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
        MetaObject listMetaObject = MetaObjectFactory.createObject(objects, schema);

        PresentationContext context = getContext().createSubContext(listMetaObject);
        context.setRenderMode(RenderMode.getMode("output"));

        HtmlTable table = (HtmlTable) RenderKit.getInstance().renderUsing(getRenderer(), context, objects, objects.getClass());
        return decorateTable(table, objects);
    }

    private List<? extends DomainObject> getPageObjects(List<? extends DomainObject> objects) {
        if (!this.pagedValue) {
            return getPageContainerBean().getAllObjects();
        }
        return getPageContainerBean().getPageByPageSize(getDefaultObjectsPerPage());
    }

    private HtmlComponent decorateTable(HtmlTable table, List<? extends DomainObject> objects) {
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

        if (table.getHeader() != null) {
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
        if (this.pagedValue && getPageContainerBean().getNumberOfPages(getDefaultObjectsPerPage()) > 0) {
            if (getPageContainerBean().hasPreviousPage(getDefaultObjectsPerPage())) {
                HtmlSubmitButton previousButton = new HtmlSubmitButton();
                previousButton.setText(RenderUtils.getResourceString(getBundle(), "pages.button.previous"));
                previousButton.setName(getLocalName(previousName));
                previousButton.setController(new PreviousController(page));
                container.addChild(previousButton);
            }

            container.addChild(new HtmlText(getPageContainerBean().getPage() + " / "
                    + getPageContainerBean().getNumberOfPages(getDefaultObjectsPerPage())));

            if (getPageContainerBean().hasNextPage(getDefaultObjectsPerPage())) {
                HtmlSubmitButton nextButton = new HtmlSubmitButton();
                nextButton.setText(RenderUtils.getResourceString(getBundle(), "pages.button.next"));
                nextButton.setName(getLocalName(nextName));
                nextButton.setController(new NextController(page));
                container.addChild(nextButton);
            }
        }
    }

    private String getButtonText() {
        if (isKey()) {
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
            } else {
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
            } else {
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

    @Override
    public String getClasses() {
        return this.renderer.getClasses();
    }

    @Override
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

    @Override
    public String getStyle() {
        return this.renderer.getStyle();
    }

    @Override
    public void setStyle(String style) {
        this.renderer.setStyle(style);
    }

    public String getObjectsPerPage() {
        return objectsPerPage;
    }

    public void setObjectsPerPage(String objectsPerPage) {
        this.objectsPerPage = objectsPerPage;
        if (objectsPerPage != null && objectsPerPage.length() != 0) {
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
