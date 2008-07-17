<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniManageFormation.jsp -->
<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
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
					
					<bean:define id="areaIdInternal" name="alumniEducationArea" property="educationArea.idInternal" />
					<bean:define id="areaCode" name="alumniEducationArea" property="codeIndentationValue" type="java.lang.String" />
					<bean:define id="areaDescription" name="alumniEducationArea" property="educationArea.description" type="java.lang.String" />
	
					<logic:notEmpty name="alumniEducationArea" property="educationArea.childAreas">
						<optgroup label="<%= areaCode + " " + areaDescription %>" >
						</optgroup>
					</logic:notEmpty>
					
					<logic:empty name="alumniEducationArea" property="educationArea.childAreas">
						<logic:equal name="formationEducationArea" value="<%= areaIdInternal.toString() %>">
							<option value="<%= areaIdInternal %>" selected="selected">
								<%= areaCode + " " + areaDescription %>
							</option>
						</logic:equal>
						<logic:notEqual name="formationEducationArea" value="<%= areaIdInternal.toString() %>">
							<option value="<%= areaIdInternal %>">
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


