<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExecutionCourseProcessor" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
<bean:define id="hostURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/</bean:define>
<bean:define id="hostURL2" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %></bean:define>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="label.professorships"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<table class="tstyle5 thmiddle mtop1 mbottom1">
	<tr>
		<td nowrap="nowrap">
			<bean:message key="property.executionPeriod"/>:
    	</td>
		<td nowrap="nowrap">
			<html:form action="/showProfessorships" >
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="list"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
				<html:select bundle="HTMLALT_RESOURCES" property="executionPeriodID" size="1" onchange="this.form.submit();">
					<html:option key="option.all.execution.periods" value=""/>
					<html:options labelProperty="label" property="value" collection="executionPeriodLabelValueBeans"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</html:form>
    	</td>
    </tr>
</table>


<logic:empty name="executionCourses">
	<p><em><bean:message key="label.noProfessorships"/></em></p>
</logic:empty>


<logic:notEmpty name="executionCourses">
	<p class="mbottom05"><bean:message key="label.choose.course.to.administrate"/>:</p>
	<table class="tstyle4 thlight tdpadding1 mtop05">
		<tr>
			<th><bean:message key="label.semestre"/></th>
			<th><bean:message key="label.executionCourseManagement.menu.view.courseAndPage"/></th>
			<th><bean:message key="label.professorships.degrees"/></th>
		</tr>
	<logic:iterate id="executionCourse" name="executionCourses" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
			<tr>
				<td style="width: 150px;" class="acenter">
		            <span class="smalltxt">
						<bean:write name="executionCourse" property="executionPeriod.qualifiedName"/>
					</span>
				</td>
				<td style="width: 450px;">
					<strong>
						<html:link page="/manageExecutionCourse.do?method=instructions" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
							<bean:write name="executionCourse" property="nome"/>
		                    (<bean:write name="executionCourse" property="sigla"/>)
						</html:link>
					</strong>
					
		            <bean:define id="executionCourseURL"><%= hostURL2 + ExecutionCourseProcessor.getExecutionCourseAbsolutePath(executionCourse) %></bean:define>	            
		            <p class="mtop05 mbottom0">
			            <span class="smalltxt breakword color888" style="word-wrap: break-word !important;">
							<html:link href="<%= executionCourseURL %>" styleClass="color888">
			                    <bean:write name="executionCourseURL"/>
			                </html:link>
		                </span>
	                </p>
				</td>
				<td>
		            <span class="smalltxt">
						<logic:iterate id="degree" name="executionCourse" property="degreesSortedByDegreeName">
							<bean:define id="degreeCode" type="java.lang.String" name="degree" property="sigla"/>
							<bean:define id="degreeLabel" type="java.lang.String"><bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/> <bean:message key="label.in"/> <bean:write name="degree" property="name"/></bean:define>
							<html:link href="<%= hostURL + degreeCode %>" title="<%= degreeLabel %>">
								<bean:write name="degreeCode"/>
							</html:link>
						</logic:iterate>
					</span>				
				</td>
			</tr>

	</logic:iterate>
	</table>
</logic:notEmpty>



