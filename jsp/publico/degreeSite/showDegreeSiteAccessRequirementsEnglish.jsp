<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeInfo">

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
		<bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso" />	
		<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Mestrados</html:link>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Licenciaturas</html:link>		
		</logic:equal>
		&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>">
			<bean:write name="infoDegreeInfo" property="infoDegree.sigla" />
		</html:link>
		&gt;&nbsp;<bean:message key="label.accessRequirements.en"/>
	</div>
	
	<!-- P�GINA EM PORTUGU�S -->
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
				<bean:message key="label.version.portuguese" />
			</html:link>
			<img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: Portuguese version!" width="16" height="12" />
		</span>
	</div> 
	<div class="clear"></div> 
	
	<h1><bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeInfo" property="infoDegree.nome" /></h1>
	<h2><span class="greytxt"><bean:message key="label.accessRequirements.en"/></span></h2>

	<!-- NOME(S) DA PROVA(S) DE INGRESSO -->
  <logic:notEmpty name="infoDegreeInfo" property="testIngressionEn">
  	<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.testIngression.en" /></h2>  
	<p><bean:write name="infoDegreeInfo" property="testIngressionEn" filter="false" /></p>
  </logic:notEmpty>
  
  <!-- VAGAS -->
 	<logic:notEmpty name="infoDegreeInfo" property="driftsInitial">
	  <h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.drifts.en" /></h2>
	  <ul>
	  	<li><strong><bean:message key="label.coordinator.degreeSite.driftsInitial.en" />:</strong> <bean:write name="infoDegreeInfo" property="driftsInitial" /></li>
	  	
			<logic:notEmpty name="infoDegreeInfo" property="driftsFirst">  	
	    <li><strong><bean:message key="label.coordinator.degreeSite.driftsFirst.en" />:</strong> <bean:write name="infoDegreeInfo" property="driftsFirst" /></li>
			</logic:notEmpty>    
    
			<logic:notEmpty name="infoDegreeInfo" property="driftsSecond">    
			<li><strong><bean:message key="label.coordinator.degreeSite.driftsSecond.en" />:</strong> <bean:write name="infoDegreeInfo" property="driftsSecond" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>    	  	
	  	
  <!-- CLASSIFICA��ES-->
  <logic:notEmpty name="infoDegreeInfo" property="classificationsEn">
	  <h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.classifications.en" /></h2>
	 	<bean:write name="infoDegreeInfo" property="classificationsEn" filter="false" />
	</logic:notEmpty>
 	
 	<!-- NOTAS -->
 	<logic:notEmpty name="infoDegreeInfo" property="markAverage">	 	
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.marks.en" /></h2>
	  <ul>
	  	<li><strong><bean:message key="label.coordinator.degreeSite.mark.average.en" />:</strong> <bean:write name="infoDegreeInfo" property="markAverage" /></li>
	  	
			<logic:notEmpty name="infoDegreeInfo" property="markMin">  	
	    <li><strong><bean:message key="label.coordinator.degreeSite.markMin.en" />:</strong> <bean:write name="infoDegreeInfo" property="markMin" /></li>
			</logic:notEmpty>    
    
			<logic:notEmpty name="infoDegreeInfo" property="markMax">    
			<li><strong><bean:message key="label.coordinator.degreeSite.markMax.en" />:</strong> <bean:write name="infoDegreeInfo" property="markMax" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>

	<logic:empty name="infoDegreeInfo" property="testIngressionEn">
	<logic:empty name="infoDegreeInfo" property="classificationsEn">
	<logic:empty name="infoDegreeInfo" property="driftsInitial">
	<logic:empty name="infoDegreeInfo" property="driftsFirst">
	<logic:empty name="infoDegreeInfo" property="driftsSecond">
	<logic:empty name="infoDegreeInfo" property="markAverage">
	<logic:empty name="infoDegreeInfo" property="markMin">
	<logic:empty name="infoDegreeInfo" property="markMax">
		<p><span class="error"><bean:message key="error.public.DegreeInfoNotPresent.en" /></span></p>
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>	
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty> 
	<br /> 
	<br /> 
	<br /> 
	<bean:message key="label.information.responsability.information.degree.en" />			 
	<br />
	<br />
</logic:present>

