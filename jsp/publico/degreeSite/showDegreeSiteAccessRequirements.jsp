<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeInfo">

	<div  class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> > <html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino</html:link> &gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
		</html:link>
		 &gt;&nbsp;<bean:message key="label.accessRequirements"/>
	</div>
	
	<!-- PÁGINA EM INGLÊS -->
	<!-- <div class="version"><span class="px10"><a href="#">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div> -->
	<div class="clear"></div> 
	
	<h1><bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeInfo" property="infoDegree.nome" /></h1>
	<h2><span class="greytxt"><bean:message key="label.accessRequirements"/></span></h2>
  <br>

	<!-- NOME(S) DA PROVA(S) DE INGRESSO -->
  <logic:notEmpty name="infoDegreeInfo" property="testIngression">
  	<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.testIngression" /></h2>  
	<p><bean:write name="infoDegreeInfo" property="testIngression" filter="false" /></p>
  </logic:notEmpty>
  
  <!-- VAGAS -->
 	<logic:notEmpty name="infoDegreeInfo" property="driftsInitial">
	  <h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.drifts" /></h2>
	  <ul>
	  	<li><strong><bean:message key="label.coordinator.degreeSite.driftsInitial" />:</strong> <bean:write name="infoDegreeInfo" property="driftsInitial" /></li>
	  	
			<logic:notEmpty name="infoDegreeInfo" property="driftsFirst">  	
	    <li><strong><bean:message key="label.coordinator.degreeSite.driftsFirst" />:</strong> <bean:write name="infoDegreeInfo" property="driftsFirst" /></li>
			</logic:notEmpty>    
    
			<logic:notEmpty name="infoDegreeInfo" property="driftsSecond">    
			<li><strong><bean:message key="label.coordinator.degreeSite.driftsSecond" />:</strong> <bean:write name="infoDegreeInfo" property="driftsSecond" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>    	  	
	  	
  <!-- CLASSIFICAÇÕES-->
  <logic:notEmpty name="infoDegreeInfo" property="classifications">
	  <h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.classifications" /></h2>
	 	<bean:write name="infoDegreeInfo" property="classifications" filter="false" />
	</logic:notEmpty>
 	
 	<!-- NOTAS -->
 	<logic:notEmpty name="infoDegreeInfo" property="markAverage">	 	
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.marks" /></h2>
	  <ul>
	  	<li><strong><bean:message key="label.coordinator.degreeSite.mark.average" />:</strong> <bean:write name="infoDegreeInfo" property="markAverage" /></li>
	  	
			<logic:notEmpty name="infoDegreeInfo" property="markMin">  	
	    <li><strong><bean:message key="label.coordinator.degreeSite.markMin" />:</strong> <bean:write name="infoDegreeInfo" property="markMin" /></li>
			</logic:notEmpty>    
    
			<logic:notEmpty name="infoDegreeInfo" property="markMax">    
			<li><strong><bean:message key="label.coordinator.degreeSite.markMax" />:</strong> <bean:write name="infoDegreeInfo" property="markMax" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>

</logic:present>