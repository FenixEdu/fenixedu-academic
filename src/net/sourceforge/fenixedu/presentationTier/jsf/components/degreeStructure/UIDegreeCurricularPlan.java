package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public class UIDegreeCurricularPlan extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeCurricularPlan";

    protected static final int BASE_DEPTH = -1;
    
    public UIDegreeCurricularPlan() {
        super();
        this.setRendererType(null);
    }

    public String getFamily() {
        return UIDegreeCurricularPlan.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

        final IDegreeCurricularPlan dcp = (IDegreeCurricularPlan) this.getAttributes().get("dcp");
        if (!dcp.getCurricularStage().equals(CurricularStage.OLD)) {
            final Boolean toEdit = this.getToEdit();
            
            StringBuffer dcpBuffer = new StringBuffer();
            dcpBuffer.append("[DCP ").append(dcp.getIdInternal()).append("] ").append(dcp.getName());
            System.out.println(dcpBuffer);
            
            new UIDegreeModule(dcp.getDegreeModule(), toEdit, BASE_DEPTH, "").encodeBegin(context);
        }
    }

    private Boolean getToEdit() {
        if (this.getAttributes().get("toEdit") instanceof Boolean) {
            return (Boolean) this.getAttributes().get("toEdit");
        } else {
            return Boolean.valueOf((String) this.getAttributes().get("toEdit"));
        }
    }

}
