<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
<bean:define id="hostURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/</bean:define>

<p>
	<span class="error">
		<html:errors/>
	</span>
</p>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.program.explanation" />
		</td>
	</tr>
</table>

<logic:present name="executionCourse">

	<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
		<h3>
			<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
			<bean:message key="label.in"/>
			<bean:write name="degree" property="nome"/>
		</h3>
		<logic:iterate id="curriculum" name="curricularCourse" property="associatedCurriculums">
			<blockquote>
				<h4>
					<bean:write name="curricularCourse" property="name"/>: <bean:message key="title.program"/>
				</h4>
				<bean:write name="curriculum" property="program" filter="false"/>
				<logic:present name="curriculum" property="programEn">
					<br/>
					<h4>
						<bean:write name="curricularCourse" property="name"/>: <bean:message key="title.program.eng"/>
					</h4>
					<bean:write name="curriculum" property="programEn" filter="false"/>
				</logic:present>
			</blockquote>

			<bean:define id="url" type="java.lang.String">/editProgram.do?method=prepareEditProgram&amp;curriculumID=<bean:write name="curriculum" property="idInternal"/></bean:define>
			<logic:notPresent name="curricularCourse" property="competenceCourse">
				<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:message key="button.edit"/>
				</html:link>
			</logic:notPresent>
			<logic:present name="curricularCourse" property="competenceCourse">
				<logic:equal name="curricularCourse" property="competenceCourse.curricularStage" value="OLD">
					<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
						<bean:message key="button.edit"/>
					</html:link>
				</logic:equal>
			</logic:present>

		</logic:iterate>

		<br/>
		<br/>
	</logic:iterate>

</logic:present>