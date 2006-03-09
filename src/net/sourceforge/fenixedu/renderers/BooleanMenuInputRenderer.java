package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer provides an alternative way of doing the input of
 * a boolean value. This renderer presents an html menu with two
 * options: one for the <tt>true</tt> value, and other for the
 * <tt>false</tt> value.
 * 
 * <p>
 * The options text is retrieved from the resources using the keys
 * <tt>TRUE</tt> and <tt>FALSE</tt>.
 * 
 * <p>
 * Example:
 * <select>
 *  <option>Yes</option>
 *  <option>No</option>
 * </select>
 * 
 * @author cfgi
 */
public class BooleanMenuInputRenderer extends InputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlMenu menu = new HtmlMenu();
                
                HtmlMenuOption trueOption  = menu.createOption(RenderUtils.getResourceString("TRUE"));
                HtmlMenuOption falseOption = menu.createOption(RenderUtils.getResourceString("FALSE"));
                
                trueOption.setValue("true");
                falseOption.setValue("false");
                
                (((Boolean) object) ? trueOption : falseOption).setSelected(true);
                menu.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                
                return menu;
            }
            
        };
    }

}
