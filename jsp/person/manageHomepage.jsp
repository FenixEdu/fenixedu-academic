<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2 class="mtop05"><bean:message key="title.manage.homepage" bundle="HOMEPAGE_RESOURCES"/></h2>

<div class="infoop2">
<p class="mvert0"><bean:message key="message.homepage.info" bundle="HOMEPAGE_RESOURCES"/></p>
</div>

<p><span class="error"><html:errors/></span></p>

<html:form action="/manageHomepage">
	<html:hidden property="method" value="submitHomepage"/>

	<p>
	<bean:message key="label.homepage.activated" bundle="HOMEPAGE_RESOURCES"/>
	<html:radio property="activated" value="true" onchange="this.form.submit()"/><bean:message key="label.homepage.activated.yes" bundle="HOMEPAGE_RESOURCES"/>
	<html:radio property="activated" value="false" onchange="this.form.submit()"/><bean:message key="label.homepage.activated.no" bundle="HOMEPAGE_RESOURCES"/>
	</p>
	

	<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
	<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

	<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="UserView" property="person.user.userUId"/></bean:define>
	<p>
	<bean:message key="person.homepage.adress" bundle="HOMEPAGE_RESOURCES"/>:
	<logic:notPresent name="UserView" property="person.homepage">
		<bean:write name="homepageURL"/>
	</logic:notPresent>
	<logic:present name="UserView" property="person.homepage">
		<logic:notPresent name="UserView" property="person.homepage.activated">
				<bean:write name="homepageURL"/>
		</logic:notPresent>
		<logic:present name="UserView" property="person.homepage.activated">
			<logic:equal name="UserView" property="person.homepage.activated" value="true">
				<html:link href="<%= homepageURL %>"><bean:write name="homepageURL"/></html:link>
			</logic:equal>
			<logic:equal name="UserView" property="person.homepage.activated" value="false">
				<bean:write name="homepageURL"/>
			</logic:equal>
		</logic:present>
	</logic:present>
	</p>


	<br/>

	
	<p><strong><bean:message key="label.homepage.components" bundle="HOMEPAGE_RESOURCES"/>:</strong></p>

	<table class="tform01">
	<tr><td class="leftcol"><bean:message key="label.homepage.name" bundle="HOMEPAGE_RESOURCES"/>:</td><td><html:text property="name" size="50"/><br />
		<bean:message key="label.homepage.name.instructions" bundle="HOMEPAGE_RESOURCES"/>
		</td>
	</tr>
	<logic:present name="UserView" property="person.employee.currentContract.workingUnit">
		<tr>
			<td class="leftcol">
				<bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:
			</td>
			<td>
				<html:checkbox property="showUnit" value="true"/>
				<bean:write name="UserView" property="person.employee.currentContract.workingUnit.name"/>
			</td>
		</tr>
	</logic:present>
	<logic:present name="UserView" property="person.teacher">
		<logic:present name="UserView" property="person.employee.currentContract">
			<tr>
				<td class="leftcol">
					<bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>
				</td>
				<td>
					<html:checkbox property="showCategory" value="true"/>
					<logic:present name="UserView" property="person.teacher">
						<bean:write name="UserView" property="person.teacher.category.longName"/>
					</logic:present>
				</td>
			</tr>
		</logic:present>
	</logic:present>
	<logic:notEmpty name="UserView" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<tr>
			<td class="leftcol">
				<bean:message key="label.homepage.showActiveStudentCurricularPlans" bundle="HOMEPAGE_RESOURCES"/>:
			</td>
			<td>
				<html:checkbox property="showActiveStudentCurricularPlans" value="true"/>
				<logic:iterate id="studentCurricularPlan" name="UserView" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
					<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
					<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="UserView" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
					<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
					<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
				</logic:iterate>
			</td>
		</tr>
	</logic:notEmpty>
	<logic:notEmpty name="UserView" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<tr>
			<td class="leftcol">
				<bean:message key="label.homepage.showAlumniDegrees" bundle="HOMEPAGE_RESOURCES"/>:
			</td>
			<td>
				<html:checkbox property="showAlumniDegrees" value="true"/>
				<logic:iterate id="studentCurricularPlan" name="UserView" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
					<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
					<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="UserView" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
					<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
					<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
				</logic:iterate>
			</td>
		</tr>
	</logic:notEmpty>
<!--
	<tr><td class="leftcol"><bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
			<html:checkbox property="showUnit" value="true"/>
			<logic:present name="UserView" property="person.employee.currentContract.workingUnit">
				<bean:write name="UserView" property="person.employee.currentContract.workingUnit.name"/>
			</logic:present>
			<logic:iterate id="student" name="UserView" property="person.students">
				<logic:present name="student" property="activeStudentCurricularPlan">
					<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.degree.presentationName"/>
				</logic:present>
			</logic:iterate>
		</td>
	</tr>
-->
	<tr><td class="leftcol"><bean:message key="label.homepage.showPhoto" bundle="HOMEPAGE_RESOURCES"/>:</td><td> <html:checkbox property="showPhoto" value="true"/> <html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" /> </td></tr>
	<tr><td class="leftcol"><bean:message key="label.homepage.showEmail" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td style="vertical-align: center;">
			<html:checkbox property="showEmail" value="true"/>
			<bean:write name="UserView" property="person.email"/>
			<%--
			<bean:define id="emailURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/publico/viewHomepage.do?method=emailPng&personID=<bean:write name="UserView" property="person.idInternal"/></bean:define>
			<html:img align="middle" src="<%= emailURL %>"/>
			 --%>
		</td>
	</tr>
	<tr><td class="leftcol"><bean:message key="label.homepage.showTelephone" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
			<html:checkbox property="showTelephone" value="true"/> <bean:write name="UserView" property="person.phone"/>
		</td>
	</tr>
	<tr><td class="leftcol"><bean:message key="label.homepage.showWorkTelephone" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
			<html:checkbox property="showWorkTelephone" value="true"/>
			<bean:write name="UserView" property="person.workPhone"/>
		</td>
	</tr>
	<tr><td class="leftcol"><bean:message key="label.homepage.showMobileTelephone" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
		<html:checkbox property="showMobileTelephone" value="true"/>
		<bean:write name="UserView" property="person.mobile"/>
		</td>
	</tr>
	
	<tr><td class="leftcol"><bean:message key="label.homepage.showAlternativeHomepage" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
			<html:checkbox property="showAlternativeHomepage" value="true"/>
			<bean:write name="UserView" property="person.webAddress"/>
		</td>
	</tr>
	
	<logic:present name="UserView" property="person.teacher">
	<logic:present name="UserView" property="person.employee.currentContract">
	<tr><td class="leftcol"><bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
			<html:checkbox property="showCurrentExecutionCourses" value="true"/>
			<logic:iterate id="executionCourse" name="UserView" property="person.teacher.currentExecutionCourses" length="1">
				<bean:write name="executionCourse" property="nome"/>
			</logic:iterate>
			<logic:iterate id="executionCourse" name="UserView" property="person.teacher.currentExecutionCourses" offset="1">, 
			<bean:write name="executionCourse" property="nome"/>
			</logic:iterate>
		</td>
	</tr>
	</logic:present>
	</logic:present>
	
	<tr><td class="leftcol"></td><td><html:submit styleClass="mvert05"><bean:message key="person.homepage.submit" bundle="HOMEPAGE_RESOURCES"/></html:submit></td></tr>
	</table>

</html:form>