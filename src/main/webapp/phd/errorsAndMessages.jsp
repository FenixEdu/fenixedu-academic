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

<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:messagesPresent message="true" property="success">
	<div class="mvert15">
		<span class="success0">
			<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="warning">
	<div class="warning1 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="warning">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<% 
	String[] viewStateIdsParam = request.getParameterValues("viewStateId");
	List<String> viewStateIds = (viewStateIdsParam != null) ? Arrays.asList(viewStateIdsParam) : Collections.EMPTY_LIST;
	request.setAttribute("viewStateIds",viewStateIds);
%> 

<logic:iterate id="eachViewStateId" name="viewStateIds">
	<fr:hasMessages type="conversion" for="<%= eachViewStateId.toString() %>">
		<div class="error3" style="width: 500px;">
			<fr:messages>
				<p class="mvert025"><fr:message show="label"/>: <fr:message /></p>
			</fr:messages>
		</div>
	</fr:hasMessages>
</logic:iterate>



