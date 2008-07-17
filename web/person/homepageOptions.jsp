<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>

<html:xhtml/>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="title.manage.homepage" bundle="HOMEPAGE_RESOURCES"/></h2>

<div class="infoop2">
    <p class="mvert0"><bean:message key="message.homepage.activation" bundle="HOMEPAGE_RESOURCES"/></p>
</div>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:notPresent name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId">
	<span class="error">
		<bean:message key="message.resource.not.available.for.external.users" bundle="HOMEPAGE_RESOURCES"/>
	</span>
</logic:notPresent>

<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId">
<html:form action="/manageHomepage">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeHomepageOptions"/>


    <logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage">
        <logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage.activated">
            <logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage.activated" value="true">
				<p>
					<span class="success0">
						<bean:message key="label.homepage.activated.afirmative" bundle="HOMEPAGE_RESOURCES"/>.
					</span>
				</p>
            </logic:equal>
        </logic:present>
    </logic:present>


    <p>
        <bean:message key="label.homepage.activated" bundle="HOMEPAGE_RESOURCES"/>
        <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.activated" property="activated" value="true" ondblclick="this.form.submit();" onclick="this.form.submit();"/><bean:message key="label.homepage.activated.yes" bundle="HOMEPAGE_RESOURCES"/>
        <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.activated" property="activated" value="false" ondblclick="this.form.submit();" onclick="this.form.submit();"/><bean:message key="label.homepage.activated.no" bundle="HOMEPAGE_RESOURCES"/>
        <html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
            <bean:message key="button.submit"/>
        </html:submit>
    </p>
    

    <% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
    <% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

    <bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.user.userUId"/></bean:define>
    <p>
    <bean:message key="person.homepage.adress" bundle="HOMEPAGE_RESOURCES"/>:
    <logic:notPresent name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage">
        <bean:write name="homepageURL"/>
    </logic:notPresent>
    <logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage">
        <logic:notPresent name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage.activated">
                <bean:write name="homepageURL"/>
        </logic:notPresent>
        <logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage.activated">
            <logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage.activated" value="true">
                <html:link href="<%= homepageURL %>"><bean:write name="homepageURL"/></html:link>
            </logic:equal>
            <logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.homepage.activated" value="false">
                <bean:write name="homepageURL"/>
            </logic:equal>
        </logic:present>
    </logic:present>
    </p>


	<p>
        <h3 class="mtop2"><bean:message key="label.homepage.components" bundle="HOMEPAGE_RESOURCES"/></h3>
    </p>

    <div class="infoop2">
        <p class="mvert0"><bean:message key="message.homepage.options" bundle="HOMEPAGE_RESOURCES"/></p>
    </div>

	<table class="tstyle5 thlight thright">
		<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.currentWorkingContract.workingUnit">
			<tr>
				<th>
					<bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:
				</th>
				<td class="valigntop width05em">
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showUnit" property="showUnit" value="true"/>
				</td>
				<td>
					<p>
						<bean:define id="currentUnit" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.currentWorkingContract.workingUnit" toScope="request"/>
						<jsp:include page="unitStructure.jsp"/>
					</p>
				</td>
			</tr>
		</logic:present>
		<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher">
			<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.currentWorkingContract">
				<tr>
					<th>
						<bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:
					</th>
					<td class="valigntop width05em">
						<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showCategory" property="showCategory" value="true"/>
					</td>
					<td>
						<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher">
							<bean:write name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher.category.name.content"/>
						</logic:present>
					</td>
				</tr>
				<tr>
					<th>
						<bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:
					</th>
					<td class="valigntop width05em">
						<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showResearchUnitHomepage" property="showResearchUnitHomepage" value="true"/>
					</td>					
					<td>
						<p><bean:message key="label.homepage.research.unit.homepage" bundle="HOMEPAGE_RESOURCES"/>: <html:text bundle="HTMLALT_RESOURCES" altKey="text.researchUnitHomepage" property="researchUnitHomepage" size="30"/></p>
						<p><bean:message key="label.homepage.research.unit.name" bundle="HOMEPAGE_RESOURCES"/>: <html:text bundle="HTMLALT_RESOURCES" altKey="text.researchUnit" property="researchUnit" size="30"/></p>
					</td>
				</tr>

			</logic:present>
		</logic:present>
			<tr>
				<th>
					<bean:message key="label.homepage.showActiveStudentCurricularPlans" bundle="HOMEPAGE_RESOURCES"/>:
				</th>
				<td class="valigntop width05em">
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showActiveStudentCurricularPlans" property="showActiveStudentCurricularPlans" value="true"/>
				</td>
				<td>
					<logic:iterate id="studentCurricularPlan" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
						<app:contentLink name="studentCurricularPlan" property="degreeCurricularPlan.degree.site" target="_blank">
							<logic:present name="studentCurricularPlan" property="specialization.name">
								<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:equal>
								<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:notEqual>
							</logic:present>
							<logic:notPresent name="studentCurricularPlan" property="specialization.name">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notPresent>
							<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
							<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
						</app:contentLink>
					</logic:iterate>
					<logic:iterate id="studentCurricularPlan" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
						,
						<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
						<html:link href="<%= url %>">
							<logic:present name="studentCurricularPlan" property="specialization.name">
								<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:equal>
								<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
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
			<tr>
				<th>
					<bean:message key="label.homepage.showCurrentAttendingExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:
				</th>
				<td class="valigntop width05em">
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showCurrentAttendingExecutionCourses" property="showCurrentAttendingExecutionCourses" value="true"/>
				</td>
				<td>
					<logic:iterate id="attend" name="personAttends" length="1">
						<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
						<app:contentLink name="executionCourse" property="site">
							<bean:write name="executionCourse" property="nome"/>
						</app:contentLink>
					</logic:iterate>
					<logic:iterate id="attend" name="personAttends" offset="1">
						,
						<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
						<app:contentLink name="executionCourse" property="site">
							<bean:write name="executionCourse" property="nome"/>
						</app:contentLink>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.homepage.showAlumniDegrees" bundle="HOMEPAGE_RESOURCES"/>:
				</th>
				<td class="valigntop width05em">
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showAlumniDegrees" property="showAlumniDegrees" value="true"/>
				</td>
				<td>
					<logic:iterate id="studentCurricularPlan" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
						<app:contentLink name="studentCurricularPlan" property="degreeCurricularPlan.degree.site">
							<logic:present name="studentCurricularPlan" property="specialization.name">
								<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:equal>
								<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:notEqual>
							</logic:present>
							<logic:notPresent name="studentCurricularPlan" property="specialization.name">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notPresent>
							<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
							<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
						</app:contentLink>
					</logic:iterate>
					<logic:iterate id="studentCurricularPlan" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
						,
						<app:contentLink name="studentCurricularPlan" property="degreeCurricularPlan.degree.site">
							<logic:present name="studentCurricularPlan" property="specialization.name">
								<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:equal>
								<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
									<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
								</logic:notEqual>
							</logic:present>
							<logic:notPresent name="studentCurricularPlan" property="specialization.name">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notPresent>
							<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
							<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
						</app:contentLink>
					</logic:iterate>
				</td>
			</tr>
<%--
	<tr><td class="leftcol"><bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:</td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showUnit" property="showUnit" value="true"/>
			<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.currentWorkingContract.workingUnit">
				<bean:write name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.currentWorkingContract.workingUnit.name"/>
			</logic:present>
			<logic:iterate id="student" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.students">
				<logic:present name="student" property="activeStudentCurricularPlan">
					<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.degree.presentationName"/>
				</logic:present>
			</logic:iterate>
		</td>
	</tr>
--%>
		<tr>
			<th>
				<bean:message key="label.homepage.showPhoto" bundle="HOMEPAGE_RESOURCES"/>:
			</th>
			<td class="valigntop width05em">
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showPhoto" property="showPhoto" value="true"/>
			</td>
			<td>
                <logic:equal name="hasPhoto" value="true">
                    <html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
                </logic:equal>
<%--
                <logic:equal name="hasPhoto" value="false">
                    <html:img align="middle" src="<%= request.getContextPath() +"/images/photoPlaceHolder.jpg" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
                </logic:equal>
--%>
			</td>
		</tr>

		<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher">
			<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.currentWorkingContract">
				<tr>
					<th>
						<bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:
					</th>
					<td class="valigntop width05em">
						<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.showCurrentExecutionCourses" property="showCurrentExecutionCourses" value="true"/>
					</td>
					<td>
						<logic:iterate id="executionCourse" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher.currentExecutionCourses" length="1">
							<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
							<html:link href="<%= url %>">
								<bean:write name="executionCourse" property="nome"/>
							</html:link>
						</logic:iterate>
						<logic:iterate id="executionCourse" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacher.currentExecutionCourses" offset="1">
							, 
							<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
							<html:link href="<%= url %>">
								<bean:write name="executionCourse" property="nome"/>
							</html:link>
						</logic:iterate>
					</td>
				</tr>
			</logic:present>
		</logic:present>
		<tr>
			<th></th>
			<td colspan="3">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
					<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
				</html:submit>
			</td>
		</tr>
	</table>

</html:form>
</logic:present>