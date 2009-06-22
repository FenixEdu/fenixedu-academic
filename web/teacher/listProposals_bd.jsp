<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.finalWork"/>
</h2>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<table class="mtop15">
	<tr>
		<th class="listClasses-header">
			<bean:write name="explicitDegree"/>
		</th>
	</tr>
	<tr>
		<th class="listClasses-header">
			<bean:write name="explicitYear"/>
		</th>
	</tr>
</table>

<logic:present name="proposals">
	<logic:greaterEqual name="proposals" value="1">
		<table class="mtop15">
			<tr>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.number"/>
				</th>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.title"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.orientatorName"/>
				</th>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.year"/>
				</th>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.degree"/>
				</th>
				<th class="listClasses-header" rowspan="2">
				</th>
			</tr>
			<tr>
		        <th class="listClasses-header">
		        	<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </th>
			</tr>

			<bean:define id="degree" name="finalWorkInformationForm" property="degree"/>
			<logic:iterate id="proposal" name="proposals">
					<tr>		
						<td class="listClasses" rowspan="2">
							<bean:write name="proposal" property="proposalNumber"/> 
						</td>
						<td class="listClasses" rowspan="2">
							<logic:notEmpty name="proposal" property="title">
					        	<bean:write name="proposal" property="title"/>
					        </logic:notEmpty>
						</td>
						<td class="listClasses">
							<bean:write name="proposal" property="orientator.name"/> 
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="proposal" property="scheduleing.executionYearOfOneExecutionDegree.qualifiedName"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:define id="executionDegrees" name="proposal" property="scheduleing.executionDegrees"/>
							<logic:iterate id="executionDegree" name="executionDegrees">
								<p><bean:write name="executionDegree" property="degree.sigla"/></p> 
							</logic:iterate>
						</td>
						<td class="listClasses" rowspan="2">
					        <html:link page="<%= "/finalWorkManagement.do?method=editToCreateFinalDegreeWorkProposal&amp;degree=" + request.getParameter("degree") + "&amp;executionYear=" + request.getParameter("executionYear") + "&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal) proposal).getIdInternal().toString() %>">
								<bean:message bundle="APPLICATION_RESOURCES" key="editToCreate"/>
						    </html:link>
						</td>
					</tr>
					<tr>
						<td class="listClasses">
							<logic:present name="proposal" property="coorientator">
								<bean:write name="proposal" property="coorientator.name"/>
							</logic:present>
						</td>
					</tr>

			</logic:iterate>
		</table>
	</logic:greaterEqual>
	<logic:lessThan name="proposals" value="1">
		<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>

<logic:notPresent name="proposals">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>