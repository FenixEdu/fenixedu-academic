<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.candidacy"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
	<html:hidden property="method" value="somemethod"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>

	<logic:present name="finalDegreeWorkProposalHeaders">
		<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
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
				</tr>
				<tr>
			        <td class="listClasses-header">
			        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
		    	    </td>
				</tr>
				<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
					<tr>
						<td class="listClasses" rowspan="2">
							<bean:define id="proposalOID" name="finalDegreeWorkProposalHeader" property="idInternal"/>
							<html:radio property="selectedProposal" value='<%= proposalOID.toString() %>'/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/> 
						</td>
						<td class="listClasses" rowspan="2">
				        	<html:link target="_blank" href="<%= request.getContextPath() + "/publico/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
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

	<br />
	<html:submit onclick='this.form.method.value=\'addProposal\';'>
		<bean:message key="label.finalDegreeWork.addProposal"/>
	</html:submit>
</html:form>
<br />