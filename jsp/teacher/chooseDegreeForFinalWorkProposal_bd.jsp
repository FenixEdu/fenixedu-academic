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

	<strong><bean:message key="label.teacher.finalWork.chooseDegreeAndYear"/>:</strong><br />
	<html:select property="executionYear"
			onchange="this.form.method.value='changeExecutionYear';this.form.submit();"
			>
		<html:options collection="infoExecutionYears" property="idInternal" labelProperty="year" />
	</html:select>
	<html:select property="degree"
			onchange="this.form.method.value='chooseDegree';this.form.submit();"
			>
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
				<td class="listClasses-header" rowspan="2" colspan="2">
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
				</td>
			</tr>
			<tr>
		        <td class="listClasses-header">
		        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </td>
			</tr>
			<% java.util.Set processedExecutionDegreeIds = new java.util.HashSet(); %>
			<bean:define id="degree" name="finalWorkInformationForm" property="degree"/>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
				<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
				<logic:equal name="executionDegreeId"value="<%= degree.toString() %>">
				<tr>
					<td class="listClasses-header" rowspan="2" colspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.proposal"/>
					</td>				
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/> 
					</td>
					<td class="listClasses" rowspan="2">
						<logic:equal name="finalDegreeWorkProposalHeader" property="editable" value="true">
				        	<html:link page="<%= "/finalWorkManagement.do?method=editFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
								<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
					        </html:link>
				        </logic:equal>
						<logic:notEqual name="finalDegreeWorkProposalHeader" property="editable" value="true">
				        	<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
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
					<td class="listClasses" rowspan="2">
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>
			<logic:present name="finalDegreeWorkProposalHeader" property="groupProposals">
			<bean:size id="numberOfGroups" name="finalDegreeWorkProposalHeader" property="groupProposals"/>
			<% int total = 1; %>
			<logic:iterate id="groupProposal" name="finalDegreeWorkProposalHeader" property="groupProposals">
				<bean:size id="numberOfStudents" name="groupProposal" property="infoGroup.groupStudents"/>
				<% total += numberOfStudents.intValue(); %>
			</logic:iterate>
			<html:form action="/finalDegreeWorkAttribution">
				<html:hidden property="method" value="attributeFinalDegreeWork"/>
				<html:hidden property="selectedGroupProposal"/>

				<tr>
					<td bgcolor="#a2aebc" align="center" rowspan="<%= total %>">
						<bean:message key="finalDegreeWorkProposalHeader.candidates"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.teacher.attribute"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.preference"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.number"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.name"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.email"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.phone"/>
					</td>
				</tr>
			<% boolean isOdd = true; %>
			<% java.lang.String bgColor = null; %>
			<logic:iterate id="groupProposal" name="finalDegreeWorkProposalHeader" property="groupProposals">
			<% isOdd = !isOdd; %>
			<% if (isOdd) { %>
				<% bgColor = "#d3cec8"; %>
			<% } else { %>
				<% bgColor = "#eae7e4"; %>
			<% } %>
			<bean:size id="numberOfStudents" name="groupProposal" property="infoGroup.groupStudents"/>
			<% java.lang.String emails = ""; %>
			<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
				<bean:define id="student" name="groupStudent" property="student"/>
				<bean:define id="email" name="student" property="infoPerson.email"/>
				<% emails += email; %>
			</logic:iterate>
			<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" offset="1">
				<bean:define id="student" name="groupStudent" property="student"/>
				<bean:define id="email" name="student" property="infoPerson.email"/>
				<% emails += "," + email; %>
			</logic:iterate>
				<tr>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<bean:define id="onChange">
							this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';this.form.submit();
						</bean:define>
						<html:multibox property="selectedGroupProposals" onchange='<%= onChange.toString() %>'>
							<bean:write name="groupProposal" property="idInternal"/>
						</html:multibox>
					</td>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<a href="mailto:<%= emails %>"><bean:write name="groupProposal" property="orderOfPreference"/></a>
					</td>
					<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
						<bean:define id="student" name="groupStudent" property="student"/>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
							<bean:define id="curriculumLink">
								/finalWorkManagement.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
							<bean:define id="curriculumLink">
								/finalWorkManagement.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.nome"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="email" name="student" property="infoPerson.email"/>
							<a href="mailto:<%= email %>"><bean:write name="email"/></a>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:write name="student" property="infoPerson.telefone"/>
						</td>
					</logic:iterate>
				</tr>
				<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" offset="1">
					<bean:define id="student" name="groupStudent" property="student"/>
					<tr>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
							<bean:define id="curriculumLink">
								/finalWorkManagement.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/> 
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
							<bean:define id="curriculumLink">
								/finalWorkManagement.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.nome"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="email" name="student" property="infoPerson.email"/>
							<a href="mailto:<%= email %>"><bean:write name="email"/></a>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:write name="student" property="infoPerson.telefone"/>
						</td>
					</tr>
				</logic:iterate>					
			</logic:iterate>
			</html:form>
			</logic:present>
			</logic:equal>
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