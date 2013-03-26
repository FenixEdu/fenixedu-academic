<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />

<h2><bean:message key="title.coordinationTeam"/></h2>

<ul>
	<logic:iterate id="executionDegree" name="executionDegrees" >
		<li>
			<bean:define id="executionDegreeId" name="executionDegree" property="idInternal" />
			<html:link page='<%= "/viewCoordinationTeam.do?method=viewTeam&infoExecutionDegreeId="+ executionDegreeId + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
				<bean:write name="executionDegree" property="infoExecutionYear.year" />
			</html:link>
		</li>
	</logic:iterate>
</ul>