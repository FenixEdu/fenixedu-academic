<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />

<h2><bean:message key="title.coordinationTeam"/></h2>

<ul>
	<logic:iterate id="executionDegree" name="executionDegrees" >
		<li>
			<bean:define id="executionDegreeId" name="executionDegree" property="externalId" />
			<html:link page='<%= "/viewCoordinationTeam.do?method=viewTeam&infoExecutionDegreeId="+ executionDegreeId + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
				<bean:write name="executionDegree" property="infoExecutionYear.year" />
			</html:link>
		</li>
	</logic:iterate>
</ul>