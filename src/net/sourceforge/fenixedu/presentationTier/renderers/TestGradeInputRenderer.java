package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.tests.TestsGrade;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlController;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;

public class TestGradeInputRenderer extends InputRenderer {
	private String fieldWidth;

	public String getFieldWidth() {
		return fieldWidth;
	}

	/**
	 * This property allows to specify the width of each input field (value and
	 * scale).
	 * 
	 * @property
	 */
	public void setFieldWidth(String fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	public class GradeInputLayout extends Layout {

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			HtmlInlineContainer container = new HtmlInlineContainer();

			MetaSlot slot = (MetaSlot) getInputContext().getMetaObject();
			final HtmlHiddenField hiddenField = new HtmlHiddenField();
			hiddenField.bind(slot);

			final HtmlTextInput value = new HtmlTextInput();
			value.setName(slot.getKey().toString() + "_value");
			value.setSize(getFieldWidth());
			final HtmlTextInput scale = new HtmlTextInput();
			scale.setName(slot.getKey().toString() + "_scale");
			scale.setSize(getFieldWidth());

			hiddenField.setController(new HtmlController() {
				@Override
				public void execute(IViewState viewState) {
					hiddenField.setValue(value.getValue() + "|" + scale.getValue());
				}
			});

			hiddenField.setConverter(new Converter() {
				@Override
				public Object convert(Class type, Object inValue) {
					String input = (String) inValue;

					if (input == null || input.length() == 0) {
						return null;
					}

					try {
						String[] parts = input.split("\\|");
						double value = Double.parseDouble(parts[0]);
						double scale = Double.parseDouble(parts[1]);

						return new TestsGrade(value, scale);
					} catch (NumberFormatException e) {
						throw new ConversionException("fenix.renderers.converter.grade.invalid", e);
					} catch (IndexOutOfBoundsException e) {
						throw new ConversionException("fenix.renderers.converter.grade.invalid", e);
					}
				}
			});

			container.addChild(hiddenField);
			container.addChild(value);
			container.addChild(new HtmlText("/"));
			container.addChild(scale);

			return container;
		}

	}

	public TestGradeInputRenderer() {
		super();
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new GradeInputLayout();
	}

}
