<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<span class="error"><html:errors/></span>

<logic:present name="infoDegreeInfo">

	<div  class="breadcumbs"><a href="/index.shtml">IST</a> > <a href="/html/ensino/ensino.shtml">Ensino</a> &gt;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" + request.getAttribute("degreeId").toString() %>">
			<bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
		</html:link>
		 &gt;<bean:message key="label.accessRequirements"/>
	</div>
	
	<!-- PÁGINA EM INGLÊS -->
	<div class="version"><span class="px10"><a href="/html/en/teaching.shtml">english version</a> <img src="/img/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div>
	<div class="clear"></div> 
	
	<h1><bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeInfo" property="infoDegree.nome" /></h1>
	<!-- ANO LECTIVO -->
	<logic:present name="schoolYear">
	  <h2><span class="redbox"><bean:write name="schoolYear" /></span>
  </logic:present>  
  <span class="greytxt">&nbsp;<bean:message key="label.accessRequirements"/></span></h2>
  <br>

	<!-- NOME(S) DA PROVA(S) DE INGRESSO -->
	<logic:present name="infoDegreeInfo" property="testIngression">
  <h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.testIngression" /></h2>  
  <p><bean:write name="infoDegreeInfo" property="testIngression" filter="false" /></p>
  </logic:present>
  
  <!-- VAGAS -->
 	<logic:present name="infoDegreeInfo" property="driftsInitial">
	  <h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.drifts" /></h2>
	  <ul>
	  	<li><strong><bean:message key="label.coordinator.degreeSite.driftsInitial" />:</strong> <bean:write name="infoDegreeInfo" property="driftsInitial" /></li>
	  	
			<logic:present name="infoDegreeInfo" property="driftsFirst">  	
	    <li><strong><bean:message key="label.coordinator.degreeSite.driftsFirst" />:</strong> <bean:write name="infoDegreeInfo" property="driftsFirst" /></li>
			</logic:present>    
    
			<logic:present name="infoDegreeInfo" property="driftsSecond">    
			<li><strong><bean:message key="label.coordinator.degreeSite.driftsSecond" />:</strong> <bean:write name="infoDegreeInfo" property="driftsSecond" /></li>		
			</logic:present>
	  </ul>			
	</logic:present>    	  	
	  	
  <!-- CLASSIFICAÇÕES-->
  <logic:present name="infoDegreeInfo" property="classifications">
	  <h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.classifications" /></h2>
	 	<bean:write name="infoDegreeInfo" property="classifications" filter="false" />
	</logic:present>
 	
 	<!-- NOTAS -->
 	<logic:present name="infoDegreeInfo" property="markAverage">	 	
		<h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.marks" /></h2>
	  <ul>
	  	<li><strong><bean:message key="label.coordinator.degreeSite.mark.average" />:</strong> <bean:write name="infoDegreeInfo" property="markAverage" /></li>
	  	
			<logic:present name="infoDegreeInfo" property="markMin">  	
	    <li><strong><bean:message key="label.coordinator.degreeSite.markMin" />:</strong> <bean:write name="infoDegreeInfo" property="markMin" /></li>
			</logic:present>    
    
			<logic:present name="infoDegreeInfo" property="markMax">    
			<li><strong><bean:message key="label.coordinator.degreeSite.markMax" />:</strong> <bean:write name="infoDegreeInfo" property="markMax" /></li>		
			</logic:present>
	  </ul>			
	</logic:present>

</logic:present>