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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>

<html:xhtml/>

<!-- Configuration -->

<%
	final String notifyElements = request.getParameter("notifyElements");
	if (notifyElements != null) {
		request.setAttribute("notifyElements", notifyElements);
	} else {
	    request.setAttribute("notifyElements", Boolean.FALSE);
	}
	
	request.setAttribute("submitMethod", request.getParameter("submitMethod"));
	request.setAttribute("invalidMethod", request.getParameter("invalidMethod"));
	request.setAttribute("postBackMethod", request.getParameter("postBackMethod"));
	
	if(request.getParameter("processName") != null){
	    request.setAttribute("processName", request.getParameter("processName"));
	}
	else{
	    request.setAttribute("processName", "phdThesisProcess");
	}
%>

<bean:define id="submitMethod" name="submitMethod" />
<bean:define id="invalidMethod" name="invalidMethod" />
<bean:define id="postBackMethod" name="postBackMethod" />
<bean:define id="processName" name="processName" />

<bean:define id="processId" name="process" property="externalId" />

<!-- End of configuration -->

<fr:form action="<%=String.format("/%s.do?processId=%s", processName, processId) %>">

	<input type="hidden" name="method" value="" />
	<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />

	<br/><br/>
	<strong><bean:message key="label.phd.thesis.schedule" bundle="PHD_RESOURCES" /></strong>
	<fr:edit id="thesisProcessBean-info" name="thesisProcessBean">

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= String.format("/%s.do?method=%s&processId=%s", processName, invalidMethod ,processId) %>" />
		<fr:destination name="postBack" path="<%= String.format("/%s.do?method=%s&processId=%s", processName, postBackMethod, processId) %>"/>

		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">

			<logic:equal name="notifyElements" value="true">
				<fr:slot name="toNotify" layout="radio-postback">
					<fr:property name="classes" value="liinline nobullet"/>
				</fr:slot>
			</logic:equal>

			<fr:slot name="scheduledDate" required="true" />
			<fr:slot name="scheduledPlace" required="true">
				<fr:property name="size" value="40" />
			</fr:slot>
		</fr:schema>

	</fr:edit>
	
	<logic:equal name="thesisProcessBean" property="toNotify" value="true">
		<br/><br/>
		<strong><bean:message key="label.phd.email.to.send" bundle="PHD_RESOURCES" />:</strong>
		<fr:edit id="thesisProcessBean-mail-information" name="thesisProcessBean">
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
	
			<fr:destination name="invalid" path="<%= String.format("/%s.do?method=%s&processId=&s", processName, invalidMethod ,processId) %>" />
			<fr:destination name="postBack" path="<%= String.format("/%s.do?method=%s&processId=%s", processName, postBackMethod, processId) %>"/>
	
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
				<fr:slot name="mailSubject" required="true">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="mailBody" layout="longText" required="true">
					<fr:property name="rows" value="15" />
					<fr:property name="columns" value="100" />
				</fr:slot>
			</fr:schema>
	
		</fr:edit>
	
		<p>
			<strong>Notas - </strong> O sistema irá anexar automaticamente no final da mensagem de mail a seguinte informação:
		</p>
		<ul>
			<li>Data, hora e local introduzidos</li>
			<li>Informação de acesso aos pareceres para os elementos do júri</li>
		</ul>
	</logic:equal>	
	
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="<%= String.format("this.form.method.value='%s';", submitMethod) %>"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	

</fr:form>
