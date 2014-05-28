<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniManageFormation.jsp -->
<h2><bean:message key="link.qualification" bundle="ALUMNI_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p class="mbottom05 mtop15"><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<bean:define id="typeSchema" name="alumniFormation" property="typeSchema" type="java.lang.String" />
<bean:define id="institutionSchema" name="alumniFormation" property="institutionSchema" type="java.lang.String" />

<logic:present name="formationUpdate">
	<fr:form id="createFormationForm" action="/formation.do?method=manageAlumniQualification">

		<fr:edit id="alumniFormation" name="alumniFormation" visible="false"/>
	
		<fr:edit id="alumniFormationDegree" name="alumniFormation" schema="<%= typeSchema %>" >
			<fr:destination name="updateFormationTypePostback" path="/formation.do?method=updateAlumniFormationTypePostback"/>
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright width100 mbottom0"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<table class="tstyle5 thlight thmiddle thright mvert0">
		<tr>
			<th class="width12em"><bean:message key="label.formation.education.area" bundle="ALUMNI_RESOURCES" />:</th>
			<td>
			<select name="formationEducationArea">
				<logic:iterate id="alumniEducationArea" name="alumniFormation" property="allAreas">
					
					<bean:define id="areaExternalId" name="alumniEducationArea" property="educationArea.externalId" />
					<bean:define id="areaCode" name="alumniEducationArea" property="codeIndentationValue" type="java.lang.String" />
					<bean:define id="areaDescription" name="alumniEducationArea" property="educationArea.description" type="java.lang.String" />
	
					<logic:notEmpty name="alumniEducationArea" property="educationArea.childAreas">
						<optgroup label="<%= areaCode + " " + areaDescription %>" >
						</optgroup>
					</logic:notEmpty>
					
					<logic:empty name="alumniEducationArea" property="educationArea.childAreas">
						<logic:equal name="formationEducationArea" value="<%= areaExternalId.toString() %>">
							<option value="<%= areaExternalId %>" selected="selected">
								<%= areaCode + " " + areaDescription %>
							</option>
						</logic:equal>
						<logic:notEqual name="formationEducationArea" value="<%= areaExternalId.toString() %>">
							<option value="<%= areaExternalId %>">
								<%= areaCode + " " + areaDescription %>
							</option>
						</logic:notEqual>
					</logic:empty>
	
				</logic:iterate>
			</select>
			</td>
		</tr>
		</table>

		<fr:edit id="alumniFormationInstitution" name="alumniFormation" schema="<%= institutionSchema %>" >
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright width100 mvert0"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="updateInstitutionTypePostback" path="/formation.do?method=updateAlumniFormationInfoPostback"/>
		</fr:edit>
	
	
		<fr:edit name="alumniFormation" schema="alumni.formation.info">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright width100 mtop0"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="cancel" path="/formation.do?method=innerFormationManagement"/>
		</fr:edit>
		
		<html:submit>
			<bean:message key="label.submit" bundle="ALUMNI_RESOURCES" />
		</html:submit>
		<html:cancel>
			<bean:message key="label.cancel" bundle="ALUMNI_RESOURCES" />
		</html:cancel>

	</fr:form>
</logic:present>


