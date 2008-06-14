<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="studentExecutionCoursesList">
	<em><bean:message key="title.student.portalTitle"/></em>
	<h2>
		<bean:message key="link.tests"/>
	</h2>

	<logic:empty name="studentExecutionCoursesList">
		<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="studentExecutionCoursesList" >
	
	<table>
		<tr>
			<th class="listClasses-header"><bean:message key="label.curricular.course.acronym"/></th>
			<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
		</tr>
		<logic:iterate id="executionCourse" name="studentExecutionCoursesList" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
		<tr>
			<td class="listClasses">
				<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
					<bean:write name="executionCourse" property="sigla"/>
				</html:link>
			</td>
			<td class="listClasses">
				<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
					<bean:write name="executionCourse" property="nome"/>
				</html:link>
			</td>
		</tr>
		</logic:iterate>
	</table>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="studentExecutionCoursesList">
<h2><bean:message key="message.noStudentTests"/></h2>
</logic:notPresent>