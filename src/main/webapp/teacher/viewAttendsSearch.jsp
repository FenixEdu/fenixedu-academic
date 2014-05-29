<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>
<html:xhtml/>

<logic:present name="searchBean">
	<h2> <bean:message key="message.attendingStudentsOf"/> 
	<bean:write name="searchBean" property="executionCourse.nome" /></h2>
	
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<div class="infoop2">
		<bean:message key="message.students.explanation"/>
	</div>
	
	<fr:form action="/searchECAttends.do?method=search&amp;executionCourseID=${executionCourseID}">
		<%  if (request.getAttribute("degreeCurricularPlanID") != null) { %>
			    <html:hidden property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>
		<%  } %>
		<fr:edit id="searchBean" name="searchBean" layout="search-execution-course-attends">
			<fr:layout>
				<fr:property name="searchTableClasses" value="tstyle4 mtop15 tdtop inobullet"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	<logic:notEmpty name="searchBean" property="attendsResult">
		<bean:size id="size" name="searchBean" property="attendsResult"/>
		<h3> <bean:write name="size"/>
			<bean:message key="message.attendingStudents"/> </h3>
	
		<fr:form id="sendMailForm" action="/searchECAttends.do?method=sendEmail">
			<fr:edit name="searchBean" id="mailViewState" visible="false"/>
			<a href="javascript:document.getElementById('sendMailForm').submit()"><bean:message key="link.sendEmailToAllStudents"/></a>
		</fr:form>
		
		<fr:form id="downloadStudentListForm" action="/getTabSeparatedStudentList.do">
			<fr:edit name="searchBean" id="downloadViewState" visible="false"/>
			<a href="javascript:document.getElementById('downloadStudentListForm').submit()"><bean:message key="link.getExcelSpreadSheet"/></a>
		</fr:form>

		<html:link action="/getTabSeparatedStudentListWithGrades.do?method=downloadStudentListForGradesForm"
				paramId="executionCourseOID" paramName="searchBean" paramProperty="executionCourse.externalId">
			<bean:message key="link.getExcelSpreadSheetWithGrades" bundle="APPLICATION_RESOURCES"/>
		</html:link>

		<br/>

		<bean:define id="bean" name="searchBean" property="searchElementsAsParameters"/>
		<cp:collectionPages
			url="/teacher/searchECAttends.do?method=prepare&executionCourseID=${executionCourseID}&viewPhoto=${searchBean.viewPhoto}" 
			pageNumberAttributeName="pageNumber"
			numberOfPagesAttributeName="numberOfPages"/>
		<fr:view name="attendsPagesBean">
			<fr:layout name="execution-course-attends-spreadsheet">
				<fr:property name="attendsListTableClasses" value="tstyle4 tdcenter"/>
				<fr:property name="summaryClasses" value="tstyle1 tdcenter mtop05"/>
			</fr:layout>
		</fr:view>
		<cp:collectionPages
			url="/teacher/searchECAttends.do?method=prepare&executionCourseID=${executionCourseID}"
			pageNumberAttributeName="pageNumber"
			numberOfPagesAttributeName="numberOfPages"/>
		<br/>
		<br/>
		<table class="tstyle1 tdcenter mtop05">
			<tr>
				<th><bean:message bundle="APPLICATION_RESOURCES" key="label.attends.summary.enrollmentsNumber"/></th>
				<th><bean:message bundle="APPLICATION_RESOURCES" key="label.attends.summary.studentsNumber"/></th>
			</tr>
		<%
			for (final Entry<Integer, Integer> entry : ((SearchExecutionCourseAttendsBean) request.getAttribute("attendsPagesBean")).getEnrolmentsNumberMap().entrySet()) {
		%>
				<tr>
					<td><%= entry.getKey() %></td>
					<td><%= entry.getValue() %></td>
				</tr>
		<%
			}
		%>
		</table>
	</logic:notEmpty>
	
	<logic:empty name="searchBean" property="attendsResult">
		<p><em><bean:message key="message.students.attendsSearch.noResults"/>.</em></p>
	</logic:empty>
	
</logic:present>


