<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.transpose"/>
</h2>

<logic:present name="finalDegreeWorkProposal">
	<br />
	<table>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.title"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="title"/>
			</td>
		</tr>

		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.description"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="description"/>
			</td>
		</tr>
			
		<logic:present name="finalDegreeWorkProposalAttribution">
		
			<tr>
				<td class="listClasses-supheader" colspan="3">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.attributedTo"/>
				</td>
			</tr>
			
			<tr>
				<td class="listClasses-header" colspan="1">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.number"/>
				</td>
				<td class="listClasses-header" colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.name"/>
				</td>
			</tr>
		
			<logic:iterate id="student" name="finalDegreeWorkProposalAttribution">
			
					<td class="listClasses" colspan="1">
					
						<bean:write name="student" property="number" />
					
					</td>
					
					<td class="listClasses" colspan="2">
					
						<bean:write name="student" property="name" />
						
					</td>
				
			</logic:iterate>
		</logic:present>
	
	</table>
	
	<br />
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.transpositionWarning" />
	<br />
	<br />
	
	<html:form action="/finalWorkManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="transposeFinalDegreeWorkProposal"/>
		<html:hidden property="finalDegreeWorkProposalOID" value="<%= request.getParameter("finalDegreeWorkProposalOID") %>"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="APPLICATION_RESOURCES" key="button.confirm"/></html:submit>
	</html:form>
	
</logic:present>

<logic:messagesPresent message="true" property="finalDegreeWork.success">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="finalDegreeWork.success">
		<bean:write name="messages" />
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="finalDegreeWork.error">
	<span class="error">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="finalDegreeWork.error">
			<bean:write name="messages" />
		</html:messages>
	</span>
</logic:messagesPresent>

<br />
<br />
<html:link href="finalWorkManagement.do?method=chooseDegree"><bean:message bundle="APPLICATION_RESOURCES" key="button.back"/></html:link>
