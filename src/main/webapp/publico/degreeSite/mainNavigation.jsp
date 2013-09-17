<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<ul class="treemenu">
	<li><a href="/html/instituto/">Instituto</a></li>
	<li><a href="/html/estrutura/">Estrutura</a></li>
  	<li class="treenode"><a href="">Ensino</a>
		<ul class="expmenu">
			<li><html:link page="<%= "/showDegrees.do?method=nonMaster" %>" ><bean:message key="link.degree.nonMaster"/></html:link></li>
			<bean:define id="institutionUrlPostGraduation" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/ensino/pos_grad.html</bean:define>
			<li><a href="<%= institutionUrlPostGraduation %>">Pós-graduações</a></li>
			<li><html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.master"/></html:link></li>
			<bean:define id="institutionUrlPhd" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/ensino/doutoramentos.html</bean:define>
			<li><a href="<%= institutionUrlPhd %>">Doutoramentos</a></li>
		</ul>
	</li>
	<li><a href="/html/id/">I &amp; D</a></li>
	<li><a href="/html/viverist/">Viver no IST</a></li>
</ul>
