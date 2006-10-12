<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.StartHiddenActionMessages" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<script language="JavaScript">
function check(e)
{
if (e.style.display == "none")
  {
	  e.style.display = "";
  }
else
  {
	  e.style.display = "none";
  }
}
</script>


<span class="infoMsg">
		 <%
				int infoMessageNumber =0;
				ActionMessages infoMessages = (ActionMessages) request.getAttribute(org.apache.struts.Globals.MESSAGE_KEY);
		 %>
		<html:messages id="infoMsg" message="true" bundle="MESSAGING_RESOURCES">
			<logic:present name="infoMsg">
				<ul onclick="check(document.getElementById('<%="I"+infoMessageNumber%>'));check(document.getElementById('<%="ID"+infoMessageNumber%>'));">
					<div id="<%="ID"+infoMessageNumber%>" <%if(! (infoMessages instanceof StartHiddenActionMessages)) {%> style="display:none"<%} %>>
						<u><bean:message bundle="MESSAGING_RESOURCES" key="messaging.context.infoDetails"/></u>
					</div>
					<div id="<%="I"+infoMessageNumber%>" <%if(infoMessages instanceof StartHiddenActionMessages) {%> style="display:none"<%} %>>
						<li onclick="check(document.getElementById('<%=infoMessageNumber %>'));">
							<span class="error0"><bean:write name="infoMsg" filter="false"/></span>
						</li>
					</div>
				</ul>
				<% infoMessageNumber++; %>
			</logic:present>		 
		</html:messages>
</span>

<span class="error">
		 <%
				int errorMessageNumber =0;
				ActionMessages errorMessages = (ActionMessages) request.getAttribute(org.apache.struts.Globals.ERROR_KEY);
				if(errorMessages != null)
		 %>
		<html:messages id="errorMsg" message="false" bundle="MESSAGING_RESOURCES">
			<logic:present name="errorMsg">
				<ul onclick="check(document.getElementById('<%="E"+errorMessageNumber%>'));check(document.getElementById('<%="ED"+errorMessageNumber%>'));">
					<div id="<%="ED"+errorMessageNumber%>" <%if(!(errorMessages instanceof StartHiddenActionMessages)) {%> style="display:none"<%} %>>
						<u><bean:message bundle="MESSAGING_RESOURCES" key="messaging.context.errorDetails"/></u>
					</div>
					<div id="<%="E"+errorMessageNumber%>" <%if(errorMessages instanceof StartHiddenActionMessages) {%> style="display:none"<%} %>>
						<li>
							<span class="error0"><bean:write name="errorMsg" filter="false"/></span>
						</li>
					</div>
				</ul>
				<% errorMessageNumber++; %>
			</logic:present>
		</html:messages>
</span>
