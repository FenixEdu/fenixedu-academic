<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.TipoCurso" %>

<html:html locale="true"> 
<p><span class="error"><html:errors/></span></p>

<logic:notPresent name="infoDegreeInfo">
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>ensino/index.shtml</bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="label.education" /></a>
	</div>
</logic:notPresent>

<logic:present name="infoDegreeInfo">
	<bean:define id="infoDegreeInfo" name="infoDegreeInfo"/>
	<div class="breadcumbs">
		<bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso" />	
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>ensino/index.shtml</bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="label.education" /></a>
		&nbsp;&gt;&nbsp;
		<bean:write name="infoDegreeInfo" property="infoDegree.sigla" />
	</div>

	<!-- COURSE NAME -->
	<h1>
		<bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />
		<bean:message key="label.in" />
		<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
	</h1>

	<logic:present name="infoExecutionDegrees" >
		<!-- CAMPUS -->
		<bean:size id="campusSize" name="infoExecutionDegrees" />
	  	<h2 class="greytxt">
	  		<bean:define id="campus" value="" />
	  		<logic:iterate id="executionDegree" name="infoExecutionDegrees" indexId="indexCampus" >
				<bean:define id="campusName" name="executionDegree" property="infoCampus.name"/>
	  			<logic:notMatch name="campus" value="<%= campusName.toString()%>">
					<bean:write name="campusName" />		
			  		<bean:define id="campus" value="<%= campus.toString().concat(campusName.toString()) %>" />	
	  			</logic:notMatch>
			</logic:iterate>
		</h2>
	</logic:present>
	
 	<logic:present name="infoExecutionDegrees" >
		<!-- COORDINATORS -->
		<p><strong><bean:message key="label.coordinators" /></strong></p>
		<bean:size id="executionDegreesSize" name="infoExecutionDegrees" />
		<bean:define id="coordinators" value="" />
		<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees" indexId="executionDegreesSize" >
			<logic:iterate id="infoCoordinator" name="infoExecutionDegree" property="coordinatorsList">
  				<logic:equal name="infoCoordinator" property="responsible" value="true">
					<bean:define id="coordinatorName" name="infoCoordinator" property="infoTeacher.infoPerson.nome" />
					<logic:notMatch name="coordinators" value="<%= coordinatorName.toString()%>">
						<bean:message key="label.title.coordinator" />&nbsp; 
								
						<logic:notEmpty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
							<bean:define id="homepage" name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb" />
							<a href=" <%= homepage %>"><bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome" /></a>
						</logic:notEmpty>		
								
						<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
							<logic:notEmpty name="infoCoordinator" property="infoTeacher.infoPerson.email">
								<bean:define id="email" name="infoCoordinator" property="infoTeacher.infoPerson.email" />	
									<a href="mailto: <%= email %>"><bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome" /></a>											
							</logic:notEmpty>						
						</logic:empty>		
								
						<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
							<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.email">
								<bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome" />											
							</logic:empty>						
						</logic:empty>	
								
						<logic:lessThan name="executionDegreesSize" value="executionDegreesSize" >
							<br />
						</logic:lessThan>
					
						<bean:define id="coordinators" value="<%= coordinators.toString().concat(coordinatorName.toString()) %>" />
					</logic:notMatch>
				</logic:equal>
			 </logic:iterate>
		</logic:iterate>
	</logic:present>			

<!--
	<div class="degree_imageplacer">
		IMAGEM REFERENTE � LICENCIATURA  width="250" height="150"
	</div>
-->

	<logic:notEmpty name="infoDegreeInfo" property="description" >			 	
		<!-- OVERVIEW -->
		<h2 class="arrow_bullet"><bean:message key="label.overview" /></h2>
		<p><bean:write name="infoDegreeInfo" property="description" filter="false" /></p>
	</logic:notEmpty>
	
	<logic:notEmpty name="infoDegreeInfo" property="objectives" >
		<!-- OBJECTIVES -->
		<h2 class="arrow_bullet"><bean:message key="label.objectives" /></h2>
	 	<p><bean:write name="infoDegreeInfo" property="objectives" filter="false" /></p>
	</logic:notEmpty>
				  
	<div class="col_right">
		<logic:notEmpty name="infoDegreeInfo" property="additionalInfo" >	
			<!-- ADDITIONAL INFO -->	
			<table class="box" cellspacing="0">
				<tr>
					<td class="box_header"><strong><bean:message key="label.additionalInfo" /></strong></td>
				</tr>						
				<tr>
					<td class="box_cell"><p><bean:write name="infoDegreeInfo" property="additionalInfo" filter="false" /></p></td>						
				</tr>
			<logic:empty name="infoDegreeInfo" property="links" >
				</table>
			</logic:empty>
		</logic:notEmpty>
		
		<logic:notEmpty name="infoDegreeInfo" property="links" >
			<!-- LINKS -->	
			<logic:empty name="infoDegreeInfo" property="additionalInfo" >	
				<table class="box" cellspacing="0">	
			</logic:empty>
				<tr>
					<td class="box_header"><strong><bean:message key="label.links"/></strong></td>
				</tr>
				<tr>
					<td class="box_cell"><p><bean:write name="infoDegreeInfo" property="links" filter="false" /></p></td>	
				</tr>
			</table>
		</logic:notEmpty>
	</div>
	
	<logic:notEmpty name="infoDegreeInfo" property="professionalExits" >
		<!-- PROFESSIONAL EXITS -->
		<h2 class="arrow_bullet"><bean:message key="label.professionalExits" /></h2>
		<p><bean:write name="infoDegreeInfo" property="professionalExits" filter="false" /></p>  
	</logic:notEmpty>

	<logic:notEmpty name="infoDegreeInfo" property="history" >
		<!-- HISTORY -->
		<h2 class="arrow_bullet"><bean:message key="label.history" /></h2>
		<p><bean:write name="infoDegreeInfo" property="history" filter="false" /></p>
	</logic:notEmpty>

	<logic:empty name="infoDegreeInfo" property="description">
	<logic:empty name="infoDegreeInfo" property="objectives">
	<logic:empty name="infoDegreeInfo" property="additionalInfo">
	<logic:empty name="infoDegreeInfo" property="links">
	<logic:empty name="infoDegreeInfo" property="professionalExits">
	<logic:empty name="infoDegreeInfo" property="history">
		<p><span class="error"><bean:message key="error.public.DegreeInfoNotPresent" /></span></p>
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>	
	</logic:empty>	
	</logic:empty>

	<div class="clear"></div>
	<p><span class="px10"><bean:message key="label.responsability.information.degree" /></span></p>				 
	
</logic:present>
</html:html>