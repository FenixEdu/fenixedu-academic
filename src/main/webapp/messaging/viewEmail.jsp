<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="java.util.Set"%>
<%@page import="java.util.TreeSet"%>
<%@page import="org.fenixedu.academic.domain.util.EmailAddressList"%><html:xhtml/>

<h2><bean:message bundle="MESSAGING_RESOURCES" key="title.email.sent.emails"/></h2>
<logic:present name="message">


	<logic:empty name="message" property="emails">
	
		<p class="mtop15">
			<html:link page="/viewSentEmails.do?method=resubmit" paramId="messagesId" paramName="message" paramProperty="externalId">
				<bean:message bundle="MESSAGING_RESOURCES" key="link.message.email.resubmit"/>
		</html:link>
		</p>
	</logic:empty>
	<logic:present name="created">
		<p class="mtop15">
			<span class="success0">
				<bean:message bundle="MESSAGING_RESOURCES" key="message.email.sent"/>
			</span>
		</p>
	</logic:present>

	<logic:notPresent name="message" property="sent">
		<html:link page="/viewSentEmails.do?method=deleteMessage" paramId="messagesId" paramName="message" paramProperty="externalId">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.delete"/>
		</html:link>
	</logic:notPresent>

	<fr:view name="message" schema="org.fenixedu.academic.domain.util.email.Message.info">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thleft thtop"/>
			<fr:property name="columnClasses" value="width11em,,,"/>
		</fr:layout>
	</fr:view>
	
	<h3><bean:message bundle="MESSAGING_RESOURCES" key="title.email.sent.emails.resume"/></h3>
	<fr:view name="message" schema="email.sent.resume">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thleft thtop"/>
			<fr:property name="columnClasses" value="width30em,taright"/>
		</fr:layout>
	</fr:view>
	
	<% final Set failed = new TreeSet(); %>
	<logic:iterate id="utilEmail" type="org.fenixedu.academic.domain.util.Email" name="message" property="emails">
		<%
			final EmailAddressList failedAddressList = utilEmail.getFailedAddresses();
			if (failedAddressList != null && !failedAddressList.isEmpty()) {
			    failed.addAll(failedAddressList.toCollection());
			}
		%>
	</logic:iterate>
	<%
		if (!failed.isEmpty()) {
		    %>
				<h3>
					Não foi possível entregar o e-mail aos seguintes destinatários:
				</h3>
		    	<ul>
		    <%
		    for (final Object addr : failed) {
			    %>
		    		<li><font color="red"><%= addr.toString() %></font></li>
			    <%
		    }
		    %>
		    	</ul>
		    <%
		}
	%>

	
	<logic:present role="role(MANAGER)">
		<br>
		<br>
		<br>
		<span>
			The following information is only visible to users with the role MANAGER
		</span>
			<logic:iterate id="utilEmail" name="message" property="emails">
				<h4>
					<bean:write name="utilEmail" property="externalId"/>
				</h4>
				<h5>
					Failed:
				</h5>
				<p>
					<logic:present name="utilEmail" property="failedAddresses">
						<bean:write name="utilEmail" property="failedAddresses"/>
					</logic:present>
				</p>
				<h5>
					Possibly Sent:
				</h5>
				<p>
					<logic:present name="utilEmail" property="confirmedAddresses">
						<bean:write name="utilEmail" property="confirmedAddresses"/>
					</logic:present>
				</p>
			</logic:iterate>
	</logic:present>

</logic:present>
