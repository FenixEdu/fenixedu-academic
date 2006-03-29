<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="homepage">
	<logic:equal name="homepage" property="activated" value="true">

		<h1>
			<bean:message bundle="HOMEPAGE_RESOURCES" key="title.homepage.of"/>:
			<bean:write name="homepage" property="name"/>
		</h1>

		<br/>
		<br/>

		<logic:equal name="homepage" property="showPhoto" value="true">
			<br/>
		</logic:equal>
		<logic:equal name="homepage" property="showUnit" value="true">
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
		</logic:equal>
		<logic:equal name="homepage" property="showCategory" value="true">
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentContract">
					<bean:write name="homepage" property="person.teacher.category.longName"/>
					<br/>
				</logic:present>
			</logic:present>
		</logic:equal>
		<logic:equal name="homepage" property="showEmail" value="true">
			<bean:write name="homepage" property="person.email"/>
			<br/>
		</logic:equal>
		<logic:equal name="homepage" property="showTelephone" value="true">
			<bean:write name="homepage" property="person.phone"/>
			<br/>
		</logic:equal>
		<logic:equal name="homepage" property="showWorkTelephone" value="true">
			<bean:write name="homepage" property="person.workPhone"/>
			<br/>
		</logic:equal>
		<logic:equal name="homepage" property="showMobileTelephone" value="true">
			<bean:write name="homepage" property="person.mobile"/>
			<br/>
		</logic:equal>
		<logic:equal name="homepage" property="showAlternativeHomepage" value="true">
			<bean:write name="homepage" property="person.webAddress"/>
			<br/>
		</logic:equal>
<!--
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
			<br/>
		</logic:equal>
-->
		<logic:equal name="homepage" property="showCurrentExecutionCourses" value="true">
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentContract">
					<logic:iterate id="executionCourse" name="homepage" property="person.homepage.currentExecutionCourses" length="1">
						<bean:write name="executionCourse" property="nome"/>
					</logic:iterate>
					<logic:iterate id="executionCourse" name="homepage" property="person.homepage.currentExecutionCourses" offset="1">
						, <bean:write name="executionCourse" property="nome"/>
					</logic:iterate>
					<br/>
				</logic:present>
			</logic:present>
		</logic:equal>

	</logic:equal>
</logic:present>