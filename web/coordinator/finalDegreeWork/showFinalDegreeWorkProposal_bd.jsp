<%@ page language="java" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" type="String" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegreeOID" type="String" scope="request" />



<style>
input.teacherNameDisplay {
background: none;
border: none;
color: #000;
}
</style>


<h2>
	<bean:message key="title.final.degree.work.administration"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	

<p><html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="label.return"/></html:link></p>

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
	<div class="infoop2">
		<p>
			<strong><bean:message key="message.final.degree.work.other.execution.degrees"/></strong>
		</p>
			<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
				<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID %>">
					<p class="mvvert05">
						<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
					</p>
				</logic:notEqual>
			</logic:iterate>
	</div>
</logic:notEqual>
</logic:present>


<span class="error"><!-- Error messages go here -->
<html:errors/>
</span>


<html:form action="/finalDegreeWorkProposal">
	
	<%
	    boolean showCoordinator = false; 
	    boolean showCompanion = false;
	    String proposalOID = (String) request.getAttribute("proposalOID");
	%>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="submit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orientatorOID" property="orientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coorientatorOID" property="coorientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.alteredField" property="alteredField"/>
	<html:hidden bundle="HTMLALT_RESOURCES" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID  %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" property="executionDegreeOID" value="<%= executionDegreeOID %>"/>
	<logic:present name="proposalOID">
		<html:hidden bundle="HTMLALT_RESOURCES" property="proposalOID" value="<%= proposalOID %>"/>
	</logic:present>

	
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
	final net.sourceforge.fenixedu.domain.ExecutionDegree executionDegree = net.sourceforge.fenixedu.domain.DomainObject.fromExternalId((String)request.getAttribute("executionDegreeOID"));
	final net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing scheduleing = executionDegree.getScheduling();
	if (scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
		showCoordinator = true;
		showCompanion = true;
	}
	request.setAttribute("showCoordinator", showCoordinator);
	request.setAttribute("showCompanion", showCompanion);
	%>

	<p class="mtop2 mbottom05"><bean:message key="label.teacher.finalWork.title"/>:</p>
	<p><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" property="title" size="85"/></p>


	<p class="mtop2 mbottom05"><b><bean:message key="label.teacher.finalWork.responsable"/></b></p>
	<table width="100%">
		<tr>
			<td style="width: 120px;"><bean:message key="label.teacher.finalWork.number"/>:</td>
			<td style="width: 120px;">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherId" property="responsableTeacherId" size="6"/>
				<html:img src="<%= "" + request.getContextPath() + "/images/zoom.png" %>" onclick="document.forms[0].method.value='showTeacherName';document.forms[0].page.value='1';document.forms[0].alteredField.value='orientator';document.forms[0].submit();"/>
				
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
			<td><bean:message key="label.teacher.finalWork.name"/>:</td>
			<td id='respName'>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherName" property="responsableTeacherName" size="55" styleClass="teacherNameDisplay" disabled="false"/>
			</td>
		</tr>
	</table>


	<logic:equal name="showCoordinator" value="true">
	<p class="mtop2 mbottom05"><b><bean:message key="label.teacher.finalWork.coResponsable"/></b></p>
	<table width="100%">
		<tr>
			<td style="width: 120px;"><bean:message key="label.teacher.finalWork.number"/>:</td>
			<td style="width: 120px;">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherNumber" property="coResponsableTeacherId" size="6"/>
				<html:img src="<%= "" + request.getContextPath() + "/images/zoom.png" %>" onclick="document.forms[0].method.value='showTeacherName';document.forms[0].page.value='1';document.forms[0].alteredField.value='coorientator';document.forms[0].submit();"/>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
			<td><bean:message key="label.teacher.finalWork.name"/>:</td>
			<td id='coRespName'>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherName" property="coResponsableTeacherName" size="55" styleClass="teacherNameDisplay" disabled="false"/>
			</td>
		</tr>
	</table>
	</logic:equal>



	<logic:equal name="showCompanion" value="true">
	<p class="mtop2 mbottom05"><b><bean:message key="label.teacher.finalWork.companion"/></b></p>
	<table width="100%">
		<tr>
			<td style="width: 140px;"><bean:message key="label.teacher.finalWork.name"/>:</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionName" property="companionName" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"  />						
					<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.teacher.finalWork.mail"/>:</td>
			<td>		
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionMail" property="companionMail" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
					<html:submit styleId="javascriptButtonID4" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.teacher.finalWork.phone"/>:</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionPhone" property="companionPhone" size="10" maxlength="9" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>						
					<html:submit styleId="javascriptButtonID5" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>	
		<tr>
			<td><bean:message key="label.teacher.finalWork.companyName"/>:</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companyName" property="companyName" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
					<html:submit styleId="javascriptButtonID6" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.teacher.finalWork.companyAdress"/>:</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companyAdress" property="companyAdress" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
					<html:submit styleId="javascriptButtonID7" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
	</table>
	</logic:equal>

	<p class="mvert2">
		<bean:message key="label.teacher.finalWork.credits"/>:
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsibleCreditsPercentage" property="responsibleCreditsPercentage" size="3" maxlength="3"/>% /
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsibleCreditsPercentage" property="coResponsibleCreditsPercentage" size="3" maxlength="3"/>%
	</p>

	<p class="mtop2 mbottom05"><bean:message key="label.teacher.finalWork.framing"/>:</p>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.framing" property="framing" rows="4" cols="80"/>

	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.objectives"/>:</p>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.objectives" property="objectives" rows="4" cols="80"/>

	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.description"/>:</p>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.description" property="description" rows="8" cols="80"/>

	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.requirements"/>:</p>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.requirements" property="requirements" rows="8" cols="80"/>

	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.deliverable"/>:</p>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.deliverable" property="deliverable" rows="4" cols="80"/>

	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.url"/>:</p>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.url" property="url" size="80"/>

	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.observations"/>:</p>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.observations" property="observations" rows="4" cols="80"/>
	
	<p class="mtop15 mbottom05"><bean:message key="label.teacher.finalWork.location"/>:</p>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.location" property="location" size="81"/>
	
	<p class="mtop2 mbottom05">
		<bean:message key="finalDegreeWorkProposal.status"/>:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.status" property="status" size="1">
			<html:option value=""/>
			<html:options property="value" labelProperty="label" collection="finalDegreeWorkProposalStatusList" />
		</html:select>
	</p>
	
	<p class="mtop2">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
	</p>
	
	
</html:form>
