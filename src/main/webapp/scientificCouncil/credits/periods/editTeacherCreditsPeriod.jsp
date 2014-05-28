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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present role="role(SCIENTIFIC_COUNCIL)">

	<h2><bean:message key="label.edit.credits.period"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	<html:messages id="message" message="true">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:write name="message"/>
			</span>
		</p>
	</html:messages>
	
	<logic:notEmpty name="teacherCreditsBean">
		
		<bean:define id="schemaName" value="" />
		<logic:equal name="teacherCreditsBean" property="teacher" value="true">
			<h3 class="mtop15 mbottom05"><bean:message key="label.teacher"/></h3>
			<bean:define id="schemaName" value="teacher.credits.period.view" />
		</logic:equal>
		<logic:equal name="teacherCreditsBean" property="teacher" value="false">
			<h3 class="mtop15 mbottom05"><bean:message key="label.department.adm.office"/></h3>
			<bean:define id="schemaName" value="departmentAdmOffice.credits.period.view" />
		</logic:equal>
		
		<bean:define id="URL">/defineCreditsPeriods.do?executionPeriodId=<bean:write name="teacherCreditsBean" property="executionPeriod.externalId"/></bean:define>
		
		<fr:form action="<%= URL %>">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="creditsPeriodForm" value="editPeriod"/> 

			<fr:edit id="teacherCreditsBeanID" name="teacherCreditsBean" schema="<%= schemaName %>"	>				
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>				
			</fr:edit>
			<html:submit><bean:message key="submit"/></html:submit>
			<html:cancel onclick="this.form.method.value='showPeriods';this.form.submit();"><bean:message key="button.cancel"/></html:cancel>
		</fr:form>
			
	</logic:notEmpty>	
</logic:present>