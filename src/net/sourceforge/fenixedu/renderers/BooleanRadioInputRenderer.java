package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLabel;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonList;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

/**
 * The <code>BooleanRadioInputRender</code> provides a way of doing the
 * input of an Boolean value by using a list of radio buttons. All the values
 * of that Boolean are presented as a radio button and the use can choose
 * one of the values.
 * 
 * <p>
 * Example: 
 * <ul>
 *  <li><input type="radio" name="same"/>True</li>
 *  <li><input type="radio" name="same" checked="checked"/>False</li>
 * </ul>
 * 
 * @author mrsp
 */
public class BooleanRadioInputRenderer extends InputRenderer {
    
    private String eachClasses;
    
    private String eachStyle;
        
    public BooleanRadioInputRenderer() {
        super();
        
        setStyle("white-space: nowrap;");
    }

    public String getEachClasses() {
        return eachClasses;
    }

    /**
     * This property will set the class for each list item
     * @property
     */
    public void setEachClasses(String eachClasses) {
        this.eachClasses = eachClasses;
    }

    public String getEachStyle() {
        return eachStyle;
    }

    /**
     * This property will set the style for each list item
     * @property
     */
    public void setEachStyle(String eachStyle) {
        this.eachStyle = eachStyle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Boolean booleanObject = (Boolean) object;                
                HtmlRadioButtonList radioList = new HtmlRadioButtonList();
                
                Boolean booleanTrue = Boolean.TRUE;                
                MetaObject booleanMetaObject = MetaObjectFactory.createObject(booleanTrue, null);
                PresentationContext newContext = getContext().createSubContext(booleanMetaObject);
                newContext.setRenderMode(RenderMode.getMode("output"));
                HtmlComponent component = RenderKit.getInstance().render(newContext, booleanTrue);
                
                HtmlLabel trueLabel = new HtmlLabel();
                trueLabel.setBody(component);
                HtmlRadioButton buttonTrue = radioList.addOption(trueLabel, booleanTrue.toString());
                trueLabel.setFor(buttonTrue);
                
                Boolean booleanFalse = Boolean.FALSE;                                                            
                booleanMetaObject = MetaObjectFactory.createObject(booleanFalse, null);
                newContext = getContext().createSubContext(booleanMetaObject);
                newContext.setRenderMode(RenderMode.getMode("output"));
                component = RenderKit.getInstance().render(newContext, booleanFalse);
                
                HtmlLabel falseLabel = new HtmlLabel();
                falseLabel.setBody(component);
                HtmlRadioButton buttonFalse = radioList.addOption(falseLabel, booleanFalse.toString());
                falseLabel.setFor(buttonFalse);
                
                buttonTrue.setChecked(booleanObject == null ? false : booleanObject);                
                buttonFalse.setChecked(booleanObject == null ? false : !booleanObject);
                
                radioList.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                
                for (HtmlListItem item : radioList.getList().getItems()) {
                    item.setClasses(getEachClasses());
                    item.setStyle(getEachStyle());
                }
                
                return radioList;
            }
            
        };
    }
}
