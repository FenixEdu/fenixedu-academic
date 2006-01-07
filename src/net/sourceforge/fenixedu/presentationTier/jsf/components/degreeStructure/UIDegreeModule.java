package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class UIDegreeModule extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeModule";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeModule";

    protected DegreeModule degreeModule;
    protected Boolean onlyStructure;
    protected Boolean toEdit;
    protected int depth;
    protected String tabs;

    protected FacesContext facesContext;
    protected ResponseWriter writer;
    
    protected static final int BASE_DEPTH = UIDegreeCurricularPlan.BASE_DEPTH;
    
    public UIDegreeModule() {
        super();
        this.setRendererType(null);
    }

    public UIDegreeModule(DegreeModule degreeModule, Boolean toEdit, int depth, String tabs) {
        this();
        this.degreeModule = degreeModule;
        this.toEdit = toEdit;
        this.depth = depth;
        this.tabs = tabs;
    }

    public UIDegreeModule(DegreeModule degreeModule, Boolean onlyStructure, Boolean toEdit, int depth, String tabs) {
        this(degreeModule, toEdit, depth, tabs);
        this.onlyStructure = onlyStructure;
    }
    
    public String getFamily() {
        return UIDegreeModule.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        setFromAttributes();
        
        if (this.degreeModule instanceof CurricularCourse) {
            new UICurricularCourse(this.degreeModule, this.toEdit, this.depth, this.tabs, null).encodeBegin(facesContext);
        } else if (this.degreeModule instanceof CourseGroup) {
            new UICourseGroup(this.degreeModule, this.onlyStructure, this.toEdit, this.depth, this.tabs).encodeBegin(facesContext);
        }
    }
    
    private void setFromAttributes() {
        if (this.degreeModule == null) {
            this.degreeModule = (DegreeModule) this.getAttributes().get("degreeModule");
        }
        if (this.toEdit == null) {
            this.toEdit = (Boolean) this.getAttributes().get("toEdit");
        }
        if (this.tabs == null) {
            this.depth = UIDegreeModule.BASE_DEPTH;
            this.tabs = "";
        }
    }

    protected String getBundleValue(FacesContext facesContext, String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, facesContext.getViewRoot().getLocale());
        return bundle.getString(bundleKey);
    }

    protected void encodeLink(String href, String bundleKey) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        writer.write(this.getBundleValue(facesContext, "ServidorApresentacao/BolonhaManagerResources", bundleKey));
        writer.endElement("a");
    }

}
