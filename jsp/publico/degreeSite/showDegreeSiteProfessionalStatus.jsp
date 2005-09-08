<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<logic:present name="infoDegreeInfo">
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> 
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp; <a href="<%= institutionUrlTeaching %>"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education" /></a>
		<bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp; 
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")  %>">
			<bean:write name="infoDegreeInfo" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message key="public.degree.information.label.professionalStatus"  bundle="PUBLIC_DEGREE_INFORMATION" />
	</div>

<p><span class="error"><html:errors/></span></p>
	
	<!-- COURSE NAME -->
	<h1>
	    <bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso.name"/>
		<logic:equal name="degreeType" value="DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
		</logic:equal>    
		<logic:equal name="degreeType" value="MASTER_DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
		</logic:equal>   

		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
		<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
	</h1>
	
	<!-- PROFESSIONAL STATUS SITE -->
	<h2 class="greytxt">
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.professionalStatus" />
	</h2>

	<!-- QUALIFICATION LEVEL -->
	<logic:notEmpty name="infoDegreeInfo" property="qualificationLevel">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.qualificationLevel" /></h2>  
		<p><bean:write name="infoDegreeInfo" property="qualificationLevel" filter="false" /></p>
	</logic:notEmpty>
	
	<!-- RECOGNITIONS -->
	<logic:notEmpty name="infoDegreeInfo" property="recognitions">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.recognitions" /></h2>  
		<p><bean:write name="infoDegreeInfo" property="recognitions" filter="false" /></p>
	</logic:notEmpty>
  
	<div class="clear"></div>
	<p><span class="px10"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.responsability.information.degree" /></span></p>				 

</logic:present>
