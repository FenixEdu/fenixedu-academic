<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.candidacy"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="somemethod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>

	<logic:present name="finalDegreeWorkProposalHeaders">
		<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
			<table>
				<tr>
					<th class="listClasses-header" rowspan="2">
					</th>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.number"/>
					</th>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.title"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
					</th>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
					</th>
				</tr>
				<tr>
			        <th class="listClasses-header">
			        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
		    	    </th>
				</tr>
				<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
					<tr>
						<td class="listClasses" rowspan="2">
							<bean:define id="proposalOID" name="finalDegreeWorkProposalHeader" property="idInternal"/>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.selectedProposal" property="selectedProposal" value='<%= proposalOID.toString() %>'/>
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
			<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
		</logic:lessThan>
	</logic:present>
	<logic:notPresent name="finalDegreeWorkProposalHeaders">
		<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:notPresent>

	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='this.form.method.value=\'addProposal\';'>
		<bean:message key="label.finalDegreeWork.addProposal"/>
	</html:submit>
</html:form>
<br />