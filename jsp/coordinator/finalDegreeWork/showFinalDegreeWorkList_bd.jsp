<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<br />
<span class="error"><html:errors/><br /><br /></span>
<logic:present name="sucessfulSetOfDegreeProposalPeriod">
	<span class="sucessfulOperarion"><bean:message key="finalDegreeWorkProposal.setProposalPeriod.sucess"/><br /><br /></span>
</logic:present>
<logic:present name="sucessfulSetOfDegreeCandidacyPeriod">
	<span class="sucessfulOperarion"><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.sucess"/><br /><br /></span>
</logic:present>

<table width="90%">
<tr>
<td align="center">
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
				<html:text property="startOfProposalPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfProposalPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="finalDegreeWorkProposal.setProposalPeriod.button"/>
	</html:submit>
</html:form>
</td>
<td align="center">
<html:form action="/setFinalDegreeWorkCandidacyPeriod">
	<html:hidden property="method" value="setFinalDegreeCandidacyPeriod"/>
	<html:hidden property="page" value="1"/>

	<strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header"/></strong>
	<br />
	<table>
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.date"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.hour"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.start"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfCandidacyPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.end"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfCandidacyPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.button"/>
	</html:submit>
</html:form>
</td>
</tr>
</table>

<br />
<html:form action="/finalDegreeWorkProposal">
	<html:hidden property="method" value="createNewFinalDegreeWorkProposal"/>
	<html:hidden property="page" value="0"/>
	<html:submit>
		<bean:message key="finalDegreeWorkProposal.create.new.button"/>
	</html:submit>
</html:form>
<html:form action="/manageFinalDegreeWork">
	<html:hidden property="method" value="publishAprovedProposals"/>
	<html:hidden property="page" value="0"/>
	<html:submit>
		<bean:message key="finalDegreeWorkProposal.publishAproved.button"/>
	</html:submit>
</html:form>
<br />
<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
	<html:form action="/manageFinalDegreeWork">
		<html:hidden property="method" value="aproveSelectedProposalsStatus"/>
		<html:hidden property="page" value="0"/>

		<table>
			<tr>
				<td class="listClasses-header" rowspan="2">
				</td>
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
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposal.status"/>
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
						<html:multibox property="selectedProposals">
							<bean:write name="finalDegreeWorkProposalHeader" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/>
					</td>
					<td class="listClasses" rowspan="2">
			        	<html:link page="<%= "/finalDegreeWorkProposal.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
							<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
				        </html:link>
					</td>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
					</td>
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
					</td>
					<td class="listClasses" rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="status">
							<bean:write name="finalDegreeWorkProposalHeader" property="status.key"/> 
						</logic:present>
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>
			</logic:iterate>
		</table>

	<html:submit>
		<bean:message key="finalDegreeWorkProposal.aproveSelectedProposals.button"/>
	</html:submit>
	<html:submit onclick='this.form.method.value=\'publishSelectedProposals\';this.form.submit();'>
		<bean:message key="finalDegreeWorkProposal.publishSelectedProposals.button"/>
	</html:submit>
	</html:form>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>