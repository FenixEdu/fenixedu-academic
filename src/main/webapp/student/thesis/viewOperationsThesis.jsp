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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.thesis.operation.title" bundle="STUDENT_RESOURCES"/></h2>

<logic:present name="thesis"> 
	<table class="tstyle1">
		<thead>
		<tr>
			<th><bean:message key="label.thesis.operation.operation"  bundle="STUDENT_RESOURCES" /></th>
			<th><bean:message key="label.thesis.operation.date" bundle="STUDENT_RESOURCES"/></th>
			<th><bean:message key="label.thesis.operation.how" bundle="STUDENT_RESOURCES"/></th>
		</tr>
	</thead>
	<tbody> 
		<logic:present name="thesis" property="creation"> 		
		<tr>
			<bean:define id="dateCreator" name="thesis" property="creation" />
			<td><bean:message key="label.thesis.operation.creation" bundle="STUDENT_RESOURCES"/></td>
			<td><%=((org.joda.time.DateTime)dateCreator).toString("dd/MM/yyyy hh:mm")%> </td>
			<td><bean:write name="thesis" property="creator.personName" /> </td>
		</tr>
	</logic:present>
		<logic:present name="thesis" property="submission" > 
		<tr>
		<bean:define id="dateSubmission" name="thesis" property="submission" />
			<td><bean:message key="label.thesis.operation.submission"  bundle="STUDENT_RESOURCES"/></td>
			<td><%=((org.joda.time.DateTime)dateSubmission).toString("dd/MM/yyyy hh:mm")%> </td>
			<td><bean:write name="thesis" property="submitter.personName"/></td>
		</tr>
	</logic:present>	

	<logic:present name="thesis" property="confirmation" > 
		<tr>
			<bean:define id="dateConfirmation" name="thesis" property="confirmation" />
			<td><bean:message key="label.thesis.operation.confirmation" bundle="STUDENT_RESOURCES" /></td>
			<td><%=((org.joda.time.DateTime)dateConfirmation).toString("dd/MM/yyyy hh:mm")%> </td>
			<td><bean:write name="thesis" property="confirmer.personName"/></td>
		</tr>
	</logic:present>	
	<logic:present name="thesis" property="approval"> 
		<tr>
			<bean:define id="dateApproval" name="thesis" property="approval" />
			<td><bean:message key="label.thesis.operation.approval" bundle="STUDENT_RESOURCES" /></td>
			<td><%=((org.joda.time.DateTime)dateApproval).toString("dd/MM/yyyy hh:mm")%> </td>
			<td><bean:write name="thesis" property="proposalApprover.personName"/></td>
		</tr>
	</logic:present>	
	</tbody>
	</table>
</logic:present>


	

