package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlCheckBoxGroup extends HtmlMultipleValueComponent {

    private List<HtmlCheckBox> checkBoxes;
    
    private HtmlComponent body;
    
    public HtmlCheckBoxGroup() {
        super();
        
        checkBoxes = new ArrayList<HtmlCheckBox>();
    }

    public HtmlCheckBox createCheckBox() {
         HtmlCheckBox checkBox = new HtmlCheckBox();
         
         this.checkBoxes.add(checkBox);
         
         return checkBox;
    }

    public HtmlComponent getBody() {
        return body;
    }

    public void setBody(HtmlComponent body) {
        this.body = body;
    }

    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        children.add(body);
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        return body.getOwnTag(context);
    }

}