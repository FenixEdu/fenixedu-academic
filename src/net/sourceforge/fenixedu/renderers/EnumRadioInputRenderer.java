package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonList;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

/**
 * The <code>EnumRadioInputRenderer</code> provides a way of doing the
 * input of an enum value by using a list of radio buttons. All the values
 * of that enum are presented as a radio button and the use can choose
 * one of the values.
 * 
 * <p>
 * Example: 
 * <ul>
 *  <li><input type="radio" name="same"/>Male</li>
 *  <li><input type="radio" name="same" checked="checked"/>Female</li>
 * </ul>
 * 
 * @author cfgi
 */
public class EnumRadioInputRenderer extends EnumInputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Enum enumerate = (Enum) object;
                
                HtmlRadioButtonList radioList = new HtmlRadioButtonList();
                
                Object[] constants = type.getEnumConstants();
                for (Object constant : constants) {
                    Enum oneEnum = (Enum) constant;
                    MetaObject enumMetaObject = MetaObjectFactory.createObject(oneEnum, null);
                    
                    PresentationContext newContext = getContext().createSubContext(enumMetaObject);
                    newContext.setRenderMode(RenderMode.getMode("output"));
                    
                    HtmlComponent component = RenderKit.getInstance().render(newContext, oneEnum);
                    HtmlRadioButton radioButton = radioList.addOption(component, oneEnum.toString());
                                       
                    if (oneEnum.equals(enumerate)) {
                        radioButton.setChecked(true);
                    }
                }
                
                radioList.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                radioList.setConverter(new EnumConverter(type));
                
                return radioList;
            }
            
        };
    }

}
