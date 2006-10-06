package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

import org.apache.commons.collections.Predicate;

public class HtmlRadioButtonList extends HtmlSimpleValueComponent {

    private HtmlList list;
    private List<HtmlRadioButton> radioButtons;

    public HtmlRadioButtonList() {
        super();
        
        this.list = new HtmlList();
        this.radioButtons = new ArrayList<HtmlRadioButton>();
    }

    public void addClass(String newClass) {
        this.list.addClass(newClass);
    }

    public HtmlListItem createItem() {
        return this.list.createItem();
    }

    public HtmlComponent getChild(Predicate predicate) {
        return this.list.getChild(predicate);
    }

    public List<HtmlComponent> getChildren() {
        return this.list.getChildren();
    }

    public List<HtmlComponent> getChildren(Predicate predicate) {
        return this.list.getChildren(predicate);
    }

    public HtmlComponent getChildWithId(String id) {
        return this.list.getChildWithId(id);
    }

    public String getClasses() {
        return this.list.getClasses();
    }

    public String getId() {
        return this.list.getId();
    }

    public String getOnClick() {
        return this.list.getOnClick();
    }

    public String getOnDblClick() {
        return this.list.getOnDblClick();
    }

    public String getOnKeyDown() {
        return this.list.getOnKeyDown();
    }

    public String getOnKeyPress() {
        return this.list.getOnKeyPress();
    }

    public String getOnKeyUp() {
        return this.list.getOnKeyUp();
    }

    public String getOnMouseDown() {
        return this.list.getOnMouseDown();
    }

    public String getOnMouseMove() {
        return this.list.getOnMouseMove();
    }

    public String getOnMouseOut() {
        return this.list.getOnMouseOut();
    }

    public String getOnMouseOver() {
        return this.list.getOnMouseOver();
    }

    public String getOnMouseUp() {
        return this.list.getOnMouseUp();
    }

    public String getStyle() {
        return this.list.getStyle();
    }

    public String getTitle() {
        return this.list.getTitle();
    }

    public boolean isVisible() {
        return this.list.isVisible();
    }

    public void setClasses(String classes) {
        this.list.setClasses(classes);
    }

    public void setId(String id) {
        this.list.setId(id);
    }

    public void setOnClick(String onclick) {
        this.list.setOnClick(onclick);
    }

    public void setOnDblClick(String ondblclick) {
        this.list.setOnDblClick(ondblclick);
    }

    public void setOnKeyDown(String onkeydown) {
        this.list.setOnKeyDown(onkeydown);
    }

    public void setOnKeyPress(String onkeypress) {
        this.list.setOnKeyPress(onkeypress);
    }

    public void setOnKeyUp(String onkeyup) {
        this.list.setOnKeyUp(onkeyup);
    }

    public void setOnMouseDown(String onmousedown) {
        this.list.setOnMouseDown(onmousedown);
    }

    public void setOnMouseMove(String onmousemove) {
        this.list.setOnMouseMove(onmousemove);
    }

    public void setOnMouseOut(String onmouseout) {
        this.list.setOnMouseOut(onmouseout);
    }

    public void setOnMouseOver(String onmouseover) {
        this.list.setOnMouseOver(onmouseover);
    }

    public void setOnMouseUp(String onmouseup) {
        this.list.setOnMouseUp(onmouseup);
    }

    public void setStyle(String style) {
        this.list.setStyle(style);
    }

    public void setTitle(String title) {
        this.list.setTitle(title);
    }

    public void setVisible(boolean visible) {
        this.list.setVisible(visible);
    }

    public List<HtmlRadioButton> getRadioButtons() {
        return this.radioButtons;
    }

    public HtmlList getList() {
        return this.list;
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        
        for (HtmlRadioButton radio : getRadioButtons()) {
            if (value != null && value.equals(radio.getValue())) {
                radio.setChecked(true);
            }
            else {
                radio.setChecked(false);
            }
        }
    }

    public HtmlRadioButton addOption(HtmlComponent component, String value) {
        HtmlRadioButton radio = addOption(component);
        
        radio.setUserValue(value);
        return radio;
    }
    
    public HtmlRadioButton addOption(HtmlComponent component) {
        HtmlRadioButton radio = new HtmlRadioButton() {

            @Override
            public void setChecked(boolean checked) {
                HtmlRadioButtonList.this.setChecked(this, checked);
                
                super.setChecked(checked);
            }
            
        };
        
        getRadioButtons().add(radio);
        
        HtmlListItem item = this.list.createItem();
        item.addChild(component);
        
        radio.setName(getName());
        return radio;
    }

    protected void setChecked(HtmlRadioButton button, boolean checked) {
        if (! checked) {
            return;
        }

        for (HtmlRadioButton radio : getRadioButtons()) {
            if (radio.equals(button)) {
                continue;
            }
            
            radio.setChecked(false);
        }
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        for (HtmlRadioButton radio : getRadioButtons()) {
            radio.setName(getName());
            
            if (getTargetSlot() != null) {
                radio.setTargetSlot(getTargetSlot());
            }
        }

        for (int i = 0; i < this.list.getItems().size(); i++) {
            HtmlListItem item = this.list.getItems().get(i);
            HtmlInlineContainer container = new HtmlInlineContainer();
            
            container.addChild(getRadioButtons().get(i));
            
            for (HtmlComponent component : item.getChildren()) {
                container.addChild(component);
            }
            
            item.setBody(container);
        }
        
        return this.list.getOwnTag(context);
    }
}
