package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityStage;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;


public abstract class ResearchActivityStageRenderer extends OutputRenderer {
    
    private String subLayout;
    private String subSchema;

    private Map<String, String> typeClasses;
    
    public ResearchActivityStageRenderer() {
	super();
	
	this.typeClasses = new Hashtable<String, String>();
    }

    public String getEnumClasses(String name) {
	return this.typeClasses.get(name);
    }
    
    public void setEnumClasses(String name, String value) {
	this.typeClasses.put(name, value);
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {


	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
		MetaObject metaObject = MetaObjectFactory.createObject(object, schema);
		PresentationContext context = getContext().createSubContext(metaObject);
		context.setLayout(getSubLayout());
		
		HtmlComponent component = RenderKit.getInstance().render(context, object, type);
		
		return component;
	    }
	    
	    @Override
	    public void applyStyle(HtmlComponent component) {
		component.setClasses(getEnumClasses(getResearchActivityStage().toString()));
	        component.setTitle(getTitle());
	    }
	    
	};
	
    }

    public String getSubLayout() {
        return subLayout;
    }

    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }
    
    public abstract ResearchActivityStage getResearchActivityStage();

}
