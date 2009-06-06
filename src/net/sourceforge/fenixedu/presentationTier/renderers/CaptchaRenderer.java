package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.dataTransferObject.Captcha;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;

public class CaptchaRenderer extends InputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		final MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();
		final HtmlInlineContainer container = new HtmlInlineContainer();

		final HtmlHiddenField hiddenField = new HtmlHiddenField();
		hiddenField.setTargetSlot(key);
		container.addChild(hiddenField);

		final HtmlImage image = new HtmlImage();
		image.setSource(getContext().getViewState().getRequest().getContextPath() + "/publico/jcaptcha.do");
		image.setStyle("border: 1px solid #bbb; padding: 5px;");
		container.addChild(image);

		final HtmlTextInput input = new HtmlTextInput();
		input.setName(key.toString() + "_response");
//		input.addValidator(new CaptchaValidator());
		container.addChild(input);

		return container;
	    }
	};
    }

    public class CaptchaConverter extends Converter {
	static private final long serialVersionUID = 1L;

	private HtmlTextInput input;

	public CaptchaConverter(HtmlTextInput input) {
	    this.input = input;
	}

	@Override
	public Object convert(Class type, Object value) {
	    return new Captcha(input.getValue());
	}
    }

    /*
    static private class CaptchaValidator extends HtmlValidator {

	private static final long serialVersionUID = 5199426957775725692L;

	public CaptchaValidator() {
	    super();
	    setMessage("renderers.validator.invalid.captcha.value");
	}

	public CaptchaValidator(HtmlChainValidator htmlChainValidator) {
	    super(htmlChainValidator);
	    setMessage("renderers.validator.invalid.captcha.value");
	}

	@Override
	public void performValidation() {

	    final HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();
	    final String value = component.getValue();

	    if (value == null || value.length() == 0) {
		setMessage("renderers.validator.dateTime.required");
		setValid(false);
	    } else {
		final String captchaId =getComponent().getViewState().getRequest().getSession().getId();
		try {
		    if (!CaptchaServicePlugin.getInstance().getService().validateResponseForID(captchaId, value)) {
			setMessage("renderers.validator.invalid.captcha.value");
			setValid(false);
		    } else {
			setValid(true);
		    }
		} catch (CaptchaServiceException e) {
		    e.printStackTrace();
		    setMessage("renderers.validator.dateTime.required");
		    setValid(false);
		}
	    }
	}
    }
    */

}
