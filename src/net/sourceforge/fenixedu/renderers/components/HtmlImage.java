package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlImage extends HtmlComponent {
    private String source;
    private String description;
    private String name;
    private String height; // String because it can receive "100px" or "100%"
    private String width;

    public HtmlImage() {
        super();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);

        tag.setName("img");

        tag.setAttribute("src", getSource());
        tag.setAttribute("name", getName());
        tag.setAttribute("alt", getDescription());
        tag.setAttribute("width", getWidth());
        tag.setAttribute("height", getHeight());

        return tag;
    }

}
