package net.sourceforge.fenixedu.renderers;

import java.util.Iterator;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.FlowLayout;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

/**
 * The <code>FlowRenderer</code> can show several slots with a special layout.
 * The slot's label and value are show one after the other.
 * 
 * <p>
 * Example: Name: Joanne Age: 52
 */
public class FlowRenderer extends OutputRenderer {

	private String eachClasses;

	private String eachStyle;

	private boolean eachInline = true;

	private boolean labelExcluded = false;

	private String labelTerminator;

	private String labelClasses;

	private String labelStyle;

	public String getEachClasses() {
		return this.eachClasses;
	}

	/**
	 * Allows to specify the class attribute for each one of the slot's
	 * presentation.
	 * 
	 * @property
	 */
	public void setEachClasses(String eachClasses) {
		this.eachClasses = eachClasses;
	}

	public boolean isEachInline() {
		return this.eachInline;
	}

	/**
	 * This property selects if each slot should be presented as an inline
	 * element or as a block element. By default slots are presented inline,
	 * that is, they will be added inside <tt>span</tt> elements. If
	 * <tt>eachInline</tt> is <tt>false</tt> then a <tt>div</tt> will be
	 * used.
	 * 
	 * @property
	 */
	public void setEachInline(boolean eachInline) {
		this.eachInline = eachInline;
	}

	public String getEachStyle() {
		return this.eachStyle;
	}

	/**
	 * The value of the style attribute of each slot's presentation.
	 * 
	 * @property
	 */
	public void setEachStyle(String eachStyle) {
		this.eachStyle = eachStyle;
	}

	public boolean isLabelExcluded() {
		return this.labelExcluded;
	}

	/**
	 * Whether the label should be presented before each editor or not.
	 * 
	 * @property
	 */
	public void setLabelExcluded(boolean labelExcluded) {
		this.labelExcluded = labelExcluded;
	}

	public String getLabelTerminator() {
		return this.labelTerminator;
	}

	/**
	 * Chooses the suffix to be added to each label. If the label already
	 * contains that suffix then nothing will be added. See
	 * {@link StandardObjectRenderer#setLabelTerminator(String)}.
	 * 
	 * @property
	 */
	public void setLabelTerminator(String labelTerminator) {
		this.labelTerminator = labelTerminator;
	}

	public String getLabelClasses() {
		return labelClasses;
	}

	/**
	 * Allows to specify the class of the element around the generated label
	 * (when shown)
	 * 
	 * @property
	 */
	public void setLabelClasses(String labelClasses) {
		this.labelClasses = labelClasses;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Allows to specify the style applied to the generated label (when shown)
	 * 
	 * @property
	 */
	public void setLabelStyle(String labelStyle) {
		this.labelStyle = labelStyle;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		MetaObject meta = getContext().getMetaObject();
		final Iterator<MetaSlot> slots = meta.getSlots().iterator();

		return new FlowLayout() {

			@Override
			protected boolean hasMoreComponents() {
				return slots.hasNext();
			}

			@Override
			protected HtmlComponent getNextComponent() {
				MetaSlot slot = slots.next();
				HtmlComponent component = renderSlot(slot);

				return createContainer(slot, component);
			}

			private HtmlComponent createContainer(MetaSlot slot, HtmlComponent component) {
				if (isLabelExcluded()) {
					return component;
				}

				HtmlInlineContainer container = new HtmlInlineContainer();
				HtmlText label = new HtmlText(addLabelTerminator(slot.getLabel()), false);

				HtmlInlineContainer labelContainer = new HtmlInlineContainer();
				labelContainer.addChild(label);
				labelContainer.setStyle(getLabelStyle());
				labelContainer.setClasses(getLabelClasses());

				container.addChild(labelContainer);
				container.addChild(component);

				return container;
			}

			// duplicated code id=standard-renderer.label.addTerminator
			protected String addLabelTerminator(String label) {
				if (getLabelTerminator() == null) {
					return label;
				}

				if (label == null) {
					return null;
				}

				if (label.endsWith(getLabelTerminator())) {
					return label;
				}

				return label + getLabelTerminator();
			}

		};
	}

}
