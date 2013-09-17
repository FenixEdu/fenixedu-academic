<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<br />

<bean:define id="backendInstance" name="backendInstance" type="net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance"/>
<bean:define id="backendInstanceUrl" type="java.lang.String">&amp;backendInstance=<%= backendInstance.name() %></bean:define>


<logic:present name="infoCostCenterList">
	<ul>
		<li class="navheader"><bean:message key="title.costCenter" /></li>
		<logic:iterate id="costCenter" name="infoCostCenterList">
			<bean:define id="code" name="costCenter" property="code"/>
			<li><html:link page="<%="/index.do?costCenter=" + code+backendInstanceUrl%>">
				<bean:write name="costCenter" property="description" />
			</html:link></li>
		</logic:iterate>
	</ul>
	<br />
</logic:present>
