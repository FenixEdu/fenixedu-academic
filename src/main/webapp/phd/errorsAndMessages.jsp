<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


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



