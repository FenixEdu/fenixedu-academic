<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="link.tests"/></h2>


<logic:present name="registrationSelectExecutionYearBean">
	<div class="mvert15">
		<fr:form action="/studentTests.do?method=viewStudentExecutionCoursesWithTests">
			<fr:edit id="executionYear" name="registrationSelectExecutionYearBean" slot="executionYear">
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.StudentExecutionYearsProvider" />
					<fr:property name="format" value="${year}" />
					<fr:property name="destination" value="postBack"/>
				</fr:layout>
			</fr:edit>
			<p>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton">
					<bean:message key="button.submit" />
				</html:submit>
			</p>
		</fr:form>
	</div>
</logic:present>


<logic:present name="studentExecutionCoursesList">
	<logic:empty name="studentExecutionCoursesList">
		<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="studentExecutionCoursesList" >
		<bean:message key="message.CoursesWithTests"/>:
		<table class="tstyle1 thlight tdcenter">
			<tr>
				<th><bean:message key="label.curricular.course.acronym"/></th>
				<th><bean:message key="label.curricular.course.name"/></th>
			</tr>
			<logic:iterate id="executionCourse" name="studentExecutionCoursesList" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
			<tr>
				<td>
					<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
						<bean:write name="executionCourse" property="sigla"/>
					</html:link>
				</td>
				<td>
					<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</td>
			</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="studentExecutionCoursesList">
	<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
</logic:notPresent>
