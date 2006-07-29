<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<span class="error"><!-- Error messages go here --><html:errors /><br /></span>
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareFinalWorkInformation"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.responsibleCreditsPercentage" property="responsibleCreditsPercentage"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coResponsibleCreditsPercentage" property="coResponsibleCreditsPercentage"/>

	<strong><bean:message key="label.teacher.finalWork.chooseDegreeAndYear"/>:</strong><br />
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear"
			onchange="this.form.method.value='changeExecutionYear';this.form.submit();"
			>
		<html:options collection="infoExecutionYears" property="idInternal" labelProperty="year" />
	</html:select>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.degree" property="degree"
			onchange="this.form.method.value='chooseDegree';this.form.submit();"
			>
		<html:options collection="executionDegreeList" property="idInternal" labelProperty="infoDegreeCurricularPlan.presentationName" />
	</html:select>
	<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
	<br /><br />
	<strong><bean:message key="label.teacher.finalWork.role" />:</strong>
	<br />
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.role" property="role" value="responsable" /><bean:message key="label.teacher.finalWork.responsable"/>
	<br />
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.role" property="role" value="coResponsable" /><bean:message key="label.teacher.finalWork.coResponsable"/>
	<br /><br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.create"/></html:submit>
</html:form>

<br />
<br />
<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
		<table>
			<tr>
				<th class="listClasses-header" rowspan="2" colspan="2">
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
				<th class="listClasses-header" rowspan="2">
				</th>
			</tr>
			<tr>
		        <th class="listClasses-header">
		        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </th>
			</tr>
			<% java.util.Set processedExecutionDegreeIds = new java.util.HashSet(); %>
			<bean:define id="degree" name="finalWorkInformationForm" property="degree"/>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
				<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
				<logic:equal name="executionDegreeId"value="<%= degree.toString() %>">
				<tr>
					<th class="listClasses-header" rowspan="2" colspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.proposal"/>
					</th>				
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
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="attributeFinalDegreeWork"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>

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
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedGroupProposals" property="selectedGroupProposals" onchange='<%= onChange.toString() %>'><bean:write name="groupProposal" property="idInternal"/></html:multibox>
						<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
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
								/viewCurriculum.do?method=getStudentCP&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
							<bean:define id="curriculumLink">
								/viewCurriculum.do?method=getStudentCP&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
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
								/viewCurriculum.do?method=getStudentCP&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/> 
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
							<bean:define id="curriculumLink">
								/viewCurriculum.do?method=getStudentCP&amp;studentNumber=<%= studentNumber.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
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