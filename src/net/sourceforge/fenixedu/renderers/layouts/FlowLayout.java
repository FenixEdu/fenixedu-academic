package net.sourceforge.fenixedu.renderers.layouts;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;

public abstract class FlowLayout extends Layout {
    private String eachClasses;

    private String eachStyle;

    private String eachSchema;

    private String eachLayout;

    private boolean eachInline = true;

    public void setEachClasses(String classes) {
        this.eachClasses = classes;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    public void setEachStyle(String style) {
        this.eachStyle = style;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    public String getEachLayout() {
        return eachLayout;
    }

    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public boolean isEachInline() {
        return eachInline;
    }

    public void setEachInline(boolean eachInline) {
        this.eachInline = eachInline;
    }

    @Override
    public String[] getPropertyNames() {
        return mergePropertyNames(super.getPropertyNames(), new String[] { "eachClasses", "eachStyle",
                "eachLayout", "eachSchema", "eachInline" });
    }

    @Override
    public HtmlComponent createComponent(Object object, Class type) {
        HtmlComponent container = getContainer();

        while (hasMoreComponents()) {
            HtmlComponent component = getNextComponent();

            addComponent(container, component);
        }

        return container;
    }

    protected HtmlComponent getContainer() {
        return new HtmlInlineContainer();
    }

    protected abstract boolean hasMoreComponents();

    protected abstract HtmlComponent getNextComponent();

    protected void addComponent(HtmlComponent component, HtmlComponent child) {
        ((HtmlContainer) component).addChild(child);
    }

    @Override
    public void applyStyle(HtmlComponent component) {
        super.applyStyle(component);

        String[] eachClasses = new String[] { null };
        if (this.eachClasses != null) {
            eachClasses = this.eachClasses.split(",", -1);
        }

        int index = 0;
        for (HtmlComponent child : component.getChildren()) {
            child.setClasses(eachClasses[index % eachClasses.length]);
            child.setStyle(this.eachStyle);

            index++;
        }
    }
}
