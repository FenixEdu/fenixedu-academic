<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<ul>
	<li class="navheader"><bean:message  key="label.coordinator.management"/></li>
	<li>
		<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=prepare" %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/>
		</html:link>
	</li>
</ul>
