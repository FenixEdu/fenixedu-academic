<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %></bean:define>

<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
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
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
</h1>

<h2 class="greytxt">
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.accessRequirements" />
	<bean:write name="executionYear" property="year" />
</h2>

<logic:notPresent name="degreeInfo">
	<p><em><bean:message bundle="DEFAULT" key="error.public.DegreeInfoNotPresent"/></em></p>
</logic:notPresent>
<logic:present name="degreeInfo">	
	<!-- TEST REQUIREMENTS -->
	<logic:notEmpty name="degreeInfo" property="testIngression">
		<h2 class="arrow_bullet">
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.testRequirements" />
		</h2>  
		<p><bean:write name="degreeInfo" property="testIngression.content" filter="false" /></p>
	</logic:notEmpty>
  
	<!-- CLASSIFICATIONS -->
	<logic:notEmpty name="degreeInfo" property="classifications">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.minimumScores" /></h2>
	 	<bean:write name="degreeInfo" property="classifications.content" filter="false" />
	</logic:notEmpty>
 	
	<!-- ACCESS REQUISITES -->
	<logic:notEmpty name="degreeInfo" property="accessRequisites">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.accessRequisites" /></h2>
	 	<bean:write name="degreeInfo" property="accessRequisites.content" filter="false" />
	</logic:notEmpty>

	<!-- CANDIDACY DOCUMENTS -->
	<logic:notEmpty name="degreeInfo" property="candidacyDocuments">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.candidacyDocuments" /></h2>
	 	<bean:write name="degreeInfo" property="candidacyDocuments.content" filter="false" />
	</logic:notEmpty>
 	
	<!-- DRIFTS -->
 	<logic:notEmpty name="degreeInfo" property="driftsInitial">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.availableSpaces" /></h2>
		<ul>
			<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.total" />:&nbsp;</strong><bean:write name="degreeInfo" property="driftsInitial" /></li>
			<logic:notEmpty name="degreeInfo" property="driftsFirst">
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.filledPhase1" />:</strong>&nbsp;<bean:write name="degreeInfo" property="driftsFirst" /></li>
			</logic:notEmpty>
			<logic:notEmpty name="degreeInfo" property="driftsSecond">    
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.filledPhase2" />:</strong>&nbsp;<bean:write name="degreeInfo" property="driftsSecond" /></li>		
			</logic:notEmpty>
	  </ul>			
	</logic:notEmpty>    	  	
	 
 	<!-- MARKS -->
 	<logic:notEmpty name="degreeInfo" property="markAverage">
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.entranceMarks" /></h2>
		<ul>
			<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.average" />:</strong>&nbsp;<bean:write name="degreeInfo" property="markAverage" /></li>
			<logic:notEmpty name="degreeInfo" property="markMin">  	
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.minimum" />:</strong>&nbsp;<bean:write name="degreeInfo" property="markMin" /></li>
			</logic:notEmpty>
			<logic:notEmpty name="degreeInfo" property="markMax">    
				<li><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.maximum" />:</strong>&nbsp;<bean:write name="degreeInfo" property="markMax" /></li>		
			</logic:notEmpty>
		</ul>			
	</logic:notEmpty>
	
	<logic:empty name="degreeInfo" property="testIngression">
	<logic:empty name="degreeInfo" property="classifications">
	<logic:empty name="degreeInfo" property="accessRequisites">
	<logic:empty name="degreeInfo" property="candidacyDocuments">
	<logic:empty name="degreeInfo" property="driftsInitial">
	<logic:empty name="degreeInfo" property="driftsFirst">
	<logic:empty name="degreeInfo" property="driftsSecond">
	<logic:empty name="degreeInfo" property="markAverage">
	<logic:empty name="degreeInfo" property="markMin">
	<logic:empty name="degreeInfo" property="markMax">
		<p><i><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="not.available" /></i></p>
	</logic:empty>
	</logic:empty>
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
