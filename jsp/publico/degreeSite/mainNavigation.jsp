<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<ul class="treemenu">
	<li><a href="/html/destaques/">Destaques</a></li>
	<li><a href="/html/instituto/">Instituto</a></li>
	<li><a href="/html/estrutura/">Estrutura</a></li>
  	<li class="treenode"><a href="">Ensino</a>
		<ul class="expmenu">
			<li><html:link page="<%= "/showDegrees.do?method=nonMaster" %>" ><bean:message key="link.degree.nonMaster"/></html:link></li>
			<bean:define id="institutionUrlPostGraduation" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/ensino/pos_grad.html</bean:define>
			<li><a href="<%= institutionUrlPostGraduation %>">P�s-gradua��es</a></li>
			<li><html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.master"/></html:link></li>
			<bean:define id="institutionUrlPhd" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/ensino/doutoramentos.html</bean:define>
			<li><a href="<%= institutionUrlPhd %>">Doutoramentos</a></li>
		</ul>
	</li>
	<li><a href="/html/id/">I &amp; D</a></li>
	<li><a href="/html/sociedade/">Liga&ccedil;&atilde;o &agrave; Sociedade </a></li>
	<li><a href="/html/viverist/">Viver no IST</a></li>
	<li><html:link page="<%= "/prepareConsultRoomsNew.do?method=prepare" %>" >Recursos</html:link></li>
</ul>
