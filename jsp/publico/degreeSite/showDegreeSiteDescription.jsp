<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:notPresent name="infoDegreeInfo">
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> >
		<html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>"> Ensino Mestrados</html:link>
		&nbsp;/&nbsp;
		<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>"> Ensino Licenciaturas</html:link>				
	</div>
</logic:notPresent>

<logic:present name="infoDegreeInfo">
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> >
		<bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso" />	
		<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Mestrados</html:link>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Licenciaturas</html:link>		
		</logic:equal>	
		&gt;&nbsp;<bean:write name="infoDegreeInfo" property="infoDegree.sigla" />
	</div>
				
	<!-- PÁGINA EM INGLÊS -->
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + pageContext.findAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")%>">english version</html:link> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" />
		</span>
	</div> 
		  
	<!-- NOME DO CURSO -->
	<h2 class="degree"><bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" /></h2>
		<h1><bean:write name="infoDegreeInfo" property="infoDegree.nome" /></h1>
			  
	<!-- CAMPUS BEGIN-->
	<logic:present name="infoExecutionDegrees" >
	  	<bean:size id="campusSize" name="infoExecutionDegrees" />
	  	<h2 class="blue">
	  	
		<bean:define id="campus" value="" />
	  	<logic:iterate id="executionDegree" name="infoExecutionDegrees" indexId="indexCampus" >
			<bean:define id="campusName" name="executionDegree" property="infoCampus.name"/>
	  		<logic:notMatch name="campus" value="<%= campusName.toString()%>">
				<bean:write name="campusName" />&nbsp;&nbsp;&nbsp;&nbsp;			
			  	
			  	<bean:define id="campus" value="<%= campus.toString().concat(campusName.toString()) %>" />	
	  		</logic:notMatch>
		</logic:iterate>
		</h2>
	</logic:present>
	
	<!-- CAMPUS END-->
	<br />
	<!-- COORDINATOR BEGIN-->
 	<logic:present name="infoExecutionDegrees" >
		<strong><bean:message key="label.coordinators" /></strong>

	 	<br />
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
		
						<a href=" <%= homepage %>">
							<bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome" />						
						</a>
					</logic:notEmpty>		
								
					<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
						<logic:notEmpty name="infoCoordinator" property="infoTeacher.infoPerson.email">
							<bean:define id="email" name="infoCoordinator" property="infoTeacher.infoPerson.email" />
									
							<a href="mailto: <%= email %>">
								<bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome" />						
							</a>											
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
	<!-- COORDINATOR END-->
	<br />
	<br />
	<bean:message key="label.information.responsability.description.degree" />			 
	<br />
	<br />
	<div class="degree_imageplacer">
		<!-- IMAGEM REFERENTE À LICENCIATURA  width="250" height="150"-->
	</div>
			 
	<div class="col_left">			 	
		<logic:notEmpty name="infoDegreeInfo" property="description" >			 	
			<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.description" /> </h2>
			<p><!-- BREVE DESCRIÇAO DA LICENCIATURA--><bean:write name="infoDegreeInfo" property="description" filter="false" /></p>
		</logic:notEmpty>
				 
  		<logic:notEmpty name="infoDegreeInfo" property="objectives" >
			<h2> <img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.objectives" /></h2>
		 	<p><!-- OBJECTIVOS --><bean:write name="infoDegreeInfo" property="objectives" filter="false" /></p>
		</logic:notEmpty>
	</div>
			  
	<div class="col_right">
		<table class="box" cellspacing="0">
			<logic:notEmpty name="infoDegreeInfo" property="additionalInfo" >		
				<tr>
					<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
				</tr>						
				<tr>
					<td class="box_cell"><p><!-- TEXTO - INFORMACOES ADICIONAIS! --><bean:write name="infoDegreeInfo" property="additionalInfo" filter="false" /></p></td>						
				</tr>
			</logic:notEmpty>
						
			<logic:notEmpty name="infoDegreeInfo" property="links" >	
				<tr>
					<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td class="box_cell"><p><!-- TEXTO - LINKS! --><bean:write name="infoDegreeInfo" property="links" filter="false" /></p></td>	
				</tr>
			</logic:notEmpty>
		</table>
	</div>
				
	<logic:notEmpty name="infoDegreeInfo" property="professionalExits" >
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.professionalExits" /></h2>
		<p><!-- TEXTO - SAÍDAS PROFISSIONAIS--><bean:write name="infoDegreeInfo" property="professionalExits" filter="false" /></p>  
	</logic:notEmpty>
			  
	<logic:notEmpty name="infoDegreeInfo" property="history" >
		<div class="col_left">
			<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.history" /></h2>
			<p><!-- TEXTO - HISTORIAL DA LICENCIATURA --><bean:write name="infoDegreeInfo" property="history" filter="false" /></p>
		</div>
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
</logic:present>
