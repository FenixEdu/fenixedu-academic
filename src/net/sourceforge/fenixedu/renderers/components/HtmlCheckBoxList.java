package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.Predicate;

public class HtmlCheckBoxList extends HtmlMultipleValueComponent {

    private HtmlList list;

    private List<HtmlCheckBox> checkBoxes;

    private List<HtmlHiddenField> hiddenFields;

    private boolean selectAllShown;

    public HtmlCheckBoxList() {
        super();

        this.list = new HtmlList();
        this.checkBoxes = new ArrayList<HtmlCheckBox>();
        this.hiddenFields = new ArrayList<HtmlHiddenField>();
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

    public boolean isSelectAllShown() {
        return this.selectAllShown;
    }

    public void setSelectAllShown(boolean selectAllShown) {
        this.selectAllShown = selectAllShown;
    }

    public List<HtmlCheckBox> getCheckBoxes() {
        return this.checkBoxes;
    }

    protected List<HtmlHiddenField> getHiddenFields() {
        return this.hiddenFields;
    }

    public HtmlList getList() {
        return this.list;
    }

    @Override
    public void setValues(String... values) {
        super.setValues(values);

        outter: for (HtmlCheckBox checkBox : getCheckBoxes()) {
            for (String value : values) {
                if (value.equals(checkBox.getValue())) {
                    checkBox.setChecked(true);
                    continue outter;
                }
            }

            checkBox.setChecked(false);
        }
    }

    public HtmlCheckBox addOption(HtmlComponent component, String value) {
        HtmlCheckBox checkBox = addOption(component);

        checkBox.setUserValue(value);
        return checkBox;
    }

    public HtmlHiddenField addHiddenOption(String value) {
        HtmlHiddenField hiddenField = new HtmlHiddenField();

        getHiddenFields().add(hiddenField);

        hiddenField.setValue(value);
        return hiddenField;
    }

    public HtmlCheckBox addOption(HtmlComponent component) {
        HtmlCheckBox checkBox = new HtmlCheckBox();
        getCheckBoxes().add(checkBox);

        HtmlListItem item = this.list.createItem();
        item.setBody(component);

        return checkBox;
    }

    protected HtmlCheckBox addOption(HtmlComponent component, int index) {
        HtmlCheckBox checkBox = new HtmlCheckBox();
        getCheckBoxes().add(index, checkBox);

        HtmlListItem item = this.list.createItem(index);
        item.setBody(component);

        return checkBox;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        int index = 0;
        for (HtmlCheckBox checkBox : getCheckBoxes()) {
            checkBox.setName(getName());

            if (getTargetSlot() != null) {
                checkBox.setTargetSlot(getTargetSlot());
            }

            checkBox.setId(checkBox.getName() + "/" + index++);
        }

        for (HtmlHiddenField hiddenField : getHiddenFields()) {
            hiddenField.setName(getName());

            if (getTargetSlot() != null) {
                hiddenField.setTargetSlot(getTargetSlot());
            }
        }

        if (isSelectAllShown()) {
            StringBuilder selectAllScript = new StringBuilder();
            boolean allChecked = true;

            String allCheckBoxId = getName() + "/all";

            for (HtmlCheckBox checkBox : getCheckBoxes()) {
                if (!checkBox.isChecked()) {
                    allChecked = false;
                }

                selectAllScript.append("document.getElementById('" + checkBox.getId()
                        + "').checked = this.checked; ");

                String eachScript = "if (! this.checked) document.getElementById('" + allCheckBoxId
                        + "').checked = false;";
                checkBox.setOnClick(eachScript);
                checkBox.setOnDblClick(eachScript);
            }

            HtmlCheckBox checkAllBox = addOption(new HtmlText(RenderUtils
                    .getResourceString("renderers.checkboxlist.selectAll")), 0);
            checkAllBox.setId(allCheckBoxId);
            checkAllBox.setChecked(allChecked);
            checkAllBox.setOnClick(selectAllScript.toString());
            checkAllBox.setOnDblClick(selectAllScript.toString());
        }

        for (int i = 0; i < this.list.getItems().size(); i++) {
            HtmlListItem item = this.list.getItems().get(i);
            HtmlInlineContainer container = new HtmlInlineContainer();

            container.addChild(getCheckBoxes().get(i));
            container.addChild(item.getBody());

            item.setBody(container);
        }

        if (getHiddenFields().size() > 0) {
            HtmlContainer container = new HtmlInlineContainer();

            container.addChild(list);
            for (HtmlHiddenField hiddenField : getHiddenFields()) {
                container.addChild(hiddenField);
            }

            return container.getOwnTag(context);
        } else {
            return this.list.getOwnTag(context);
        }
    }
}
