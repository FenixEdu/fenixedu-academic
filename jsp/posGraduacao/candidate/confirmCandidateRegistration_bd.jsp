<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />


<logic:present name="infoMasterDegreeCandidate">
	<html:form action="/candidateRegistrationConfirmation.do?method=confirm">
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
		                <html:options collection="branchList" property="idInternal" labelProperty="name"/>
		            </html:select>
				</td>
			</tr>
		</table>
		
		
		<br />
		<div class="infoop">
			<span class="error"><strong>Nota:</strong></span>
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