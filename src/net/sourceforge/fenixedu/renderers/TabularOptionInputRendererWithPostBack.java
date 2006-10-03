package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.renderers.TabularOptionInputRenderer.CheckableTabularLayout;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectCollection;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

import org.apache.commons.beanutils.BeanComparator;

/**
 * This renderer allows you choose several object from a list of choices. The list of choices is
 * presented in a table but each row has a checkbox that allows you to select the object in that
 * row.
 * 
 * <p>
 * The list of options is given by a {@link net.sourceforge.fenixedu.renderers.DataProvider data provider}.
 * 
 * <p>
 * Example:
 * <table border="1">
 *      <thead>
 *          <th></th>
 *          <th>Name</th>
 *          <th>Age</th>
 *          <th>Gender</th>
 *      </thead>
 *      <tr>
 *          <td><input type="checkbox"/></td>
 *          <td>Name A</td>
 *          <td>20</td>
 *          <td>Female</td>
 *      </tr>
 *      <tr>
 *          <td><input type="checkbox" checked="checked"/></td>
 *          <td>Name B</td>
 *          <td>22</td>
 *          <td>Male</td>
 *      </tr>
 *      <tr>
 *          <td><input type="checkbox" checked="checked"/></td>
 *          <td>Name C</td>
 *          <td>21</td>
 *          <td>Female</td>
 *      </tr>
 *  </table>
 *  
 * @author pcma
 */
public class TabularOptionInputRendererWithPostBack extends TabularOptionInputRenderer {
     
	private String destination;
	
    public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	
    @Override
	protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
		HtmlComponent component = super.renderComponent(layout, object, type);
		
		CheckableTabularLayout checkableLayout = (CheckableTabularLayout) layout;
		List<HtmlCheckBox> checkboxes = checkableLayout.getCheckBoxes();
    	
    	for(HtmlCheckBox checkbox : checkboxes) {
    		checkbox.setOnClick("this.form.postback.value=1; this.form.submit();");
    	}
    	
		HtmlInlineContainer htmlInlineContainer = new HtmlInlineContainer();
		HtmlHiddenField hiddenField = new HtmlHiddenField();
		hiddenField.setName("postback");
		htmlInlineContainer.addChild(hiddenField);
		htmlInlineContainer.addChild(component);
		hiddenField.setController(new PostBackController(getDestination()));
		return htmlInlineContainer;
    }



	private static class PostBackController extends HtmlController {

        private String destination;

        public PostBackController(String destination) {
            this.destination = destination;
        }

        @Override
        public void execute(IViewState viewState) {
        	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();
        	
        	if (component.getValue() != null && component.getValue().length() > 0) {
        		String destinationName = this.destination == null ? "postback" : this.destination;
                ViewDestination destination = viewState.getDestination(destinationName);

                if (destination != null) {
                    viewState.setCurrentDestination(destination);
                } else {
                    viewState.setCurrentDestination("postBack");
                }

                viewState.setSkipValidation(true);
        	}
        	
        	component.setValue(null);
        }

    }

}
