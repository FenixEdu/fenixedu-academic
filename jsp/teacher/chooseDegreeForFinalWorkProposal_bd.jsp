<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<span class="error"><html:errors/><br /></span>
<logic:present name="infoScheduleing">
	<span class="error">
		<bean:message key="finalDegreeWorkProposal.ProposalPeriod.interval"/>
		<dt:format pattern="dd/MM/yyyy HH:mm">
			<bean:write name="infoScheduleing" property="startOfProposalPeriod.time"/>
		</dt:format>
		-
		<dt:format pattern="dd/MM/yyyy HH:mm">
			<bean:write name="infoScheduleing" property="endOfProposalPeriod.time"/>
		</dt:format>
		<br />
	</span>
</logic:present>
<br />

<html:form action="/finalWorkManagement">
	<html:hidden property="method" value="prepareFinalWorkInformation"/>
	<html:hidden property="responsibleCreditsPercentage"/>
	<html:hidden property="coResponsibleCreditsPercentage"/>

	<strong><bean:message key="label.teacher.finalWork.chooseDegree"/>:</strong><br />
	<html:select property="degree">
		<html:options collection="executionDegreeList" property="idInternal" labelProperty="infoDegreeCurricularPlan.infoDegree.nome" />
	</html:select>
	<br /><br />
	<strong><bean:message key="label.teacher.finalWork.role" />:</strong>
	<br />
	<html:radio property="role" value="responsable" /><bean:message key="label.teacher.finalWork.responsable"/>
	<br />
	<html:radio property="role" value="coResponsable" /><bean:message key="label.teacher.finalWork.coResponsable"/>
	<br /><br />
	<html:submit styleClass="inputbutton"><bean:message key="button.create"/></html:submit>
</html:form>

<br />
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
						<logic:equal name="finalDegreeWorkProposalHeader" property="editable" value="true">
				        	<html:link page="<%= "/finalWorkManagement.do?method=editFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
								<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
					        </html:link>
				        </logic:equal>
						<logic:notEqual name="finalDegreeWorkProposalHeader" property="editable" value="true">
				        	<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
								<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
					        </html:link>
				        </logic:notEqual>
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