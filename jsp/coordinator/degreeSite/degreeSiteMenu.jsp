<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoExecutionDegreeId" name="infoExecutionDegree" property="idInternal"/>
	
	</br>
	</br>
	<hr></hr>
	<h2><center><bean:message key="link.coordinator.degreeSite.management"/></center></h2>
	
	<p>
	<ul>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
			    <bean:message key="link.coordinator.degreeSite.edit" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewHistoric&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
			    <bean:message key="link.coordinator.degreeSite.historic" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewDegreeSite&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">			
			    <bean:message key="link.coordinator.degreeSite.viewSite" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>		
	</ul>
	</p>
</logic:present>