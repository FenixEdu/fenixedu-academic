<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<span class="error"><html:errors/></span>


<br />


<logic:present name="infoMasterDegreeCandidate">
	<html:form action="/candidateRegistration.do?method=confirm">
		<html:hidden property="candidateID" />
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
					<bean:write name="infoMasterDegreeCandidate" property="specialization"/>
				</td>
			</tr>			
			<tr>
				<td>
					<strong><bean:message key="label.masterDegree.administrativeOffice.chooseBranch" /></strong>
				</td>
				<td>
					<html:select property="branchID">
		                <html:options collection="branchList" property="idInternal" labelProperty="name"/>
		            </html:select>
				</td>
			</tr>			
		</table>				

		<br />
		<br />
		
		<bean:message key="label.masterDegree.administrativeOffice.confirmCandidateRegistration" />

		<br />
		<br />

		<html:submit value="Confirmar" styleClass="inputbutton" property="OK"/>
		<html:submit value="Cancelar" styleClass="inputbutton" property="NOTOK"/>
	</html:form> 
</logic:present>