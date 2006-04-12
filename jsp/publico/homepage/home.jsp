<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="homepage">
	<logic:equal name="homepage" property="activated" value="true">

		<h1 id="no">
			<%-- <bean:message bundle="HOMEPAGE_RESOURCES" key="title.homepage.of"/>: --%>
			<bean:write name="homepage" property="name"/>
		</h1>

		<logic:equal name="homepage" property="showPhoto" value="true">
			<bean:define id="homepageID" name="homepage" property="idInternal"/>
			<p>
				<html:img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&homepageID=" + homepageID.toString() %>"/>
			</p>
		</logic:equal>

		<logic:equal name="homepage" property="showUnit" value="true">
		<p><bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:
			<logic:present name="homepage" property="person.employee.currentContract.workingUnit">
				<bean:write name="homepage" property="person.employee.currentContract.workingUnit.name"/>
				<br/>
			</logic:present>
			<logic:iterate id="student" name="homepage" property="person.students">
				<logic:present name="student" property="activeStudentCurricularPlan">
					<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.degree.presentationName"/>
					<br/>
				</logic:present>
			</logic:iterate>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showCategory" value="true">
		<p><bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentContract">
					<bean:write name="homepage" property="person.teacher.category.longName"/>
					<br/>
				</logic:present>
			</logic:present>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showEmail" value="true">
		<p><bean:message key="label.homepage.showEmail" bundle="HOMEPAGE_RESOURCES"/>:
			<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
			<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
			<bean:define id="emailURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/publico/viewHomepage.do?method=emailPng&personID=<bean:write name="homepage" property="person.idInternal"/></bean:define>
			<html:img align="middle" src="<%= emailURL %>"/>
			<br/>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showTelephone" value="true">
		<p><bean:message key="label.homepage.showTelephone" bundle="HOMEPAGE_RESOURCES"/>:
			<bean:write name="homepage" property="person.phone"/>
			<br/>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showWorkTelephone" value="true">
		<p><bean:message key="label.homepage.showWorkTelephone" bundle="HOMEPAGE_RESOURCES"/>:
			<bean:write name="homepage" property="person.workPhone"/>
			<br/>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showMobileTelephone" value="true">
		<p><bean:message key="label.homepage.showMobileTelephone" bundle="HOMEPAGE_RESOURCES"/>:
			<bean:write name="homepage" property="person.mobile"/>
			<br/>
		</p>
		</logic:equal>
		
		<logic:equal name="homepage" property="showAlternativeHomepage" value="true">
		<p><bean:message key="label.homepage.showAlternativeHomepage" bundle="HOMEPAGE_RESOURCES"/>:
			<bean:write name="homepage" property="person.webAddress"/>
			<br/>
		</p>
		</logic:equal>


<!--
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
			<br/>
		</logic:equal>
-->
		<logic:equal name="homepage" property="showCurrentExecutionCourses" value="true">
		<p><bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentContract">
					<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" length="1">
						<bean:write name="executionCourse" property="nome"/>
					</logic:iterate>
					<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" offset="1">
						, <bean:write name="executionCourse" property="nome"/>
					</logic:iterate>
					<br/>
				</logic:present>
			</logic:present>
		</p>
		</logic:equal>
		
	</logic:equal>
</logic:present>