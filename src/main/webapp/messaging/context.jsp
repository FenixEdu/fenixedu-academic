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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<span class="infoMsg">
	<!-- w3c compliant -->
		 <%
				int infoMessageNumber =0;
				ActionMessages infoMessages = (ActionMessages) request.getAttribute(org.apache.struts.Globals.MESSAGE_KEY);
		 %>
		<html:messages id="infoMsg" message="true" bundle="MESSAGING_RESOURCES">
			<logic:present name="infoMsg">
				<ul onclick="check(document.getElementById('<%="I"+infoMessageNumber%>'));check(document.getElementById('<%="ID"+infoMessageNumber%>'));">
					<div id="<%="ID"+infoMessageNumber%>">
						<u><bean:message bundle="MESSAGING_RESOURCES" key="messaging.context.infoDetails"/></u>
					</div>
					<div id="<%="I"+infoMessageNumber%>">
						<li onclick="check(document.getElementById('<%=infoMessageNumber %>'));">
							<span class="error0"><bean:write name="infoMsg" filter="false"/></span>
						</li>
					</div>
				</ul>
				<% infoMessageNumber++; %>
			</logic:present>		 
		</html:messages>
</span>

<span class="error"><!-- w3c compliant -->

		 <%
				int errorMessageNumber =0;
				ActionMessages errorMessages = (ActionMessages) request.getAttribute(org.apache.struts.Globals.ERROR_KEY);
				if(errorMessages != null)
		 %>
		<html:messages id="errorMsg" message="false" bundle="MESSAGING_RESOURCES">
			<logic:present name="errorMsg">
				<ul onclick="check(document.getElementById('<%="E"+errorMessageNumber%>'));check(document.getElementById('<%="ED"+errorMessageNumber%>'));">
					<div id="<%="ED"+errorMessageNumber%>">
						<u><bean:message bundle="MESSAGING_RESOURCES" key="messaging.context.errorDetails"/></u>
					</div>
					<div id="<%="E"+errorMessageNumber%>">
						<li>
							<span class="error0"><bean:write name="errorMsg" filter="false"/></span>
						</li>
					</div>
				</ul>
				<% errorMessageNumber++; %>
			</logic:present>
		</html:messages>
</span>
