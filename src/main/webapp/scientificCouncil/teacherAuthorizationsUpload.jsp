<%@page import="net.sourceforge.fenixedu.domain.teacher.CategoryType"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject"%>
<%@ page import="net.sourceforge.fenixedu.domain.Department"%>
<%@ page import="net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory"%>
<html:xhtml/>

<em><bean:message key="scientificCouncil" /></em>
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
		<% for (final Department department : RootDomainObject.getInstance().getDepartmentsSet()) { %>
			<li>
				<%= department.getAcronym() %>
			</li>
		<% } %>
	</ul>
	<bean:message key="teacherAuthorization.upload.instructions.column.professionalCategory.possible.values"/>
	<ul>
		<%
			for (final ProfessionalCategory professionalCategory : RootDomainObject.getInstance().getProfessionalCategoriesSet()) {
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
