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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.create.authorization" /></h2>


<html:messages id="message" message="true">
	<span class="error"><bean:write name="message"/></span>
</html:messages>

<fr:form action="/teacherAuthorization.do">
	
	<fr:edit id="bean" name="bean">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.TeacherAuthorizationManagement$TeacherAuthorizationManagementBean">
			
			<fr:slot name="istUsername" key="label.istid" required="true" />
			
			<fr:slot name="lessonHours" key="label.lessonHours" required="true" />
			
			<fr:slot name="professionalCategory" key="label.professionalCategory" layout="menu-select"  required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.TeacherProfessionalCategoryProvider" />
				<fr:property name="format" value="${name}" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
			
			<fr:slot name="executionSemester" key="label.exeuctionSemester" layout="menu-select"  required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider" />
				<fr:property name="format" value="${executionYear.year} - ${semester}º semestre" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
			
			<fr:slot name="department" key="department" layout="menu-select"  required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DepartmentsProvide" />
				<fr:property name="format" value="${realName}" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
			
			<fr:slot name="canPark" key="label.canPark" />
			
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
	</fr:edit>
	<html:hidden property="method" value="create"/>
	
	<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
	</html:submit>
	
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" onclick="this.form.method.value='list';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/>	
		</html:cancel>
	</p>
</fr:form>

	
	