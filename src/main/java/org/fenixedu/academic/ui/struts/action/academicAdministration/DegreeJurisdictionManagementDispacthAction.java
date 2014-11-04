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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.StringNormalizer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@StrutsFunctionality(app = AcademicAdministrationApplication.class, path = "degree-jurisdiction",
        titleKey = "label.degreeJurisdiction.title", accessGroup = "academic(MANAGE_AUTHORIZATIONS)")
@Mapping(path = "/degreeJurisdiction", module = "academicAdministration")
@Forwards({ @Forward(name = "manageJurisdictions", path = "/academicAdministration/degreeJurisdictions/manageJurisdictions.jsp") })
public class DegreeJurisdictionManagementDispacthAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward manageJurisdictions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        JsonArray programs = new JsonArray();

        for (Degree degree : Bennu.getInstance().getDegreesSet()) {
            JsonObject json = new JsonObject();
            json.addProperty("id", degree.getExternalId());
            json.addProperty("name", degree.getPresentationName());
            json.addProperty("acronym", degree.getSigla());
            json.addProperty("office", degree.getAdministrativeOffice().getExternalId());
            programs.add(json);
        }

        for (PhdProgram program : Bennu.getInstance().getPhdProgramsSet()) {
            JsonObject json = new JsonObject();
            json.addProperty("id", program.getExternalId());
            json.addProperty("name", program.getPresentationName());
            json.addProperty("acronym", program.getAcronym());
            json.addProperty("office", program.getAdministrativeOffice().getExternalId());
            programs.add(json);
        }

        request.setAttribute("programs", programs);

        JsonArray offices = new JsonArray();

        int i = 0;
        for (AdministrativeOffice office : Bennu.getInstance().getAdministrativeOfficesSet()) {
            JsonObject json = new JsonObject();
            json.addProperty("id", office.getExternalId());
            json.addProperty("name", office.getName().getContent());
            json.addProperty("acronym", acronym(office.getUnit().getName()));
            json.addProperty("idx", i++);
            offices.add(json);
        }

        request.setAttribute("offices", offices);
        return mapping.findForward("manageJurisdictions");
    }

    // Unit.getAcronym is not reliable
    private String acronym(String name) {
        StringBuilder builder = new StringBuilder();
        for (char c : StringNormalizer.normalizeAndRemoveAccents(name).toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public ActionForward changeDegreeJurisdiction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        JsonObject json = new JsonParser().parse(request.getReader()).getAsJsonObject();
        String programOid = json.get("program").getAsString();
        String officeOid = json.get("office").getAsString();
        changeDegreeJurisdiction(programOid, officeOid);
        return null;
    }

    @Atomic
    private void changeDegreeJurisdiction(String programOid, String officeOid) {
        AcademicProgram program = FenixFramework.getDomainObject(programOid);
        AdministrativeOffice office = FenixFramework.getDomainObject(officeOid);

        program.setAdministrativeOffice(office);
        if (program instanceof Degree) {
            Degree degree = (Degree) program;
            if (degree.getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA && degree.getPhdProgram() != null) {
                degree.getPhdProgram().setAdministrativeOffice(office);
            }
        }
    }
}
