<%@ page language="java" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoPerson" %>

<h2><bean:message key="title.teacher.finalWorkInformation"/></h2>
<span class="error">
	<html:errors/><br />
</span>

<logic:present name="scheduling">
	<strong>
		<bean:message key="message.final.degree.work.execution.degrees"/>
	</strong>
	<br/>
	<table>
		<logic:iterate id="currentExecutionDegree" name="scheduling" property="executionDegreesSortedByDegreeName">
			<tr>
				<td class="listClasses">
					<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br/>
</logic:present>

<html:form action="/finalWorkManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="submit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.role" property="role"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orientatorOID" property="orientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coorientatorOID" property="coorientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.alteredField" property="alteredField"/>

	<b><bean:message key="label.teacher.finalWork.title"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" property="title" size="85"/>
	<hr/><br/>

	<b><bean:message key="label.teacher.finalWork.responsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message key="label.teacher.finalWork.number"/>:</th>
			<td width="10%">
				<logic:present name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherNumber" property="responsableTeacherNumber" maxlength="6" size="6"
						value='<%= ((InfoTeacher) pageContext.findAttribute("orientator")).getTeacherNumber().toString() %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherNumber" property="responsableTeacherNumber" maxlength="6" size="6"
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
						value='<%= ((InfoTeacher) pageContext.findAttribute("orientator")).getInfoPerson().getNome().toString() %>'/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherName" property="responsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br/><br/>

	<logic:empty name="finalWorkInformationForm" property="companionName">
	<logic:empty name="finalWorkInformationForm" property="companionMail">
	<logic:empty name="finalWorkInformationForm" property="companionPhone">
	<logic:empty name="finalWorkInformationForm" property="companyAdress">
	<logic:empty name="finalWorkInformationForm" property="companyName">
	<b><bean:message key="label.teacher.finalWork.coResponsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message key="label.teacher.finalWork.number"/>:</th>
			<td width="10%">
				<logic:present name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherNumber" property="coResponsableTeacherNumber" maxlength="6" size="6"
						value='<%= ((InfoTeacher) pageContext.findAttribute("coorientator")).getTeacherNumber().toString() %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherNumber" property="coResponsableTeacherNumber" maxlength="6" size="6" 
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
						value='<%= ((InfoTeacher) pageContext.findAttribute("coorientator")).getInfoPerson().getNome().toString() %>'/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherName" property="coResponsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br/><br/>
	</logic:empty>
	</logic:empty>
	</logic:empty>
	</logic:empty>
	</logic:empty>

	<logic:empty name="finalWorkInformationForm" property="coResponsableTeacherName" >
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
	</logic:empty>
				
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
						<bean:write name="branch" property="idInternal"/>
					</html:multibox>
				</td>
			</tr>
		</logic:iterate>
	</table>

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
	<br/><hr/><br/>
	<b><bean:message key="label.teacher.finalWork.observations"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.observations" property="observations" rows="4" cols="80"/><br/><br/>
	<b><bean:message key="label.teacher.finalWork.location"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.location" property="location" size="81"/><br/>


	<br/><br/><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit></td>
</html:form>