package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.presentationTier.renderers.components.TinyMceEditor;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonList;
import pt.ist.fenixWebFramework.renderers.components.HtmlScript;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlController;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * This renderer has the same behaviour than {@link net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageTextInputRenderer}
 * but uses a rich-text editor in place of a textarea for browsers that support it.
 * 
 * @author cfgi
 */
public class MultiLanguageRichTextInputRenderer extends MultiLanguageTextInputRenderer {

    private String config;
    private boolean safe;
    private boolean showEditorOptions = true;
	private HtmlRadioButton useEditor;
	private HtmlRadioButton removeEditor;
	private List<TinyMceEditor> editors = new ArrayList<TinyMceEditor>();
    
    public boolean isSafe() {
        return safe;
    }

    /**
     * If this property is set to <tt>true</tt> then the input will be filtered and any
     * unsupported HTML will be removed or escaped to the corresponding entities. 
     * 
     * @property
     */
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public String getConfig() {
        return this.config;
    }

    /**
     * Allows you to choose a configuration for the editor. The given name
     * is used to fetch the properties from a file located in {@value TinyMceEditor#CONFIG_PATH} with
     * the same name and ending in ".properties".
     * 
     * @property
     */
    public void setConfig(String config) {
        this.config = config;
    }

    
    public boolean isShowEditorOptions() {
		return showEditorOptions;
	}

    /**
     * Allows you to hide or show options to remove the editor dynamically.
     * 
     * @property
     */
	public void setShowEditorOptions(boolean showEditorOptions) {
		this.showEditorOptions = showEditorOptions;
	}

	protected String getLocalName(String name, String category) {
		String prefix = getInputContext().getMetaObject().getKey() + "_";
		
		if (category != null) {
			return prefix + category + "_" + name;
		}
		else {
			return prefix + name;
		}
	}
	
	@Override
    protected HtmlBlockContainer getTopContainer() {
    	HtmlBlockContainer container = super.getTopContainer();

    	if (isShowEditorOptions()) {
	    	HtmlScript script = new HtmlScript();
	    	script.setContentType("text/javascript");
	    	script.setConditional(true);
	    	script.setScript("\n"+
	    			"function fenix_removeEditor(editors) {\n" +
	    			"	for (var i in editors) {\n" +
	    			"		var editorId = tinyMCE.getEditorId(editors[i]);\n" +
	    			"		if (! editorId) continue;\n" +
	    			"		tinyMCE.removeMCEControl(editorId);\n" +
	    			"	}\n" +
	    			"}\n" +
	    			"function fenix_addEditor(editors) {\n" +
	    			"	for (var i in editors) {\n" +
	    			"		var id = editors[i];\n" +
	    			"		var editorId = tinyMCE.getEditorId(id);\n" +
	    			"		if (editorId) continue;\n"+
	    			"		var element = document.getElementById(id);\n" +
	    			"		if (element) {\n" +
	    			"			tinyMCE.addMCEControl(element, id);\n"+
	    			"               }\n"+
	    			"	}\n" +
	    			"}\n"
				);
	    	
	    	HtmlRadioButtonList radioList = new HtmlRadioButtonList();
	    	radioList.setName(getLocalName("controls", "editor"));
	    	
	    	this.useEditor = radioList.addOption(new HtmlText(RenderUtils.getResourceString("renderers.rich-text.editor.add")), "activate");
	    	this.removeEditor = radioList.addOption(new HtmlText(RenderUtils.getResourceString("renderers.rich-text.editor.remove")), "disable");
	    	
	    	radioList.setClasses("liinline nobullet mbottom05"); // TODO: use style?
	    	this.useEditor.setChecked(true);
			
	    	container.addChild(script);
	    	container.addChild(radioList);
    	}
    	
    	return container;
    }

	
	@Override
	protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
		HtmlContainer component = (HtmlContainer) super.renderComponent(layout, object, type);

		HtmlHiddenField field = new HtmlHiddenField();
		field.setController(new AddScriptController());
		
		component.addChild(field);
		updateEditorSelectors();
		
		return component;
	}

	@Override
    protected HtmlSimpleValueComponent getInputComponent() {
        TinyMceEditor editor = new TinyMceEditor();
        
        editor.setConfig(getConfig());
        editor.setColumns(getColumns());
        editor.setRows(getRows());
        
        this.editors.add(editor);
        
        return editor;
    }

    @Override
    protected void configureInputComponent(HtmlSimpleValueComponent textInput) {
        super.configureInputComponent(textInput);
        
        textInput.setId(textInput.getName());
    }

    @Override
    protected Converter getConverter() {
        if (isSafe()) {
            return new MultiLanguageStringSafeHtmlConverter();
        }
        else {
            return super.getConverter();
        }
    }
    
    private void updateEditorSelectors() {
    	IViewState viewState = getInputContext().getViewState();
		Map<Integer, LanguageBean> map = (Map<Integer, LanguageBean>) viewState.getLocalAttribute("mlsMap");
    	String prefix = getLocalName("text_", null);
    	
		StringBuilder builder = new StringBuilder();
		for (Integer index : map.keySet()) {
			if (builder.length() > 0) {
				builder.append(", ");
			}
			
			builder.append("'" + prefix + index + "'");
		}
		
		this.useEditor.setOnClick(String.format("fenix_addEditor([%s]);", builder.toString()));
		this.removeEditor.setOnClick(String.format("fenix_removeEditor([%s]);", builder.toString()));
		
		if (this.removeEditor.isChecked()) {
			for (TinyMceEditor editor : this.editors) {
				editor.disable();
			}
		}
    }
    
    private class AddScriptController extends HtmlController {

		private static final long serialVersionUID = 1L;
		
		@Override
		public void execute(IViewState viewState) {
			updateEditorSelectors();
		}
    	
    }
}
