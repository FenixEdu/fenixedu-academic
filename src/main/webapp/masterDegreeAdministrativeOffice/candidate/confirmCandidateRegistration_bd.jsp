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


<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />


<logic:present name="infoMasterDegreeCandidate">
	<html:form action="/candidateRegistration.do?method=confirm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID" />
		<table>
			<tr>
				<td>
					<strong><bean:message key="label.masterDegree.administrativeOffice.name" /></strong>
				</td>
				<td>
					<bean:write name="infoMasterDegreeCandidate" property="infoPerson.nome" />
				</td>			
			</tr>
			<tr>
				<td>
					<strong><bean:message key="label.masterDegree.administrativeOffice.number" /></strong>
				</td>
				<td>
					<bean:write name="infoMasterDegreeCandidate" property="candidateNumber"/>
				</td>
			</tr>			
			<tr>
				<td>
					<strong><bean:message key="label.masterDegree.administrativeOffice.degree" /></strong>
				</td>
				<td>
					<bean:write name="infoMasterDegreeCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/>
				</td>
			</tr>			
			<tr>
				<td>
					<strong><bean:message key="label.specialization" /></strong>
				</td>
				<td>
					<bean:message name="infoMasterDegreeCandidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
			</tr>			
			<tr>
				<td>
					<strong><bean:message key="label.masterDegree.administrativeOffice.chooseBranch" /></strong>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.branchID" property="branchID">
		                <html:options collection="branchList" property="externalId" labelProperty="name"/>
		            </html:select>
				</td>
			</tr>
		</table>
		
		
		<br />
		<div class="infoop">
			<span class="error"><!-- Error messages go here --><strong>Nota:</strong></span>
			<br />&nbsp;&nbsp;&nbsp;&nbsp;Caso este Aluno tenha um número de Pós-Graduação que lhe foi atribuido antes do ano lectivo de <strong>2002/2003</strong> introduza-o aqui. 
			<br />&nbsp;&nbsp;&nbsp;&nbsp;Caso este Aluno  ainda não tenha um número atribuido ou que essa atribuição tenha sido feita no decorrer do ano lectivo de <strong>2002/2003</strong> 
			<br />&nbsp;&nbsp;&nbsp;&nbsp;deixe este campo vazio que o programa atribuirá um novo número ou associará o aluno ao seu respectivo número.
			
		</div>
		<br />
		
		
		<table>
			<tr>
				<td>
					<strong><bean:message key="label.student.number" /></strong>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" />
				</td>
			</tr>			
		</table>				

		<br />
		<br />
		
		<bean:message key="label.masterDegree.administrativeOffice.confirmCandidateRegistration" />

		<br />
		<br />

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirmation" value="Confirmar" styleClass="inputbutton" property="confirmation" />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirmation" value="Cancelar" styleClass="inputbutton" property="confirmation" />

	</html:form> 
</logic:present>