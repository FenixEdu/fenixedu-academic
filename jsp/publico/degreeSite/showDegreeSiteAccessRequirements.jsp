<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeInfo">
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml"><bean:message key="label.school" /></a>
		&nbsp;&gt;&nbsp;<a href="http://www.ist.utl.pt/html/ensino/index.shtml"><bean:message key="label.education" /></a>
		<bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp; 
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>">
			<bean:write name="infoDegreeInfo" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message key="label.accessRequirements"/>
	</div>
	
	
	<!-- LANGUAGE 	
	<div class="version">
		<span class="px10">
			<bean:define id="imageFlag"><bean:message key="image.flag" /></bean:define>
			<bean:define id="imageFlagAlt"><bean:message key="image.flag.alt" /></bean:define>
			<html:link page="htp://www.ist.utl.pt"><bean:message key="label.language" /></html:link>
			<img src="<%= request.getContextPath() + imageFlag %>" alt="<%= imageFlagAlt %>" width="16" height="12" />
		</span>

	</div>-->	
	
	<!-- COURSE NAME -->
	<h1>
		<bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />
		<bean:message key="label.in" />
		<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
	</h1>
	
	<!-- ACCESS REQUIREMENTS SITE -->
	<h2 class="greytxt">
		<bean:define id="executionPeriod" name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request" />
		<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear" />
		<bean:message key="label.accessRequirements" />
		<bean:write name="executionYear" property="year" />
	</h2>

	<!-- TEST REQUIREMENTS -->
	<logic:notEmpty name="infoDegreeInfo" property="testIngression">
		<h2 class="arrow_bullet"><bean:message key="label.testRequirements" /></h2>  
		<p><bean:write name="infoDegreeInfo" property="testIngression" filter="false" /></p>
	</logic:notEmpty>
  
	<!-- AVAILABLE SPACES -->
 	<logic:notEmpty name="infoDegreeInfo" property="driftsInitial">
		<h2 class="arrow_bullet"><bean:message key="label.availableSpaces" /></h2>
		<ul>
			<li><strong><bean:message key="label.total" />:&nbsp;</strong><bean:write name="infoDegreeInfo" property="driftsInitial" /></li>
			<logic:notEmpty name="infoDegreeInfo" property="driftsFirst">
				<li><strong><bean:message key="label.filledPhase1" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="driftsFirst" /></li>
			</logic:notEmpty>
			<logic:notEmpty name="infoDegreeInfo" property="driftsSecond">    
				<li><strong><bean:message key="label.filledPhase2" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="driftsSecond" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>    	  	
	 
	<!-- MINIMUM SCORES-->
	<logic:notEmpty name="infoDegreeInfo" property="classifications">
		<h2 class="arrow_bullet"><bean:message key="label.minimumScores" /></h2>
	 	<bean:write name="infoDegreeInfo" property="classifications" filter="false" />
	</logic:notEmpty>
 	
 	<!-- ENTRANCE MARKS -->
 	<logic:notEmpty name="infoDegreeInfo" property="markAverage">
		<h2 class="arrow_bullet"><bean:message key="label.entranceMarks" /></h2>
		<ul>
			<li><strong><bean:message key="label.average" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="markAverage" /></li>
			<logic:notEmpty name="infoDegreeInfo" property="markMin">  	
				<li><strong><bean:message key="label.minimum" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="markMin" /></li>
			</logic:notEmpty>
			<logic:notEmpty name="infoDegreeInfo" property="markMax">    
				<li><strong><bean:message key="label.maximum" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="markMax" /></li>		
			</logic:notEmpty>
		</ul>			
	</logic:notEmpty>
	
	<logic:empty name="infoDegreeInfo" property="testIngression">
	<logic:empty name="infoDegreeInfo" property="classifications">
	<logic:empty name="infoDegreeInfo" property="driftsInitial">
	<logic:empty name="infoDegreeInfo" property="driftsFirst">
	<logic:empty name="infoDegreeInfo" property="driftsSecond">
	<logic:empty name="infoDegreeInfo" property="markAverage">
	<logic:empty name="infoDegreeInfo" property="markMin">
	<logic:empty name="infoDegreeInfo" property="markMax">
		<p><span class="error"><bean:message key="error.public.DegreeInfoNotPresent" /></span></p>
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>	
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty> 

	<div class="clear"></div>
	<p><span class="px10"><bean:message key="label.responsability.information.degree" /></span></p>				 

</logic:present>
