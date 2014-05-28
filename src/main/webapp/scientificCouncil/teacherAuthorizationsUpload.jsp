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
<%@page import="net.sourceforge.fenixedu.domain.teacher.CategoryType"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@ page import="net.sourceforge.fenixedu.domain.Department"%>
<%@ page import="net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory"%>
<html:xhtml/>

<h2><bean:message key="label.upload.authorizations" /></h2>

<logic:present name="error">
	<span class="error"><bean:message key="label.error.authorization.teacher" /></span>
</logic:present>

<div class="infoop2">
<p class="mvert0">
	<bean:message key="teacherAuthorization.upload.instructions.paragraph1"/>
	<ul>
		<li>
			<bean:message key="teacherAuthorization.upload.instructions.column"/> 1: <bean:message key="teacherAuthorization.upload.instructions.column.username"/>
		</li>
		<li>
			<bean:message key="teacherAuthorization.upload.instructions.column"/> 2: <bean:message key="teacherAuthorization.upload.instructions.column.professionalCategory"/>
		</li>
		<li>
			<bean:message key="teacherAuthorization.upload.instructions.column"/> 3: <bean:message key="teacherAuthorization.upload.instructions.column.lessonHours"/>
		</li>
		<li>
			<bean:message key="teacherAuthorization.upload.instructions.column"/> 4: <bean:message key="teacherAuthorization.upload.instructions.column.carParkAccess"/>
		</li>
		<li>
			<bean:message key="teacherAuthorization.upload.instructions.column"/> 5: <bean:message key="teacherAuthorization.upload.instructions.column.identificationCard"/>
		</li>
		<li>
			<bean:message key="teacherAuthorization.upload.instructions.column"/> 6: <bean:message key="teacherAuthorization.upload.instructions.column.department"/>
		</li>
	</ul>
</p>
</div>

<fr:form action="/teacherAuthorization.do" encoding="multipart/form-data">
	<html:hidden property="method" value="upload"/>
	
	<fr:edit id="teacherAuthorizationsUploadBean" name="teacherAuthorizationsUploadBean">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.TeacherAuthorizationManagement$TeacherAuthorizationsUploadBean">
			
			<fr:slot name="executionSemester" key="label.exeuctionSemester" layout="menu-select"  required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider" />
				<fr:property name="format" value="${executionYear.year} - ${semester}º semestre" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>

			<fr:slot name="inputStream" key="label.file" required="true" bundle="APPLICATION_RESOURCES">
				<fr:property name="fileNameSlot" value="filename"/>
			</fr:slot>
			
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
	</fr:edit>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
		</html:submit>
	
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" onclick="this.form.method.value='list';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/>	
		</html:cancel>
	</p>
</fr:form>

<div class="infoop2">
<p class="mvert0">
	<bean:message key="teacherAuthorization.upload.instructions.column.department.possible.values"/>
	<ul>
		<% for (final Department department : Bennu.getInstance().getDepartmentsSet()) { %>
			<li>
				<%= department.getAcronym() %>
			</li>
		<% } %>
	</ul>
	<bean:message key="teacherAuthorization.upload.instructions.column.professionalCategory.possible.values"/>
	<ul>
		<%
			for (final ProfessionalCategory professionalCategory : Bennu.getInstance().getProfessionalCategoriesSet()) {
			    if (professionalCategory.getCategoryType() == CategoryType.TEACHER) {
		%>
			<li>
				<%= professionalCategory.getName().getContent() %>
			</li>
		<%
			    }
			}
		%>
	</ul>
</p>
</div>
