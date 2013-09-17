<%@ page language="java" %><%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>
<bean:define id="proposalOID" name="proposal" property="externalId" type="String"/>

<h2><bean:message key="title.teacher.finalWorkInformation"/></h2>
<span class="error"><!-- Error messages go here -->
	<html:errors/><br />
</span>

<html:form action="/finalDegreeWorkProposal">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="submit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orientatorOID" property="orientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coorientatorOID" property="coorientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.alteredField" property="alteredField"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>

	<%
	    boolean showCoordinator = false; 
	    boolean showCompanion = false;
	%>
	<logic:empty name="finalDegreeWorkProposal" property="companionName">
		<logic:empty name="finalDegreeWorkProposal" property="companionMail">
			<logic:empty name="finalDegreeWorkProposal" property="companionPhone">
				<logic:empty name="finalDegreeWorkProposal" property="companyAdress">
					<logic:empty name="finalDegreeWorkProposal" property="companyName">
						<%
						    showCoordinator = true;
						%>
					</logic:empty>
				</logic:empty>
			</logic:empty>
		</logic:empty>
	</logic:empty>
	<logic:empty name="finalDegreeWorkProposal" property="coResponsableTeacherName">
		<%
		    showCompanion = true;
		%>
	</logic:empty>
	<%
		final net.sourceforge.fenixedu.domain.ExecutionDegree executionDegree = net.sourceforge.fenixedu.domain.FenixFramework.getDomainObject((String) request.getAttribute("executionDegreeOID"));
		final net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing scheduleing = executionDegree.getScheduling();
		if (scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
			showCoordinator = true;
			showCompanion = true;
		}
		request.setAttribute("showCoordinator", showCoordinator);
		request.setAttribute("showCompanion", showCompanion);
	%>

	<b><bean:message key="label.teacher.finalWork.title"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" property="title" size="85"/>
	<hr/><br/>

	<b><bean:message key="label.teacher.finalWork.responsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message key="label.teacher.finalWork.number"/>:</th>
			<td width="10%">
				<logic:present name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherId" property="responsableTeacherId" maxlength="6" size="6"
						value='<%= ((Person) pageContext.findAttribute("orientator")).getEmployee().getEmployeeNumber().toString() %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherId" property="responsableTeacherId" maxlength="6" size="6"
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"/>
				</logic:notPresent>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
			<td width="1%"/>
			<th width="7%"><bean:message key="label.teacher.finalWork.name"/>:</th>
			<td width="66%">
				<logic:present name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherName" property="responsableTeacherName" size="55"
						value='<%= ((Person) pageContext.findAttribute("orientator")).getName().toString() %>'/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherName" property="responsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br/><br/>

	<logic:equal name="showCoordinator" value="true">
	<b><bean:message key="label.teacher.finalWork.coResponsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message key="label.teacher.finalWork.number"/>:</th>
			<td width="10%">
				<logic:present name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherId" property="coResponsableTeacherId" maxlength="6" size="6"
						value='<%= ((Person) pageContext.findAttribute("coorientator")).getEmployee().getEmployeeNumber().toString() %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherId" property="coResponsableTeacherId" maxlength="6" size="6" 
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
					/>
				</logic:notPresent>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
			<td width="1%"/>
			<th width="7%"><bean:message key="label.teacher.finalWork.name"/>:</th>
			<td width="66%">
				<logic:present name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherName" property="coResponsableTeacherName" size="55"
						value='<%= ((Person) pageContext.findAttribute("coorientator")).getName().toString() %>'/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherName" property="coResponsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br/><br/>
	</logic:equal>

	<logic:equal name="showCompanion" value="true">
	<b><bean:message key="label.teacher.finalWork.companion"/>:</b>
	<table width="100%">
		<tr>
			<th width="9%"><bean:message key="label.teacher.finalWork.name"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionName" property="companionName" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"  />						
					<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message key="label.teacher.finalWork.mail"/>:</th>
			<td>		
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionMail" property="companionMail" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
					<html:submit styleId="javascriptButtonID4" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message key="label.teacher.finalWork.phone"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionPhone" property="companionPhone" size="10" maxlength="9" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>						
					<html:submit styleId="javascriptButtonID5" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>	
		<tr>
			<th with="9%"><bean:message key="label.teacher.finalWork.companyName"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companyName" property="companyName" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
					<html:submit styleId="javascriptButtonID6" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
		<tr>
			<th with="9%"><bean:message key="label.teacher.finalWork.companyAdress"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companyAdress" property="companyAdress" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
					<html:submit styleId="javascriptButtonID7" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
	</table>
	<br/><br/>
	</logic:equal>
				
	<b><bean:message key="label.teacher.finalWork.credits"/>:</b>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsibleCreditsPercentage" property="responsibleCreditsPercentage" size="3" maxlength="3"/>% /
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsibleCreditsPercentage" property="coResponsibleCreditsPercentage" size="3" maxlength="3"/>%
	<br/><hr/>

	<b><bean:message key="label.teacher.finalWork.framing"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.framing" property="framing" rows="4" cols="80"/>
	<br/><br/>
	<b><bean:message key="label.teacher.finalWork.objectives"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.objectives" property="objectives" rows="4" cols="80"/>
	<br/><br/>
	<b><bean:message key="label.teacher.finalWork.description"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.description" property="description" rows="8" cols="80"/>
	<br/><br/>
	<b><bean:message key="label.teacher.finalWork.requirements"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.requirements" property="requirements" rows="8" cols="80"/>
	<br/><br/>
	<b><bean:message key="label.teacher.finalWork.deliverable"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.deliverable" property="deliverable" rows="4" cols="80"/>
	<br/><br/>
	<b><bean:message key="label.teacher.finalWork.url"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.url" property="url" size="80"/>

<!--
	<br/><hr/>
	<br/><b><bean:message key="label.teacher.finalWork.priority.info"/></b><br/><br/>
	<table>
		<logic:iterate id="branch" name="branches">
			<tr>
				<td>
					<bean:write name="branch" property="name"/>				
				</td>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.branchList" property="branchList">
						<bean:write name="branch" property="externalId"/>
					</html:multibox>
				</td>
			</tr>
		</logic:iterate>
	</table>
-->

<!--
	<br/><hr/><br/>
	<table cellspacing="2">
		<tr>
			<th><bean:message key="label.teacher.finalWork.numberOfGroupElements"/>:</th>
			<td>
				<bean:message key="label.teacher.finalWork.minimumNumberGroupElements"/>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumNumberOfGroupElements" size="3" maxlength="2" property="minimumNumberOfGroupElements"/>
			</td>		
			<td>
				<bean:message key="label.teacher.finalWork.maximumNumberGroupElements"/>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumNumberOfGroupElements" size="3" maxlength="2" property="maximumNumberOfGroupElements"/>
			</td>
		</tr>
		<tr height="10"></tr>
		<tr>
			<th><bean:message key="label.teacher.finalWork.degreeType"/>:</th>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeType" value="" property="degreeType"/><bean:message key="label.both"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeType" value="<%= net.sourceforge.fenixedu.domain.degree.DegreeType.DEGREE.toString() %>" property="degreeType"/><bean:message bundle="ENUMERATION_RESOURCES" key="<%= net.sourceforge.fenixedu.domain.degree.DegreeType.DEGREE.toString() %>"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeType" value="<%= net.sourceforge.fenixedu.domain.degree.DegreeType.MASTER_DEGREE.toString() %>" property="degreeType"/><bean:message bundle="ENUMERATION_RESOURCES" key="<%= net.sourceforge.fenixedu.domain.degree.DegreeType.MASTER_DEGREE.toString() %>"/></td>
		</tr>
	</table>
-->
	<br/><hr/><br/>
	<b><bean:message key="label.teacher.finalWork.observations"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.observations" property="observations" rows="4" cols="80"/><br/><br/>
	<b><bean:message key="label.teacher.finalWork.location"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.location" property="location" size="81"/><br/>

	<br/><hr/><br/>
	<b><bean:message key="finalDegreeWorkProposal.status"/>:</b>
	<br/><html:select bundle="HTMLALT_RESOURCES" altKey="select.status" property="status" size="1">
		<html:option value=""/>
		<html:options property="value" 
     				  labelProperty="label" 
					  collection="finalDegreeWorkProposalStatusList" />
	</html:select><br/>

	<br/><br/><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit></td>
</html:form>