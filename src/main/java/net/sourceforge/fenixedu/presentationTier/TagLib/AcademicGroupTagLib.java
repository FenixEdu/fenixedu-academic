package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.util.Collections;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import pt.ist.bennu.core.security.Authenticate;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class AcademicGroupTagLib extends TagSupport {

    private static final long serialVersionUID = -8050082985849930419L;

    private String operation;

    private AcademicProgram program;

    private AdministrativeOffice office;

    @Override
    public int doStartTag() throws JspException {
        Set<AcademicProgram> programs =
                program != null ? Collections.singleton(program) : Collections.<AcademicProgram> emptySet();
        Set<AdministrativeOffice> offices =
                office != null ? Collections.singleton(office) : Collections.<AdministrativeOffice> emptySet();
        AcademicAuthorizationGroup group =
                new AcademicAuthorizationGroup(AcademicOperationType.valueOf(operation), programs, offices);
        if (group.allows(Authenticate.getUser())) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public AcademicProgram getProgram() {
        return program;
    }

    public void setProgram(AcademicProgram program) {
        this.program = program;
    }

    public AdministrativeOffice getOffice() {
        return office;
    }

    public void setOffice(AdministrativeOffice office) {
        this.office = office;
    }
}
