<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<ul class="treemenu">
	<li><a href="/html/destaques/">Destaques</a></li>
	<li><a href="/html/instituto/">Instituto</a></li>
	<li><a href="/html/estrutura/">Estrutura</a></li>
  	<li class="treenode"><a href="">Ensino</a>
		<ul class="expmenu">
			<li><html:link page="<%= "/showDegrees.do?method=nonMaster" %>" ><bean:message key="link.degree.nonMaster"/></html:link></li>
<%-- &executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) --%>
			<li><a href="http://www.ist.utl.pt/html/ensino/pos_grad.html">Pós-graduações</a></li>
			<li><html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.master"/></html:link></li>
			<li><a href="http://www.ist.utl.pt/html/ensino/doutoramentos.html">Doutoramentos</a></li>
		</ul>
	</li>
	<li><a href="/html/id/">I &amp; D</a></li>
	<li><a href="/html/sociedade/">Liga&ccedil;&atilde;o &agrave; Sociedade </a></li>
	<li><a href="/html/viverist/">Viver no IST</a></li>
	 <li><a href="http://www.ist.utl.pt/html/recursos/">Recursos</a></li> 
	<%--	<li><html:link page="<%= "/prepareConsultRoomsNew.do?method=prepare" %>" >Recursos</html:link></li>--%>
</ul>
