package net.sourceforge.fenixedu.renderers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.FlowLayout;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

/**
 * This renderer provides a basic way of presenting slot's values without a
 * special organization. Only slot's values are presented and they can have a
 * separator between them. In the following example an object representing a
 * person is shown using this renderer and a separator of <code>" - "</code>.
 * 
 * <p>
 * Example: Jane Doe - 20 - Female
 * 
 * @author cfgi
 */
public class ValuesRenderer extends OutputRenderer {

	private String eachClasses;

	private String eachStyle;

	private boolean eachInline = true;

	private String eachLayout;

	private String eachSchema;

	private String htmlSeparator;

	private boolean indentation;

	public ValuesRenderer() {
		this.indentation = true;
	}

	/**
	 * Specifies the css classes to be used in the presentation of each value.
	 * 
	 * @property
	 */
	public void setEachClasses(String classes) {
		this.eachClasses = classes;
	}

	public String getEachClasses() {
		return this.eachClasses;
	}

	/**
	 * The style to be used in the presentation of each value.
	 * 
	 * @property
	 */
	public void setEachStyle(String style) {
		this.eachStyle = style;
	}

	public String getEachStyle() {
		return this.eachStyle;
	}

	public boolean isEachInline() {
		return eachInline;
	}

	/**
	 * This property allows you to indicate if each value should be presented
	 * inside a <code>span</code> or a <code>div</code>, that is, inline or
	 * as a block. By default the values are presented inline.
	 * 
	 * @property
	 */
	public void setEachInline(boolean eachInline) {
		this.eachInline = eachInline;
	}

	public String getEachLayout() {
		return eachLayout;
	}

	/**
	 * The layout in which each value will be shown.
	 * 
	 * @property
	 */
	public void setEachLayout(String eachLayout) {
		this.eachLayout = eachLayout;
	}

	public String getEachSchema() {
		return eachSchema;
	}

	/**
	 * The schema to use when presenting each value.
	 * 
	 * @property
	 */
	public void setEachSchema(String eachSchema) {
		this.eachSchema = eachSchema;
	}

	public String getHtmlSeparator() {
		return htmlSeparator;
	}

	/**
	 * The htm separator to be placed between each value. The separator will
	 * appera between any two elements and never at the beginning or the end.
	 * 
	 * @property
	 */
	public void setHtmlSeparator(String htmlSeparator) {
		this.htmlSeparator = htmlSeparator;
	}

	/**
	 * Chooses if the generated elements should be indented or not. This can be
	 * usefull when you want to introduce a separator but need to remove extra
	 * spaces.
	 * 
	 * @property
	 */
	public void setIndentation(boolean indentation) {
		this.indentation = indentation;
	}

	public boolean isIndentation() {
		return this.indentation;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new ValuesLayout(getContext().getMetaObject());
	}

	public class ValuesLayout extends FlowLayout {

		protected int index;
		protected List<MetaSlot> slots;
		protected boolean insertSeparator;

		public ValuesLayout(MetaObject object) {
			super();
			
			this.slots = Collections.unmodifiableList(object.getSlots());
			this.index = 0;
			this.insertSeparator = false;
		}

		@Override
		protected boolean hasMoreComponents() {
			return this.index < this.slots.size();
		}

		protected MetaSlot getNextSlot() {
			return this.slots.get(this.index++);
		}

		@Override
		protected HtmlComponent getNextComponent() {
			if (this.insertSeparator) {
				this.insertSeparator = false;
				return new HtmlText(getHtmlSeparator());
			}
			else {
				MetaSlot slot = getNextSlot();
				
				if (hasMoreComponents() && getHtmlSeparator() != null) {
					this.insertSeparator = true;
				}

				Schema schema = slot.getSchema();
				String layout = slot.getLayout();

				if (schema == null) {
					schema = RenderKit.getInstance()
							.findSchema(getEachSchema());
				}

				if (layout == null) {
					layout = getEachLayout();
				}

				return renderValue(slot.getObject(), slot.getType(), schema,
						layout, slot.getProperties());
			}
		}


		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			HtmlComponent component = super.createComponent(object, type);
			component.setIndented(isIndentation());

			return component;
		}
	}
}
