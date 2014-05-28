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
<%@ page language="java" %><%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>

<h2><bean:message bundle="APPLICATION_RESOURCES" key="title.teacher.finalWorkInformation"/></h2>
<span class="error"><!-- Error messages go here -->
	<html:errors/><br />
</span>

<logic:present name="scheduling">
	<strong>
		<bean:message bundle="APPLICATION_RESOURCES" key="message.final.degree.work.execution.degrees"/>
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orientatorOID" property="orientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coorientatorOID" property="coorientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.alteredField" property="alteredField"/>

	<%
	    boolean showCoordinator = false; 
	    boolean showCompanion = false;
	%>
	<logic:empty name="finalWorkInformationForm" property="companionName">
		<logic:empty name="finalWorkInformationForm" property="companionMail">
			<logic:empty name="finalWorkInformationForm" property="companionPhone">
				<logic:empty name="finalWorkInformationForm" property="companyAdress">
					<logic:empty name="finalWorkInformationForm" property="companyName">
						<%
						    showCoordinator = true;
						%>
					</logic:empty>
				</logic:empty>
			</logic:empty>
		</logic:empty>
	</logic:empty>
	<logic:empty name="finalWorkInformationForm" property="coResponsableTeacherName">
		<%
		    showCompanion = true;
		%>
	</logic:empty>
	<%
		final net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing scheduleing = (net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing) request.getAttribute("scheduling");
		if (scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
			showCoordinator = true;
			showCompanion = true;
		}
		request.setAttribute("showCoordinator", showCoordinator);
		request.setAttribute("showCompanion", showCompanion);
	%>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.title"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" property="title" size="85"/>
	<hr/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.responsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.number" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>:</th>
			<td width="10%">
				<logic:present name="orientator">
					<%
						String orientatorPersonId = "";
						orientatorPersonId = ((Person) pageContext.findAttribute("orientator")).getIstUsername().toString();
					%>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherId" property="responsableTeacherId" size="6"
						value='<%= orientatorPersonId %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherId" property="responsableTeacherId"  size="6"
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='orientator';this.form.submit();"/>
				</logic:notPresent>
			</td>
			<td width="1%"/>
			<th width="7%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.name"/>:</th>
			<td width="66%">
				<logic:present name="orientator">
					<html:text disabled="true" bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherName" property="responsableTeacherName" size="55"
						value='<%= ((Person) pageContext.findAttribute("orientator")).getName().toString() %>'/>
				</logic:present>
				<logic:notPresent name="orientator">
					<html:text disabled="true" bundle="HTMLALT_RESOURCES" altKey="text.responsableTeacherName" property="responsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br/><br/>

	<logic:equal name="showCoordinator" value="true">
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.coResponsable"/>:</b>
	<table width="100%">
		<tr>
			<th width="16%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.number" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>:</th>
			<td width="10%">
				<logic:present name="coorientator">
					<%
						String coorientatorPersonId = "";
						coorientatorPersonId = ((Person) pageContext.findAttribute("coorientator")).getIstUsername().toString();
					%>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherId" property="coResponsableTeacherId" size="6"
						value='<%= coorientatorPersonId %>'
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
						/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherId" property="coResponsableTeacherId" size="6" 
						 onchange="this.form.method.value='showTeacherName';this.form.page.value='1';this.form.alteredField.value='coorientator';this.form.submit();"  
					/>
				</logic:notPresent>
			</td>
			<td width="1%"/>
			<th width="7%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.name"/>:</th>
			<td width="66%">
				<logic:present name="coorientator">
					<html:text disabled="true" bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherName" property="coResponsableTeacherName" size="55"
						value='<%= ((Person) pageContext.findAttribute("coorientator")).getName().toString() %>'/>
				</logic:present>
				<logic:notPresent name="coorientator">
					<html:text disabled="true" bundle="HTMLALT_RESOURCES" altKey="text.coResponsableTeacherName" property="coResponsableTeacherName" size="55"/>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<br/><br/>
	</logic:equal>

	<logic:equal name="showCompanion" value="true">
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.companion"/>:</b>
	<table width="100%">
		<tr>
			<th width="9%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.name"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionName" property="companionName" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"  />						
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.mail"/>:</th>
			<td>		
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionMail" property="companionMail" size="70" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.phone"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companionPhone" property="companionPhone" size="10" maxlength="9" 
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>						
			</td>
		</tr>	
		<tr>
			<th width="9%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.companyName"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companyName" property="companyName" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
			</td>
		</tr>
		<tr>
			<th width="9%"><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.companyAdress"/>:</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.companyAdress" property="companyAdress" size="70"
				onchange="this.form.method.value='coorientatorVisibility';this.form.page.value='1';this.form.alteredField.value='companion';this.form.submit();"/>
			</td>
		</tr>
	</table>
	<br/><br/>
	</logic:equal>
				
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.credits"/>:</b>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsibleCreditsPercentage" property="responsibleCreditsPercentage" size="3" maxlength="3"/>% /
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.coResponsibleCreditsPercentage" property="coResponsibleCreditsPercentage" size="3" maxlength="3"/>%
	<br/><hr/>

	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.framing"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.framing" property="framing" rows="4" cols="80"/>
	<br/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.objectives"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.objectives" property="objectives" rows="4" cols="80"/>
	<br/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.description"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.description" property="description" rows="8" cols="80"/>
	<br/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.requirements"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.requirements" property="requirements" rows="8" cols="80"/>
	<br/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.deliverable"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.deliverable" property="deliverable" rows="4" cols="80"/>
	<br/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.url"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.url" property="url" size="80"/>

	<br/><hr/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.observations"/>:</b>
	<br/><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.observations" property="observations" rows="4" cols="80"/><br/><br/>
	<b><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.location"/>:</b>
	<br/><html:text bundle="HTMLALT_RESOURCES" altKey="text.location" property="location" size="81"/><br/>

	<br/><br/><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="APPLICATION_RESOURCES" key="button.submit"/></html:submit>
</html:form>


<html:form action="/finalWorkManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseDegree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.role" property="role"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.orientatorOID" property="orientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coorientatorOID" property="coorientatorOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.alteredField" property="alteredField"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="APPLICATION_RESOURCES" key="button.cancel"/></html:submit>
</html:form>	