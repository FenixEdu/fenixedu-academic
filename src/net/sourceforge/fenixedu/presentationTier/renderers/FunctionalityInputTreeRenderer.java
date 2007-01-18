package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.presentationTier.renderers.TreeRenderer.LevelDecorator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonGroup;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public class FunctionalityInputTreeRenderer extends InputRenderer {

    private FunctionalitiesTreeRenderer treeRenderer = new FunctionalitiesTreeRenderer();

    public String getClasses() {
        return this.treeRenderer.getClasses();
    }

    public String getClassesFor(String type) {
        return this.treeRenderer.getClassesFor(type);
    }

    public String getEachLayout() {
        return this.treeRenderer.getEachLayout();
    }

    public String getEachSchema() {
        return this.treeRenderer.getEachSchema();
    }

    public String getHiddenLinks() {
        return this.treeRenderer.getHiddenLinks();
    }

    public String getHiddenLinksFor(String type) {
        return this.treeRenderer.getHiddenLinksFor(type);
    }

    public String getImage() {
        return this.treeRenderer.getImage();
    }

    public String getImageFor(String type) {
        return this.treeRenderer.getImageFor(type);
    }

    public String getItemClass() {
        return this.treeRenderer.getItemClass();
    }

    public String getItemStyle() {
        return this.treeRenderer.getItemStyle();
    }

    public String getLayoutFor(String type) {
        return this.treeRenderer.getLayoutFor(type);
    }

    public String getLinks() {
        return this.treeRenderer.getLinks();
    }

    public String getLinksClasses() {
        return this.treeRenderer.getLinksClasses();
    }

    public String getLinksFor(String type) {
        return this.treeRenderer.getLinksFor(type);
    }

    public String getLinksStyle() {
        return this.treeRenderer.getLinksStyle();
    }

    public String getListClass() {
        return this.treeRenderer.getListClass();
    }

    public String getListStyle() {
        return this.treeRenderer.getListStyle();
    }

    public String getStyle() {
        return this.treeRenderer.getStyle();
    }

    public boolean isIncludeImage() {
        return this.treeRenderer.isIncludeImage();
    }

    public boolean isModuleRelative() {
        return this.treeRenderer.isModuleRelative();
    }

    public void setClasses(String classes) {
        this.treeRenderer.setClasses(classes);
    }

    public void setClassesFor(String type, String method) {
        this.treeRenderer.setClassesFor(type, method);
    }

    public void setEachLayout(String eachLayout) {
        this.treeRenderer.setEachLayout(eachLayout);
    }

    public void setEachSchema(String eachSchema) {
        this.treeRenderer.setEachSchema(eachSchema);
    }

    public void setHiddenLinks(String hiddenlinks) {
        this.treeRenderer.setHiddenLinks(hiddenlinks);
    }

    public void setHiddenLinksFor(String type, String method) {
        this.treeRenderer.setHiddenLinksFor(type, method);
    }

    public void setImage(String image) {
        this.treeRenderer.setImage(image);
    }

    public void setImageFor(String type, String method) {
        this.treeRenderer.setImageFor(type, method);
    }

    public void setIncludeImage(boolean includeImage) {
        this.treeRenderer.setIncludeImage(includeImage);
    }

    public void setItemClass(String itemClass) {
        this.treeRenderer.setItemClass(itemClass);
    }

    public void setItemStyle(String itemStyle) {
        this.treeRenderer.setItemStyle(itemStyle);
    }

    public void setLayoutFor(String type, String layout) {
        this.treeRenderer.setLayoutFor(type, layout);
    }

    public void setLinks(String links) {
        this.treeRenderer.setLinks(links);
    }

    public void setLinksClasses(String linksClass) {
        this.treeRenderer.setLinksClasses(linksClass);
    }

    public void setLinksFor(String type, String method) {
        this.treeRenderer.setLinksFor(type, method);
    }

    public void setLinksStyle(String linksStyle) {
        this.treeRenderer.setLinksStyle(linksStyle);
    }

    public void setListClass(String listClass) {
        this.treeRenderer.setListClass(listClass);
    }

    public void setListStyle(String listStyle) {
        this.treeRenderer.setListStyle(listStyle);
    }

    public void setModuleRelative(boolean moduleRelative) {
        this.treeRenderer.setModuleRelative(moduleRelative);
    }

    public void setSchemaFor(String type, String schema) {
        this.treeRenderer.setSchemaFor(type, schema);
    }

    public void setStyle(String style) {
        this.treeRenderer.setStyle(style);
    }

    public void setTitle(String title) {
        this.treeRenderer.setTitle(title);
    }

    public String getDisabledClass() {
        return this.treeRenderer.getDisabledClass();
    }

    public String getEnabledClass() {
        return this.treeRenderer.getEnabledClass();
    }

    public void setDisabledClass(String disabledClass) {
        this.treeRenderer.setDisabledClass(disabledClass);
    }

    public void setEnabledClass(String enabledClass) {
        this.treeRenderer.setEnabledClass(enabledClass);
    }

    public String getFunctionalityDisabledImage() {
        return this.treeRenderer.getFunctionalityDisabledImage();
    }

    public String getFunctionalityEnabledImage() {
        return this.treeRenderer.getFunctionalityEnabledImage();
    }

    public String getModuleDisabledImage() {
        return this.treeRenderer.getModuleDisabledImage();
    }

    public String getModuleEnabledImage() {
        return this.treeRenderer.getModuleEnabledImage();
    }

    public void setFunctionalityDisabledImage(String functionalityDisabledImage) {
        this.treeRenderer.setFunctionalityDisabledImage(functionalityDisabledImage);
    }

    public void setFunctionalityEnabledImage(String functionalityEnabledImage) {
        this.treeRenderer.setFunctionalityEnabledImage(functionalityEnabledImage);
    }

    public void setModuleDisabledImage(String moduleDisabledImage) {
        this.treeRenderer.setModuleDisabledImage(moduleDisabledImage);
    }

    public void setModuleEnabledImage(String moduleEnabledImage) {
        this.treeRenderer.setModuleEnabledImage(moduleEnabledImage);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        PresentationContext context = getInputContext();
        context.setRenderMode(RenderMode.getMode("output"));

        treeRenderer.setContext(context);
        treeRenderer.setExpandable(true);
        final Layout treeLayout = treeRenderer.getLayout(object, type);

        return new Layout() {

            @Override
            public HtmlComponent createComponent(final Object object, Class type) {
                HtmlBlockContainer container = new HtmlBlockContainer();

                final HtmlRadioButtonGroup group = new HtmlRadioButtonGroup();
                group.bind((MetaSlot) getInputContext().getMetaObject());
                group.setConverter(new DomainObjectKeyConverter());

                container.addChild(group);
                treeRenderer.setDecorator(new LevelDecorator() {

                    public HtmlComponent decorate(Object treeObject) {
                        HtmlRadioButton button = group.createRadioButton();

                        MetaObject metaObject = MetaObjectFactory.createObject(
                                treeObject, null);
                        button.setUserValue(metaObject.getKey().toString());

                        if (treeObject.equals(object)) {
                            button.setChecked(true);
                        }
                        
                        return button;
                    }

                });

                HtmlComponent component = treeLayout
                        .createComponent(Functionality
                                .getOrderedTopLevelFunctionalities(), type);
                container.addChild(component);

                return container;
            }

        };
    }

}
