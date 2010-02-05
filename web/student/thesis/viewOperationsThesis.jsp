<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message key="label.thesis.Dissertation" bundle="STUDENT_RESOURCES"/></em>
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


	

