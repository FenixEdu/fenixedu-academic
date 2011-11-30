<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>
<bean:define id="url" value="<%= String.format("/manageFinalDegreeWork.do?executionDegreeOID=%s&degreeCurricularPlanID=%s",executionDegreeOID, degreeCurricularPlanID) %>"/>
<bean:define id="method" name="method"/>
<bean:define id="candidaciesCount" name="candidaciesCount"/>


<style>
td.atribuido {
background: #fafafa;
}
td.atribuido li b  {
background: #dceff6;
color: #0a3f72;
}
td.proposta.atribuido a  {
background: #dceff6;
color: #0a3f72;
}
</style>


<table class="tstyle1 thlight tdleft mtop05">
	<logic:present name="pageNumber">
		<tr>
			<td colspan="2" style="border: none;">
				<b><bean:write name="candidaciesCount"/></b> <bean:message key="label.finalDegreeWorkProposal.results"/> <bean:write name="filterBean"/>
			</td>
			<td colspan="2" class="aright" style="border: none;">
					<logic:present name="filter">
						<bean:define id="filter" name="filter"/>
						<cp:collectionPages url="<%= "/coordinator/manageFinalDegreeWork.do?method=" + method + "&filter=" + filter + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
					</logic:present>
					<logic:notPresent name="filter">
						<cp:collectionPages url="<%= "/coordinator/manageFinalDegreeWork.do?method=" + method + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
					</logic:notPresent>
			</td>
		</tr>
	</logic:present>
	<tr>
		<th><bean:message key="label.candidate"/></th>
		<th><bean:message key="label.state"/></th>
		<th><bean:message key="label.proposal"/></th>
		<th></th>
	</tr>
	<logic:iterate id="candidacy" name="candidacies" type="net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup">
		<bean:define id="tdClass" value=""/>
		<logic:equal name="candidacy" property="attributed" value="true">
			<bean:define id="tdClass" value=" atribuido"/>
		</logic:equal>
		<bean:define id="proposalsCount" name="candidacy" property="groupProposalsCount"/>
		<bean:define id="groupProposals" name="candidacy" property="groupProposalsSortedByPreferenceOrder"/>
		<logic:notEmpty name="groupProposals">
		<tr>
			<td class="<%= tdClass %>" rowspan="<bean:write name="proposalsCount"/>">
				<logic:iterate id="candidate" name="candidacy" property="groupStudents">
					<span id="<bean:write name="candidate" property="registration.student.number"/>"></span>
					<ul class="indent0 nobullet">
						<li><b><bean:write name="candidate" property="registration.student.person.name"/></b></li>
						<li><bean:write name="candidate" property="registration.student.person.email"/></li>
						<li><bean:write name="candidate" property="registration.student.number"/></li>
					</ul>
				</logic:iterate>
			</td>
			<logic:iterate id="groupProposal" name="groupProposals" type="net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal">
			<bean:define id="proposal" name="groupProposal" property="finalDegreeWorkProposal" type="net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal"/>
				<bean:define id="tdClass2" value=""/>
				<bean:define id="isAttributed" value="false"/>
				<logic:present name="candidacy" property="attributedProposal"> 
					<bean:define id="proposalOID" name="proposal" property="externalId" type="String"/>
					<logic:equal name="candidacy" property="attributedProposal.externalId" value="<%= proposalOID %>">
						<bean:define id="tdClass2" value=" atribuido"/>
						<bean:define id="isAttributed" value="true"/>
					</logic:equal>
				</logic:present>
				<td class="acenter <%= tdClass2 %>">
					<bean:write name="proposal" property="attributionStatusLabel" filter="false"/>
					<logic:notEmpty name="proposal" property="attributionGroup">
						<logic:iterate id="candidate" name="candidacy" property="groupStudents">
							<logic:iterate id="student" name="proposal" property="attributionGroup">
								<bean:define id="studentNumber" name="student" property="registration.student.number"/>
								<bean:define id="candidateNumber" name="candidate" property="registration.student.number"/>
								<logic:notEqual name="candidateNumber" value="<%= "" + studentNumber %>">
								<%--(<%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><a href="<%= "#" + studentNumber %>"></a>) --%>
								 (<bean:write name="studentNumber"/>)
								</logic:notEqual>
							</logic:iterate>
						</logic:iterate>						
					</logic:notEmpty>
				</td>
				<td class="proposta <%= tdClass2 %>">
					<bean:write name="groupProposal" property="orderOfPreference"/>) 
                    <html:link page="<%= "/manageFinalDegreeWork.do?method=showProposal&proposalOID=" + proposal.getExternalId() + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>"><bean:write name="proposal" property="presentationName"/></html:link>
				</td>
				<td class="nowrap <%= tdClass2 %>">
				<bean:define id="attributeVisible" value="false"/>
					<logic:equal name="isAttributed" value="false">
						<bean:define id="attributeVisible" value="true"/>
						<html:link page="<%= url + "&method=attributeGroupProposal&groupProposalOID=" + groupProposal.getExternalId() %>"><bean:message key="finalDegreeWorkProposal.attribution"/></html:link>
					</logic:equal>
					<bean:define id="deleteAttributeVisible" value="false"/>
				   <logic:equal name="isAttributed" value="true">
				   		<logic:equal name="attributeVisible" value="true">|</logic:equal>
				   		<bean:define id="deleteAttributeVisible" value="true"/>
				   		<html:link page="<%= url + "&method=deleteAttributions&groupProposalOID=" + groupProposal.getExternalId() %>"><bean:message key="finalDegreeWorkProposal.delete.attribution"/></html:link>
				   	</logic:equal>
				   <logic:equal name="deleteAttributeVisible" value="true" >|</logic:equal>
				   <logic:equal name="deleteAttributeVisible" value="false">
				   		<logic:equal name="attributeVisible" value="true">|</logic:equal>
				   </logic:equal>
				   <html:link page="<%= url + "&method=deleteCandidacy&groupProposalOID=" + groupProposal.getExternalId() %>"><bean:message key="finalDegreeWorkProposal.delete.candidacy"/></html:link>
				<logic:equal name="groupProposal" property="orderOfPreference" value="1"></td></tr></logic:equal>
				<tr>
			</logic:iterate>
		</tr>
		</logic:notEmpty>
		
	</logic:iterate>
	</tr>
</table>
