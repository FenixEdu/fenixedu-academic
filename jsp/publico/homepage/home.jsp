<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="string" %>

<logic:present name="homepage">
	<logic:equal name="homepage" property="activated" value="true">

	<logic:present name="homepage" property="person.employee.currentDepartmentWorkingPlace">
		<bean:define id="institutionUrl" type="java.lang.String">
			<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
		</bean:define>
		<bean:define id="institutionUrlStructure" type="java.lang.String">
			<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
			<bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
		</bean:define>
		<bean:define id="departmentUrl" type="java.lang.String">
			<bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.webAddress"/>
		</bean:define>
		<bean:define id="departmentUnitID" type="java.lang.String">
			<bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.idInternal"/>
		</bean:define>

		<div class="breadcumbs mvert0">
			<html:link href="<%= institutionUrl %>">
				<bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/>
			</html:link>
			&nbsp;&gt;&nbsp;
			<html:link href="<%=institutionUrlStructure%>">
				<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="structure"/>
			</html:link>
			&nbsp;&gt;&nbsp;
			<html:link href="department/showDepartments.faces">
				<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="academic.units"/>
			</html:link>
			&nbsp;&gt;&nbsp;			
			<html:link href="<%=departmentUrl%>">
				<bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.realName"/>
			</html:link>
			&nbsp;&gt;&nbsp;			
			<html:link href="<%= "department/showDepartmentTeachers.faces?selectedDepartmentUnitID=" + departmentUnitID %>">
				<bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="department.faculty"/>
			</html:link>
			&nbsp;&gt;&nbsp;			
			<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.home"/>
		</div>
	</logic:present>

		<h1 id="no">
			<%-- <bean:message bundle="HOMEPAGE_RESOURCES" key="title.homepage.of"/>: --%>
			<bean:write name="homepage" property="name"/>
		</h1>


	<table class="invisible thleft">
		<!-- photo -->
		<logic:equal name="homepage" property="showPhoto" value="true">
			<bean:define id="homepageID" name="homepage" property="idInternal"/>
			<tr><th></th><td><html:img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>" style="padding: 1em 0;" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></td></tr>
		</logic:equal>

		<!-- units -->
		<logic:equal name="homepage" property="showUnit" value="true">
		<tr>
			<th><bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:present name="homepage" property="person.employee.currentWorkingContract.workingUnit">
					<bean:define id="currentUnit" name="homepage" property="person.employee.currentWorkingContract.workingUnit" toScope="request"/>
					<jsp:include page="unitStructure.jsp"/>
				</logic:present>
			</td>
		</tr>
		</logic:equal>
		
		<!-- categories -->
		<logic:equal name="homepage" property="showCategory" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentWorkingContract">
					<string:capitalizeAllWords>
						<string:lowerCase>
							<bean:write name="homepage" property="person.teacher.category.name.content"/>
						</string:lowerCase>
					</string:capitalizeAllWords>
				</logic:present>
			</logic:present>
			</td>
		</tr>
		</logic:equal>
		

		<!-- research unit -->
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
		<tr>
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentWorkingContract">
					<logic:present name="homepage" property="researchUnitHomepage">
						<logic:present name="homepage" property="researchUnit">
							<bean:define id="researchUnitHomepage" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
							<logic:match name="homepage" property="researchUnitHomepage" value="http://">
								<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
								<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
								<td>
									<html:link href="<%= url %>">
										<bean:write name="homepage" property="researchUnit.content"/> 
									</html:link>
								</td>
							</logic:match>
							<logic:notMatch name="homepage" property="researchUnitHomepage" value="http://">
								<logic:match name="homepage" property="researchUnitHomepage" value="https://">
									<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
									<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
									<td>
										<html:link href="<%= url %>">
											<bean:write name="homepage" property="researchUnit.content"/> 
										</html:link>
									</td>
								</logic:match>
								<logic:notMatch name="homepage" property="researchUnitHomepage" value="http://">
									<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
									<td>
										<bean:define id="url" type="java.lang.String">http://<bean:write name="homepage" property="researchUnitHomepage"/></bean:define>
										<html:link href="<%= url %>">
											<bean:write name="homepage" property="researchUnit.content"/> 
										</html:link>
									</td>
								</logic:notMatch>
							</logic:notMatch>
						</logic:present>
					</logic:present>
				</logic:present>
			</logic:present>
			
		</tr>
		</logic:equal>


		<!--  -->
		<logic:equal name="homepage" property="showActiveStudentCurricularPlans" value="true">
		<tr>
			<th><bean:message key="label.homepage.showActiveStudentCurricularPlans" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					,
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
			</td>
		</tr>
		</logic:equal>


		<!--  -->
		<logic:notEmpty name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<logic:equal name="homepage" property="showCurrentAttendingExecutionCourses" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCurrentAttendingExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="attend" name="personAttends" length="1">
					<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="attend" name="personAttends" offset="1">
					,
					<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</logic:iterate>
			</td>
		</tr>
		</logic:equal>
		</logic:notEmpty>


		<!--  -->
		<logic:equal name="homepage" property="showAlumniDegrees" value="true">
		<tr>
			<th><bean:message key="label.homepage.showAlumniDegrees" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					,
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
			</td>
		</tr>
		</logic:equal>
		
		
		<!--  E-mail -->
		<logic:equal name="homepage" property="showEmail" value="true">
		<tr>
			<th>E-mail:</th>
			<td>
				<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
				<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
				<bean:define id="emailURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/publico/viewHomepage.do?method=emailPng&amp;personID=<bean:write name="homepage" property="person.idInternal"/></bean:define>
				<html:img align="middle" src="<%= emailURL %>" altKey="email" bundle="IMAGE_RESOURCES"/>
			</td>
		</tr>
		</logic:equal>
		
		
		<!--  Telephone-->
		<logic:equal name="homepage" property="showTelephone" value="true">
		<tr>
			<th><bean:message key="label.homepage.showTelephone" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td><bean:write name="homepage" property="person.phone"/></td>
		</tr>
		</logic:equal>
		
	
		<!--  -->
		<logic:equal name="homepage" property="showWorkTelephone" value="true">
		<tr>
			<th><bean:message key="label.homepage.showWorkTelephone" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td><bean:write name="homepage" property="person.workPhone"/></td>
		</tr>
		</logic:equal>
		
		<!--  -->
		<logic:equal name="homepage" property="showMobileTelephone" value="true">
		<tr>
			<th><bean:message key="label.homepage.showMobileTelephone" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td><bean:write name="homepage" property="person.mobile"/></td>
		</tr>
		</logic:equal>
		
		<!--  -->
		<logic:equal name="homepage" property="showAlternativeHomepage" value="true">
		<tr>
			<th><bean:message key="label.homepage.showAlternativeHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<bean:define id="url" type="java.lang.String" name="homepage" property="person.webAddress"/>
				<html:link href="<%= url %>">
					<bean:write name="homepage" property="person.webAddress"/>
				</html:link>
			</td>
		</tr>
		</logic:equal>
		

		<!--  -->
		<logic:equal name="homepage" property="showCurrentExecutionCourses" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:present name="homepage" property="person.teacher">
					<logic:present name="homepage" property="person.employee.currentWorkingContract">
						<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" length="1">
							<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
							<html:link href="<%= url %>">
								<bean:write name="executionCourse" property="nome"/>
							</html:link>
						</logic:iterate>
						<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" offset="1">
							,
							<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
							<html:link href="<%= url %>">
								<bean:write name="executionCourse" property="nome"/>
							</html:link>
						</logic:iterate>
					</logic:present>
				</logic:present>
			</td>
		</tr>
		</logic:equal>
		
						
		</table>
		



<!--
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
			<br/>
		</logic:equal>
-->

		
	</logic:equal>
</logic:present>
