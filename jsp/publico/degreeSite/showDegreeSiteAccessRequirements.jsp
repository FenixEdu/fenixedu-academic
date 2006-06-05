<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="institutionUrl" type="java.lang.String">
	<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
</bean:define>

<div class="breadcumbs">
	<a href="<%= institutionUrl %>">
		<bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/>
	</a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String">
		<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
		<bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/>
	</bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>">
		<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/>
	</a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="degree" property="sigla"/>
		</html:link>
	</logic:present>
	&nbsp;&gt;&nbsp;
	<bean:message key="public.degree.information.label.accessRequirements"  bundle="PUBLIC_DEGREE_INFORMATION" />
</div>

<!-- COURSE NAME -->
<h1>
	<logic:equal name="degree" property="bolonhaDegree" value="true">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
	</logic:equal>
	<logic:equal name="degree" property="bolonhaDegree" value="false">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
	<logic:present name="inEnglish">
		<logic:equal name="inEnglish" value="false">
			<bean:write name="degree" property="nome"/>
		</logic:equal>
		<logic:equal name="inEnglish" value="true">
			<bean:write name="degree" property="nameEn"/>
		</logic:equal>
	</logic:present>
</h1>

<h2 class="greytxt">
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.accessRequirements" />
	<bean:write name="executionYear" property="year" />
</h2>

<logic:notPresent name="infoDegreeInfo">
	<p><em><bean:message bundle="DEFAULT" key="error.public.DegreeInfoNotPresent"/></em></p>
</logic:notPresent>
<logic:present name="infoDegreeInfo">	
	<!-- TEST REQUIREMENTS -->
	<logic:notEmpty name="infoDegreeInfo" property="testIngression">
		<h2 class="arrow_bullet">
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.testRequirements" />
		</h2>  
		<p><bean:write name="infoDegreeInfo" property="testIngression" filter="false" /></p>
	</logic:notEmpty>
  
	<!-- AVAILABLE SPACES -->
 	<logic:notEmpty name="infoDegreeInfo" property="driftsInitial">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.availableSpaces" /></h2>
		<ul>
			<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.total" />:&nbsp;</strong><bean:write name="infoDegreeInfo" property="driftsInitial" /></li>
			<logic:notEmpty name="infoDegreeInfo" property="driftsFirst">
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.filledPhase1" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="driftsFirst" /></li>
			</logic:notEmpty>
			<logic:notEmpty name="infoDegreeInfo" property="driftsSecond">    
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.filledPhase2" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="driftsSecond" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>    	  	
	 
	<!-- MINIMUM SCORES-->
	<logic:notEmpty name="infoDegreeInfo" property="classifications">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.minimumScores" /></h2>
	 	<bean:write name="infoDegreeInfo" property="classifications" filter="false" />
	</logic:notEmpty>
 	
 	<!-- ENTRANCE MARKS -->
 	<logic:notEmpty name="infoDegreeInfo" property="markAverage">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.entranceMarks" /></h2>
		<ul>
			<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.average" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="markAverage" /></li>
			<logic:notEmpty name="infoDegreeInfo" property="markMin">  	
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.minimum" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="markMin" /></li>
			</logic:notEmpty>
			<logic:notEmpty name="infoDegreeInfo" property="markMax">    
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.maximum" />:</strong>&nbsp;<bean:write name="infoDegreeInfo" property="markMax" /></li>		
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
		<p><i><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="not.available" /></i></p>
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>	
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty> 
	
</logic:present>

<p style="margin-top: 2em;">
	<span class="px10">
		<i>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.responsability.information.degree" />
		</i>
	</span>
</p>
