/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.util.Collections;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

import org.fenixedu.bennu.core.security.Authenticate;

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
                AcademicAuthorizationGroup.get(AcademicOperationType.valueOf(operation), programs, offices, null);
        if (group.isMember(Authenticate.getUser())) {
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
