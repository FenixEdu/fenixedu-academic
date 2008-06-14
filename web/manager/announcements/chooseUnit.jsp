<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<p class="mtop2"><bean:message key="manager.announcements.chooseUnit" bundle="MANAGER_RESOURCES"/>:<p>

<script language="JavaScript">
function check(e,v)
{

	if (e.style.display == "none")
	  {
	  e.style.display = "";
	  v.src = "<%= request.getContextPath() %>/images/toggle_minus10.gif";
	  }
	else
	  {
	  e.style.display = "none";
	  v.src = "<%= request.getContextPath() %>/images/toggle_plus10.gif";
	  }
}
</script>

<un:tree initialUnit="initialUnit" unitParamName="keyUnit" path="/manager/announcements/manageUnitAnnouncementBoard.do?method=prepareCreateBoard" state="true"/>
