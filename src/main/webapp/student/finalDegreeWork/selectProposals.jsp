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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.candidacy"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/finalDegreeWorkCandidacy">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="somemethod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearOID" property="executionYearOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID"/>

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
							<bean:define id="proposalOID" name="finalDegreeWorkProposalHeader" property="externalId"/>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.selectedProposal" property="selectedProposal" value='<%= proposalOID.toString() %>'/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/> 
						</td>
						<td class="listClasses" rowspan="2">
				        	<html:link target="_blank" href="<%= request.getContextPath() + "/publico/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString() %>">
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