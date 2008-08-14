package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.renderers.components.HtmlEditor;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlSubmitButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextArea;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;

/**
 * An javascript html editor for doing the input of html text. This renderer
 * abstracts the javascript html editor available in Fenix and binds it to the
 * slot beeing edited.
 * 
 * @author cfgi
 */
public class HtmlEditorRenderer extends InputRenderer {

    private int columns;
    private int rows;
    private int heigth;
    private int width;

    private boolean safe;

    public HtmlEditorRenderer() {
	super();

	setWidth(600);
	setHeigth(400);
    }

    public int getColumns() {
	return this.columns;
    }

    /**
     * The number of columns of the fall back textarea, that is, the text area
     * that is shown when the html editor is not supported by the browser.
     * 
     * @property
     */
    public void setColumns(int columns) {
	this.columns = columns;
    }

    public int getRows() {
	return this.rows;
    }

    /**
     * The number of rows of the fall back textarea.
     * 
     * @property
     */
    public void setRows(int rows) {
	this.rows = rows;
    }

    public int getHeigth() {
	return this.heigth;
    }

    /**
     * The height of the html editor.
     * 
     * @property
     */
    public void setHeigth(int heigth) {
	this.heigth = heigth;
    }

    public int getWidth() {
	return this.width;
    }

    /**
     * The width of thehtml editor.
     * 
     * @property
     */
    public void setWidth(int width) {
	this.width = width;
    }

    public boolean isSafe() {
	return this.safe;
    }

    /**
     * If this property is set to <tt>true</tt> then the input will be filtered
     * and any unsupported HTML will be removed or escaped to the corresponding
     * entities. The idea is that a piece of code like
     * 
     * <pre>
     * &lt;p onmouseover=&quot;doSomething();&quot;&gt;
     *     &lt;table style=&quot;...&quot;/&gt;
     * &lt;/p&gt;
     * </pre>
     * 
     * would be saved as
     * 
     * <pre>
     * &lt;p&gt;
     *     &lt;table&gt;&lt;/table&gt;
     * &lt;/p&gt;
     * </pre>
     * 
     * @property
     */
    public void setSafe(boolean safe) {
	this.safe = safe;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new EditorLayout();
    }

    private class EditorLayout extends Layout {

	private boolean isBrowserSupported() {
	    String[] notSupported = { "safari", "konqueror", "opera" };

	    HttpServletRequest request = getInputContext().getViewState().getRequest();

	    String userAgent = request.getHeader("User-Agent");
	    if (userAgent == null) {
		return false;
	    }

	    userAgent = userAgent.toLowerCase();
	    for (int i = 0; i < notSupported.length; i++) {
		String id = notSupported[i];

		if (userAgent.indexOf(id) != -1) {
		    return false;
		}
	    }

	    return true;
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    // TODO: cfgi, rich text editor also verifies browser so is this
	    // needed?
	    if (!isBrowserSupported()) {
		return createTextArea(object, type);
	    } else {
		return createEditor(object, type);
	    }
	}

	private HtmlComponent createEditor(Object object, Class type) {
	    HtmlInlineContainer container = new HtmlInlineContainer();

	    HtmlEditor editor = new HtmlEditor();

	    editor.setWidth(getWidth());
	    editor.setHeigth(getHeigth());

	    editor.setValue((String) object);
	    editor.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());

	    if (isSafe()) {
		editor.setConverter(new SafeHtmlConverter());
	    }

	    HtmlSubmitButton submitButton = getInputContext().getForm().getSubmitButton();

	    String currentScript = submitButton.getOnClick();
	    submitButton.setOnClick("updateRTE('" + editor.getName() + "');" + (currentScript == null ? "" : currentScript));

	    container.addChild(editor);
	    return container;
	}

	private HtmlComponent createTextArea(Object object, Class type) {
	    HtmlTextArea textArea = new HtmlTextArea();
	    textArea.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());

	    if (isSafe()) {
		textArea.setConverter(new SafeHtmlConverter());
	    }

	    textArea.setValue((String) object);

	    textArea.setColumns(getColumns());
	    textArea.setRows(getRows());

	    return textArea;
	}
    }

}
