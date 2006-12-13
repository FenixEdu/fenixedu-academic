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

<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2><bean:message key="label.professorships"/></h2>


<table border="0" cellspacing="0" cellpadding="0" class="mtop1 mbottom15">
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


<table cellpadding="5" border="0">
	<tr>
		<th class="listClasses-header" style="text-align:left">
			<bean:message key="label.professorship.course"/>
		</th>
		<th class="listClasses-header" style="text-align:left">
			<bean:message key="label.professorships.degrees"/>
		</th>
		<th class="listClasses-header" style="text-align:left">
			<bean:message key="label.semestre"/>
		</th>
        <th class="listClasses-header" style="text-align:left">
            &nbsp;
        </th>
	</tr>
	<logic:iterate id="executionCourse" name="executionCourses" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
		<tr>
			<td class="listClasses" style="text-align:left">
				<html:link page="/manageExecutionCourse.do?method=instructions" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:write name="executionCourse" property="nome"/>
                    (<bean:write name="executionCourse" property="sigla"/>)
				</html:link>
			</td>
			<td class="listClasses" style="text-align:left">
				<logic:iterate id="degree" name="executionCourse" property="degreesSortedByDegreeName">
					<bean:define id="degreeCode" type="java.lang.String" name="degree" property="sigla"/>
					<bean:define id="degreeLabel" type="java.lang.String"><bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/> <bean:message key="label.in"/> <bean:write name="degree" property="name"/></bean:define>
					<html:link href="<%= hostURL + degreeCode %>" title="<%= degreeLabel %>">
						<bean:write name="degreeCode"/>
					</html:link>
				</logic:iterate>
			</td>
			<td class="listClasses" style="text-align:left">
				<bean:write name="executionCourse" property="executionPeriod.qualifiedName"/>
			</td>
            <td class="listClasses">
                <html:link href="<%= request.getContextPath() + ExecutionCourseProcessor.getExecutionCourseAbsolutePath(executionCourse) %>">
                    <bean:message key="label.public.site"/>
                </html:link>
            </td>
		</tr>		
	</logic:iterate>
</table>
