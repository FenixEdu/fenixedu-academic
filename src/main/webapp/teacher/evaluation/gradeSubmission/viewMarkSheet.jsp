<%@ page import="org.fenixedu.academic.domain.ExecutionCourse" %>
<%@ page import="org.fenixedu.academic.domain.MarkSheet" %>
<%@ page import="org.fenixedu.bennu.core.security.Authenticate" %>
<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<html:xhtml/>
${portal.toolkit()}
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.submited.markSheets" bundle="APPLICATION_RESOURCES"/></h2>

<p>
	<bean:define id="executionCourseID" name="executionCourseID"/>
	<a href="<%= request.getContextPath() + "/teacher/markSheetManagement.do?method=viewSubmitedMarkSheets&amp;executionCourseID=" + executionCourseID%>">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
	</a>

	<fr:view name="markSheet" schema="markSheet.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>

	<fr:view name="markSheet" property="enrolmentEvaluationsSortedByStudentNumber"
			 schema="markSheet.view.evaluation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight tdcenter"/>
		</fr:layout>
	</fr:view>
</p>

<%
	final MarkSheet markSheet = (MarkSheet) request.getAttribute("markSheet");
	final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	if (markSheet.getResponsibleTeacher().getPerson().getUser() == Authenticate.getUser()) {
		final boolean isPrinted = markSheet.getPrinted() != null && markSheet.getPrinted().booleanValue();
%>

<%
	if (isPrinted) {
%>

<form action="<%= request.getContextPath() + "/teacher/" + executionCourse.getExternalId() + "/uploadSignedMarkSheet/" + markSheet.getExternalId() %>"
	  method="post" id="signedMarkSheetForm" enctype="multipart/form-data">
	${csrf.field()}
	<div class="form-group">
		<label for="signedMarkSheet">
			<% if (markSheet.getSignedMarkSheet() == null) { %>
				<bean:message key="label.mark.sheet.signedMarkSheet" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			<% } else { %>
				<a href="<%= request.getContextPath() + "/downloadFile/"
						+ markSheet.getSignedMarkSheet().getExternalId() + "/signedMarkSheet.pdf" %>">
					<bean:message key="label.mark.sheet.signedMarkSheet" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</a>
			<% } %>
		</label>
		<div class="hideWhenPrinted">
			<input type="file" id="signedMarkSheet" name="signedMarkSheet" class="form-control form-inline" accept="application/pdf" />
		</div>
	</div>
</form>
<%
	}
%>


<table>
	<tr>
        <%
            if (isPrinted) {
        %>
        <td style="padding-right: 10px;">
			<div class="hideWhenPrinted">
            <a href="#" onclick="document.getElementById('signedMarkSheetForm').submit();" class="btn btn-primary">
                <bean:message key="label.mark.sheet.save.signed.document" bundle="ACADEMIC_OFFICE_RESOURCES"/>
            </a>
			</div>
			<div class="showWhenPrinted">
				<a href="#" onclick="return toggleWhenPrinted();" class="btn btn-default">
					<bean:message key="label.mark.sheet.replace.signed.document" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</a>
			</div>
        </td>
        <%
            }
        %>

        <%
	if (!markSheet.isConfirmed()) {
%>
		<td style="padding-right: 10px; vertical-align: bottom;">
			<div class="hideWhenPrinted">
	<form action="<%= request.getContextPath() + "/teacher/markSheetManagement.do" %>" method="post"
		<% if (!isPrinted) { %>
			onsubmit="return doRefresh();"
   		<% } %>
		>
		${csrf.field()}
		<input type="hidden" name="method" value="printMarkSheet"/>
		<input type="hidden" name="markSheetId" value="<%= markSheet.getExternalId() %>"/>

        <% final String bclass = isPrinted ? "btn-default" : "btn-primary"; %>
		<button class="btn <%= bclass%>">
			<bean:message key="label.generate.mark.sheet" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</button>
	</form>
			</div>
		</td>
<%
	}
%>
	</tr>
</table>

<script>
	function doRefresh() {
		setTimeout(waitAndReload, 3000);
		return true;
	}

	function waitAndReload() {
		location.reload();
		return true;
	}

	function toggleWhenPrinted() {
		Array.prototype.forEach.call(document.getElementsByClassName("hideWhenPrinted"), function(e) {
			e.style.display = "block";
		});
		Array.prototype.forEach.call(document.getElementsByClassName("showWhenPrinted"), function(e) {
			e.style.display = "none";
		});
		return false;
	}

<%
	if (isPrinted) {
%>
	Array.prototype.forEach.call(document.getElementsByClassName("hideWhenPrinted"), function(e) {
		e.style.display = "none";
	});
<%
	} else {
%>
	Array.prototype.forEach.call(document.getElementsByClassName("showWhenPrinted"), function(e) {
		e.style.display = "none";
	});
<%
	}
%>
</script>

<%
	}
%>
