<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<br />
<span class="error"><html:errors/><br /><br /></span>
<logic:present name="sucessfulSetOfDegreeProposalPeriod">
	<span class="sucessfulOperarion"><bean:message key="finalDegreeWorkProposal.setProposalPeriod.sucess"/><br /><br /></span>
</logic:present>
<html:form action="/manageFinalDegreeWork">
	<html:hidden property="method" value="setFinalDegreeProposalPeriod"/>
	<html:hidden property="page" value="1"/>

	<strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header"/></strong>
	<br />
	<table>
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.date"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.hour"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.start"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfProposalPeriodDate" size="18"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfProposalPeriodHour" size="18"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfProposalPeriodDate" size="18"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfProposalPeriodHour" size="18"/>
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="finalDegreeWorkProposal.setProposalPeriod.button"/>
	</html:submit>
</html:form>
<br />
<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
		<table>
			<tr>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposalHeader.number"/>
				</td>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposalHeader.title"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
				</td>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
				</td>
			</tr>
			<tr>
		        <td class="listClasses-header">
		        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </td>
			</tr>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
				<tr>
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/> 
					</td>
					<td class="listClasses" rowspan="2">
			        	<html:link page="<%= "/manageFinalDegreeWork.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
							<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
				        </html:link>
					</td>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
					</td>
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>