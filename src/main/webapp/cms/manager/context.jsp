<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.StartHiddenActionMessages" %>


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
		<html:messages id="infoMsg" message="true" bundle="CMS_RESOURCES">
			<logic:present name="infoMsg">
				<div onclick="check(document.getElementById('<%="I"+infoMessageNumber%>'));check(document.getElementById('<%="ID"+infoMessageNumber%>'));">
					<div id="<%="ID"+infoMessageNumber%>" <%if(! (infoMessages instanceof StartHiddenActionMessages)) {%> style="display:none"<%} %>>
						<a href="#"/><bean:message key="cms.context.showDetails" bundle="CMS_RESOURCES"/></a>
					</div>
					<div id="<%="I"+infoMessageNumber%>" <%if(infoMessages instanceof StartHiddenActionMessages) {%> style="display:none"<%} %>>
							<div onclick="check(document.getElementById('<%=infoMessageNumber %>'));">
								<a href="#"/><bean:message key="cms.context.hideDetails" bundle="CMS_RESOURCES"/></a>
								<div class="infoop2">
									<bean:write name="infoMsg" filter="false"/>
								</div>
							</div>
					</div>
				</div>
				<% infoMessageNumber++; %>
			</logic:present>		 
		</html:messages>
</span>

<span class="error0"><!-- Error messages go here -->
		 <%
				int errorMessageNumber =0;
				ActionMessages errorMessages = (ActionMessages) request.getAttribute(org.apache.struts.Globals.ERROR_KEY);
				if(errorMessages != null)
		 %>
		<html:messages id="errorMsg" message="false" bundle="CMS_RESOURCES">
			<logic:present name="errorMsg">
				<div onclick="check(document.getElementById('<%="E"+errorMessageNumber%>'));check(document.getElementById('<%="ED"+errorMessageNumber%>'));">
					<div id="<%="ED"+errorMessageNumber%>" <%if(! (errorMessages instanceof StartHiddenActionMessages)) {%> style="display:none"<%} %>>
						<a href="#"/><bean:message key="cms.context.showErrors" bundle="CMS_RESOURCES"/></a>
					</div>
					<div id="<%="E"+errorMessageNumber%>" <%if(errorMessages instanceof StartHiddenActionMessages) {%> style="display:none"<%} %>>
							<div onclick="check(document.getElementById('<%=infoMessageNumber %>'));">
								<a href="#"/><bean:message key="cms.context.hideErrors" bundle="CMS_RESOURCES"/></a>
								<div class="error2">
									<bean:write name="errorMsg" filter="false"/>
								</div>
							</div>
					</div>
				</div>
				<% errorMessageNumber++; %>
			</logic:present>
		</html:messages>
</span>