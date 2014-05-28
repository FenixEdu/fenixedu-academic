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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean"%>
<%@page import="net.sourceforge.fenixedu.domain.person.PersonName"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice.PhdThesisProcessDA"%>


<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%><html:xhtml/>

<%-- init values --%>
<% 
	request.setAttribute("createMethod", request.getParameter("createMethod"));
	request.setAttribute("invalidMethod", request.getParameter("invalidMethod"));
	request.setAttribute("postbackMethod", request.getParameter("postbackMethod"));
%> 		

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="createMethod" name="createMethod" />
<bean:define id="invalidMethod" name="invalidMethod" />
<bean:define id="postbackMethod" name="postbackMethod" />


<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId.toString() %>">

	<input type="hidden" name="method" value="" />
	<fr:edit id="thesisJuryElementBean" name="thesisJuryElementBean" visible="false" />

	<fr:edit id="thesisJuryElementBean.jury.type" name="thesisJuryElementBean" >
		
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisJuryElementBean.class.getName() %>">
			<fr:slot name="participantSelectType" layout="radio-postback" required="true">
				<fr:property name="classes" value="liinline nobullet"/>
				<fr:property name="bundle" value="PHD_RESOURCES" />
			</fr:slot>
			
			<%-- ################################################# --%>
			<%-- ### Jury Type selected (NEW or EXISTING) ### --%>
			<%-- ################################################# --%>
	
			<logic:notEmpty name="thesisJuryElementBean" property="participantSelectType">
	
				<%-- Add existing participant to thesis jury --%>
				<logic:equal name="thesisJuryElementBean" property="participantSelectType.name" value="EXISTING">
					<fr:slot name="participant" layout="menu-select" required="true" >
						<fr:property name="classes" value="nobullet noindent" />
						<fr:property name="providerClass" value="<%= PhdThesisProcessDA.ExistingPhdParticipantsEvenInPhdThesisProcess.class.getName() %>" />
						<fr:property name="eachLayout" value="values" />
						<fr:property name="eachSchema" value="PhdParticipant.view.name.with.title" />
	
						<fr:property name="sortBy" value="name" />
					</fr:slot>
				</logic:equal>
	
				<%-- or create a new one (must select internal or external) --%>
				<logic:equal name="thesisJuryElementBean" property="participantSelectType.name" value="NEW">
	
					<%-- select internal or external element type --%>
					<fr:slot name="participantType" layout="radio-postback" required="true">
						<fr:property name="classes" value="liinline nobullet"/>
						<fr:property name="bundle" value="PHD_RESOURCES" />
					</fr:slot>					
	
					<%-- selected new participant type --%>
					<logic:notEmpty name="thesisJuryElementBean" property="participantType">
	
						<%-- INTERNAL jury type slots --%>
						<logic:equal name="thesisJuryElementBean" property="participantType.name" value="INTERNAL">
							<fr:slot name="personName" layout="autoComplete">
								<fr:property name="size" value="50"/>
								<fr:property name="labelField" value="person.name"/>
								<fr:property name="format" value="${person.name} (${person.user.username})" />
								<fr:property name="indicatorShown" value="true"/>		
								<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchInternalPersonsByNameHavingTeacherOrIsResearcher"/>
								<fr:property name="args" value="size=50"/>
								<fr:property name="className" value="<%= PersonName.class.getName() %>"/>
								<fr:property name="minChars" value="4"/>	
								<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator" />			
							</fr:slot>
						</logic:equal>
						
						<%-- EXTERNAL element --%>						
						<logic:equal name="thesisJuryElementBean" property="participantType.name" value="EXTERNAL">
							<fr:slot name="name" required="true" >
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="qualification" required="true" >
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="category" required="true" >
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="email" required="true" >
								<fr:validator name="<%= EmailValidator.class.getName() %>" />
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="address" >
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="phone" >
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="title">
								<fr:property name="size" value="30" />
							</fr:slot>
							<fr:slot name="workLocation">
								<fr:property name="size" value="50" />
							</fr:slot>
							<fr:slot name="institution">
								<fr:property name="size" value="50" />
							</fr:slot>
						</logic:equal>
						<%-- end: create new external element --%>
					</logic:notEmpty>

				</logic:equal>

				<%if (request.getParameter("juryElement") != null) { %>
					<fr:slot name="reporter" />
					<fr:slot name="expert" />
				<%} %>

			</logic:notEmpty>

		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= String.format("/phdThesisProcess.do?method=%s&processId=%s", invalidMethod, processId.toString()) %>"/>
		<fr:destination name="postBack" path="<%= String.format("/phdThesisProcess.do?method=%s&processId=%s", postbackMethod , processId.toString()) %>"/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="<%= String.format("this.form.method.value='%s';return true;", createMethod) %>"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='manageThesisJuryElements';return true;"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
</fr:form>
