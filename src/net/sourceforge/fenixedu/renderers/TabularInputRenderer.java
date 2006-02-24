package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class TabularInputRenderer extends InputRenderer {

    private CollectionRenderer collectionRenderer;
    
    public TabularInputRenderer() {
        this.collectionRenderer = new CollectionRenderer() {

            @Override
            protected HtmlComponent renderSlot(MetaSlot slot) {
                return TabularInputRenderer.this.renderSlot(slot);
            }
            
        };
    }
    
    public String getBundle(String name) {
        return this.collectionRenderer.getBundle(name);
    }

    public String getCaption() {
        return this.collectionRenderer.getCaption();
    }

    public String getClasses() {
        return this.collectionRenderer.getClasses();
    }

    public String getColumnClasses() {
        return this.collectionRenderer.getColumnClasses();
    }

    public String getHeaderClasses() {
        return this.collectionRenderer.getHeaderClasses();
    }

    public String getKey(String name) {
        return this.collectionRenderer.getKey(name);
    }

    public String getLink(String name) {
        return this.collectionRenderer.getLink(name);
    }

    public String getModule(String name) {
        return this.collectionRenderer.getModule(name);
    }

    public String getOrder(String name) {
        return this.collectionRenderer.getOrder(name);
    }

    public String getParam(String name) {
        return this.collectionRenderer.getParam(name);
    }

    public String getPrefixes() {
        return this.collectionRenderer.getPrefixes();
    }

    public String getRowClasses() {
        return this.collectionRenderer.getRowClasses();
    }

    public String getStyle() {
        return this.collectionRenderer.getStyle();
    }

    public String getSuffixes() {
        return this.collectionRenderer.getSuffixes();
    }

    public String getText(String name) {
        return this.collectionRenderer.getText(name);
    }

    public String getTitle() {
        return this.collectionRenderer.getTitle();
    }

    public void setBundle(String name, String value) {
        this.collectionRenderer.setBundle(name, value);
    }

    public void setCaption(String caption) {
        this.collectionRenderer.setCaption(caption);
    }

    public void setClasses(String classes) {
        this.collectionRenderer.setClasses(classes);
    }

    public void setColumnClasses(String columnClasses) {
        this.collectionRenderer.setColumnClasses(columnClasses);
    }

    public void setHeaderClasses(String headerClasses) {
        this.collectionRenderer.setHeaderClasses(headerClasses);
    }

    public void setKey(String name, String value) {
        this.collectionRenderer.setKey(name, value);
    }

    public void setLink(String name, String value) {
        this.collectionRenderer.setLink(name, value);
    }

    public void setModule(String name, String value) {
        this.collectionRenderer.setModule(name, value);
    }

    public void setOrder(String name, String value) {
        this.collectionRenderer.setOrder(name, value);
    }

    public void setParam(String name, String value) {
        this.collectionRenderer.setParam(name, value);
    }

    public void setPrefixes(String prefixes) {
        this.collectionRenderer.setPrefixes(prefixes);
    }

    public void setRowClasses(String rowClasses) {
        this.collectionRenderer.setRowClasses(rowClasses);
    }

    public void setStyle(String style) {
        this.collectionRenderer.setStyle(style);
    }

    public void setSuffixes(String suffixes) {
        this.collectionRenderer.setSuffixes(suffixes);
    }

    public void setText(String name, String value) {
        this.collectionRenderer.setText(name, value);
    }

    public void setTitle(String title) {
        this.collectionRenderer.setTitle(title);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        this.collectionRenderer.setContext(getContext());

        return this.collectionRenderer.new CollectionTabularLayout((Collection) object);
    }
}
