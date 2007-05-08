package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import net.sourceforge.fenixedu.presentationTier.renderers.components.TinyMceEditor;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonList;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * Allows you to use the <a href="http://tinymce.moxiecode.com/">TinyMCE</a>
 * Javascript HTML WYSIWYG editor.
 * 
 * @author cfgi
 */
public class RichTextInputRenderer extends InputRenderer {

    private String config;
    
    private Integer width;
    private Integer height;
    
    private Integer columns;
    private Integer rows;
    
    private boolean safe;
    private boolean showEditorOptions = true;
    
    public RichTextInputRenderer() {
        super();
        
        setColumns(50);
        setRows(10);
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

    public Integer getColumns() {
        return this.columns;
    }

    /**
     * The number of column of the textarea.
     * 
     * @property
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return this.rows;
    }

    /**
     * The number of rows of the textarea.
     * 
     * @property
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getHeight() {
        return this.height;
    }

    /**
     * Chooses the height in pixels of the rich text editor.
     * 
     * @property
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return this.width;
    }

    /**
     * Chooses the width in pixels of the rich text editor.
     * 
     * @property
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    public boolean isSafe() {
        return this.safe;
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

    public boolean isShowEditorOptions() {
		return showEditorOptions;
	}

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
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

			@Override
            public HtmlComponent createComponent(Object object, Class type) {
				HtmlContainer container = new HtmlBlockContainer();
				
                MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();

                TinyMceEditor editor = new TinyMceEditor(getConfig());
                editor.setValue((String) object);
                editor.setRows(getRows());
                editor.setColumns(getColumns());
                editor.setHeight(getHeight());
                editor.setWidth(getWidth());
                editor.setTargetSlot(key);
                
                editor.setName(key.toString());
                editor.setId(editor.getName());
                
                if (isSafe()) {
                    editor.setConverter(new SafeHtmlConverter());
                }
             
                if (isShowEditorOptions()) {
                	addEditorControls(container, editor);
                }
                container.addChild(editor);
                
                return container;
            }

			private void addEditorControls(HtmlContainer container, TinyMceEditor editor) {
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
    	    			"		tinyMCE.addMCEControl(element, id);\n"+
    	    			"	}\n" +
    	    			"}\n"
    				);
    	    	
    	    	HtmlRadioButtonList radioList = new HtmlRadioButtonList();
    	    	radioList.setName(getLocalName("controls", "editor"));
    	    	
    	    	HtmlRadioButton useEditor = radioList.addOption(new HtmlText(RenderUtils.getResourceString("renderers.rich-text.editor.add")), "activate");
    	    	HtmlRadioButton removeEditor = radioList.addOption(new HtmlText(RenderUtils.getResourceString("renderers.rich-text.editor.remove")), "disable");
    	    	
    	    	radioList.setClasses("liinline nobullet mbottom05"); // TODO: use style?
    	    	useEditor.setChecked(true);
    	    
    	    	useEditor.setOnClick(String.format("fenix_addEditor(['%s']);", editor.getId()));
    			removeEditor.setOnClick(String.format("fenix_removeEditor(['%s']);", editor.getId()));
    	    	
    	    	container.addChild(script);
                container.addChild(radioList);
                
                editor.setController(new DisableEditorController(editor, removeEditor));
			}
            
        };
    }

    private class DisableEditorController extends HtmlController {

		private static final long serialVersionUID = 1L;
		private HtmlRadioButton control;
		private TinyMceEditor editor;

		public DisableEditorController(TinyMceEditor editor, HtmlRadioButton removeEditor) {
			this.editor = editor;
			this.control = removeEditor;
		}

		@Override
		public void execute(IViewState viewState) {
			if (this.control.isChecked()) {
				this.editor.disable();
			}
		}
    	
    }
}
