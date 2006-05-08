<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="string" %>

<logic:present name="homepage">
	<logic:equal name="homepage" property="activated" value="true">

		<h1 id="no">
			<%-- <bean:message bundle="HOMEPAGE_RESOURCES" key="title.homepage.of"/>: --%>
			<bean:write name="homepage" property="name"/>
		</h1>

		<br/>
		
		<logic:equal name="homepage" property="showPhoto" value="true">
			<bean:define id="homepageID" name="homepage" property="idInternal"/>
			<p>
				<html:img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&homepageID=" + homepageID.toString() %>"/>
			</p>
		</logic:equal>
	

		<logic:equal name="homepage" property="showUnit" value="true">
		<p>
			<!--<bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:-->
			<logic:present name="homepage" property="person.employee.currentContract.workingUnit">
				<bean:define id="currentUnit" name="homepage" property="person.employee.currentContract.workingUnit" toScope="request"/>
				<jsp:include page="unitStructure.jsp"/>
			</logic:present>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showCategory" value="true">
		<p>
			<!--<bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:-->
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentContract">
					<string:capitalizeAllWords>
						<string:lowerCase>
							<bean:write name="homepage" property="person.teacher.category.longName"/>
						</string:lowerCase>
					</string:capitalizeAllWords>
					<br/>
				</logic:present>
			</logic:present>
		</p>
		</logic:equal>

		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
			<!--<bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:-->
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentContract">
					<logic:present name="homepage" property="researchUnitHomepage">
						<logic:present name="homepage" property="researchUnit">
							<bean:define id="researchUnitHomepage" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
							<logic:match name="homepage" property="researchUnitHomepage" value="http://"></logic:match>
								<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
								<p>
									<bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:
									<html:link href="<%= url %>">
										<bean:write name="homepage" property="researchUnit.content"/> 
									</html:link>
								</p>
							</logic:match>
							<logic:notMatch name="homepage" property="researchUnitHomepage" value="http://"></logic:match>
								<logic:match name="homepage" property="researchUnitHomepage" value="https://"></logic:match>
									<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
									<p>
										<bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:
										<html:link href="<%= url %>">
											<bean:write name="homepage" property="researchUnit.content"/> 
										</html:link>
									</p>
								</logic:match>
								<logic:notMatch name="homepage" property="researchUnitHomepage" value="http://"></logic:match>
									<bean:define id="url" type="java.lang.String">http://<bean:write name="homepage" property="researchUnitHomepage"/></bean:define>
									<p>
										<bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:
										<html:link href="<%= url %>">
											<bean:write name="homepage" property="researchUnit.content"/> 
										</html:link>
									</p>
								</logic:notMatch>
							</logic:notMatch>
						</logic:present>
					</logic:present>
				</logic:present>
			</logic:present>
		</logic:equal>

		<logic:equal name="homepage" property="showActiveStudentCurricularPlans" value="true">
			<p>
				<bean:message key="label.homepage.showActiveStudentCurricularPlans" bundle="HOMEPAGE_RESOURCES"/>:
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
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
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
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
			</p>
		</logic:equal>

		<logic:notEmpty name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
			<logic:equal name="homepage" property="showCurrentAttendingExecutionCourses" value="true">
				<p>
					<bean:message key="label.homepage.showCurrentAttendingExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:
					<logic:iterate id="attend" name="homepage" property="person.currentAttends" length="1">
						<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
						<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=<bean:write name="executionCourse" property="idInternal"/></bean:define>
						<html:link href="<%= url %>">
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</logic:iterate>
					<logic:iterate id="attend" name="homepage" property="person.currentAttends" offset="1">
						,
						<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
						<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=<bean:write name="executionCourse" property="idInternal"/></bean:define>
						<html:link href="<%= url %>">
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</logic:iterate>
				</p>
			</logic:equal>
		</logic:notEmpty>

		<logic:equal name="homepage" property="showAlumniDegrees" value="true">
			<p>
				<bean:message key="label.homepage.showAlumniDegrees" bundle="HOMEPAGE_RESOURCES"/>:
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
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
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
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
			</p>
		</logic:equal>

		<logic:equal name="homepage" property="showEmail" value="true">
		<p>
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
			<bean:define id="url" type="java.lang.String" name="homepage" property="person.webAddress"/>
			<html:link href="<%= url %>">
				<bean:write name="homepage" property="person.webAddress"/>
			</html:link>
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
						<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=<bean:write name="executionCourse" property="idInternal"/></bean:define>
						<html:link href="<%= url %>">
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</logic:iterate>
					<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" offset="1">
						,
						<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=<bean:write name="executionCourse" property="idInternal"/></bean:define>
						<html:link href="<%= url %>">
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</logic:iterate>
					<br/>
				</logic:present>
			</logic:present>
		</p>
		</logic:equal>
		
	</logic:equal>
</logic:present>