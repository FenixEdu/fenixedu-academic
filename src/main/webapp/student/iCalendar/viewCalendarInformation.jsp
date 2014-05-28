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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="registrationId" name="registrationId" />

<h2><bean:message key="label.title.calendar"
	bundle="MESSAGING_RESOURCES" /></h2>

<div class="infoop2">
<bean:message key="label.calendar.details" bundle="MESSAGING_RESOURCES" />
</div>
<bean:message key="label.calendar.info" bundle="MESSAGING_RESOURCES" />
<logic:notPresent name="expirationDate">
	<p><strong><html:link page="<%= "/ICalTimeTable.do?method=generateKey&registrationId=" + String.valueOf(registrationId) %>" bundle="MESSAGING_RESOURCES" titleKey="label.key.firsttime"><bean:message key="label.key.firsttime" bundle="MESSAGING_RESOURCES"/></html:link></strong></p>
</logic:notPresent>

<logic:present name="expirationDate">
	<bean:define id="classURL" name="classURL" />
	<bean:define id="examsURL" name="examsURL" />

	
		<p><strong><html:link page="<%= "/ICalTimeTable.do?method=generateKey&registrationId=" + String.valueOf(registrationId) %>" bundle="MESSAGING_RESOURCES"  titleKey="label.key.new"><bean:message key="label.key.new" bundle="MESSAGING_RESOURCES"/></html:link></strong></p>
	A sua chave é válida até <b><bean:write name="expirationDate" /></b>
	<logic:equal name="stillValid" value="false">
		<p class="mbottom05"><em class="highlight5">A chave perdeu a
		validade. Para actualizar os links gere uma nova chave.</em></p>
	</logic:equal>
	<logic:equal name="stillValid" value="true">
		<p>
		<form name="embedForm1">Calendário de aulas: <input
			id="embed_code" style="width: 600px;" type="text" readonly="readonly"
			onclick="javascript:document.embedForm1.embed_code.focus();document.embedForm1.embed_code.select();"
			value="<%= classURL.toString() %>">	
		</form>
		</p>

		<p>
		<form name="embedForm2">Calendário de testes: <input
			id="embed_code" style="width: 600px;" type="text" readonly="readonly"
			onclick="javascript:document.embedForm2.embed_code.focus();document.embedForm2.embed_code.select();"
			value="<%= examsURL.toString() %>""></form>
		</p>
	</logic:equal>

</logic:present>
</div>

