<%@ page language="java" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoPerson" %>

<h2><bean:message key="title.teacher.finalWorkInformation"/></h2>
<span class="error">
	<html:errors/><br />
</span>
<html:form action="/finalWorkManagement">
	<html:hidden property="page" value="2"/>
	<html:hidden property="method" value="submit"/>
	<html:hidden property="degree"/>
	<html:hidden property="role"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="orientatorOID"/>
	<html:hidden property="coorientatorOID"/>
	<html:hidden property="alteredField"/>

	<b><bean:message key="label.teacher.finalWork.title"/>:</b>
	<br><html:text property="title" size="85"/>
	<hr><br>

	<b><bean:message key="label.teacher.finalWork.responsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message key="label.teacher.finalWork.number"/>:</th>
			<td width="10%">
				<logic:present name="orientator">
					<html:text property="responsableTeacherNumber" maxlength="6" size="6"
						value='<%= ((InfoTeacher) pageContext.findAttribute("orientator")).getTeacherNumber().toString() %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text property="responsableTeacherNumber" maxlength="6" size="6"
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"/>
				</logic:notPresent>
			</td>
			<td width="1%"/>
			<th width="7%"><bean:message key="label.teacher.finalWork.name"/>:</th>
			<td width="66%">
				<logic:present name="orientator">
					<html:text property="responsableTeacherName" size="55"
						value='<%= ((InfoTeacher) pageContext.findAttribute("orientator")).getInfoPerson().getNome().toString() %>'/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text property="responsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br><br>

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
					<html:text property="coResponsableTeacherNumber" maxlength="6" size="6"
						value='<%= ((InfoTeacher) pageContext.findAttribute("coorientator")).getTeacherNumber().toString() %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text property="coResponsableTeacherNumber" maxlength="6" size="6" 
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
					/>
				</logic:notPresent>
			</td>
			<td width="1%"/>
			<th width="7%"><bean:message key="label.teacher.finalWork.name"/>:</th>
			<td width="66%">
				<logic:present name="coorientator">
					<html:text property="coResponsableTeacherName" size="55"
						value='<%= ((InfoTeacher) pageContext.findAttribute("coorientator")).getInfoPerson().getNome().toString() %>'/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text property="coResponsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br><br>
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
				<html:text property="companionName" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"  />						
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message key="label.teacher.finalWork.mail"/>:</th>
			<td>		
				<html:text property="companionMail" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message key="label.teacher.finalWork.phone"/>:</th>
			<td>
				<html:text property="companionPhone" size="10" maxlength="9" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>						
			</td>
		</tr>	
		<tr>
			<th with="9%"><bean:message key="label.teacher.finalWork.companyName"/>:</th>
			<td>
				<html:text property="companyName" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
			</td>
		</tr>
		<tr>
			<th with="9%"><bean:message key="label.teacher.finalWork.companyAdress"/>:</th>
			<td>
				<html:text property="companyAdress" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
			</td>
		</tr>
	</table>
	<br><br>
	</logic:empty>
				
	<b><bean:message key="label.teacher.finalWork.credits"/>:</b>
	<html:text property="responsibleCreditsPercentage" size="3" maxlength="3"/>% /
	<html:text property="coResponsibleCreditsPercentage" size="3" maxlength="3"/>%
	<br><hr>

	<b><bean:message key="label.teacher.finalWork.framing"/>:</b>
	<br><html:textarea property="framing" rows="4" cols="80"/>
	<br><br>
	<b><bean:message key="label.teacher.finalWork.objectives"/>:</b>
	<br><html:textarea property="objectives" rows="4" cols="80"/>
	<br><br>
	<b><bean:message key="label.teacher.finalWork.description"/>:</b>
	<br><html:textarea property="description" rows="8" cols="80"/>
	<br><br>
	<b><bean:message key="label.teacher.finalWork.requirements"/>:</b>
	<br><html:textarea property="requirements" rows="8" cols="80"/>
	<br><br>
	<b><bean:message key="label.teacher.finalWork.deliverable"/>:</b>
	<br><html:textarea property="deliverable" rows="4" cols="80"/>
	<br><br>
	<b><bean:message key="label.teacher.finalWork.url"/>:</b>
	<br><html:text property="url" size="80"/>
	<br><hr>
	<br><b><bean:message key="label.teacher.finalWork.priority.info"/></b><br><br>

	<table>
		<logic:iterate id="branch" name="branches">
			<tr>
				<td>
					<bean:write name="branch" property="name"/>				
				</td>
				<td>
					<html:multibox property="branchList">
						<bean:write name="branch" property="idInternal"/>
					</html:multibox>
				</td>
			</tr>
		</logic:iterate>
	</table>

	<br><hr><br>
	<table cellspacing="2">
		<tr>
			<th><bean:message key="label.teacher.finalWork.numberOfGroupElements"/>:</th>
			<td>
				<bean:message key="label.teacher.finalWork.minimumNumberGroupElements"/>
				<html:text size="3" maxlength="2" property="minimumNumberOfGroupElements"/>
			</td>		
			<td>
				<bean:message key="label.teacher.finalWork.maximumNumberGroupElements"/>
				<html:text size="3" maxlength="2" property="maximumNumberOfGroupElements"/>
			</td>
		</tr>
		<tr height="10"></tr>
		<tr>
			<th><bean:message key="label.teacher.finalWork.degreeType"/>:</th>
			<td><html:radio value="<%= net.sourceforge.fenixedu.domain.degree.DegreeType.DEGREE.toString() %>" property="degreeType"/> <%= net.sourceforge.fenixedu.domain.degree.DegreeType.DEGREE.toString() %> </td>
			<td><html:radio value="<%= net.sourceforge.fenixedu.domain.degree.DegreeType.MASTER_DEGREE.toString() %>" property="degreeType"/> <%= net.sourceforge.fenixedu.domain.degree.DegreeType.MASTER_DEGREE.toString() %> </td>
		</tr>
	</table>
	<br><hr><br>
	<b><bean:message key="label.teacher.finalWork.observations"/>:</b>
	<br><html:textarea property="observations" rows="4" cols="80"/><br><br>
	<b><bean:message key="label.teacher.finalWork.location"/>:</b>
	<br><html:text property="location" size="81"/><br>


	<br><br><html:submit styleClass="inputbutton"><bean:message key="button.submit"/></html:submit></td>
</html:form>