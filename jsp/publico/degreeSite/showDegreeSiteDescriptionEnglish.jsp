<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:notPresent name="infoDegreeInfo">
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> >
		<html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>"> Master Degrees</html:link>
		&nbsp;/&nbsp;
		<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>"> Master Degrees</html:link>				
	</div>
</logic:notPresent>

<logic:present name="infoDegreeInfo">
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml">IST</a> >
		<bean:define id="degreeType" name="infoDegreeInfo" property="infoDegree.tipoCurso" />	
		<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Master Degrees</html:link>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Master Degrees</html:link>		
		</logic:equal>	
		&gt;&nbsp;<bean:write name="infoDegreeInfo" property="infoDegree.sigla" />
	</div>
				
	<!-- PÁGINA EM PORTUGUÊS -->
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + pageContext.findAttribute("degreeID").toString()  + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>">portuguese version</html:link> <img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: Portuguese version!" width="16" height="12" />
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
				<%--<logic:greaterThan name="campusSize" value="1">
					<logic:lessThan name="indexCampus" value="<%= String.valueOf(campusSize.intValue() - 1) %>" >
						&nbsp;&nbsp;
					</logic:lessThan>						  
			  	</logic:greaterThan>--%>
			  	
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

		<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees" indexId="executionDegreesSize" >
			<logic:iterate id="infoCoordinator" name="infoExecutionDegree" property="coordinatorsList">
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
			</logic:iterate>
		</logic:iterate>
	</logic:present>			
	<!-- COORDINATOR END-->
	<br />
	<br />
			 
	<div class="degree_imageplacer">
		<!-- IMAGEM REFERENTE À LICENCIATURA  width="250" height="150"-->
	</div>
			 
	<div class="col_left">			 	
		<logic:notEmpty name="infoDegreeInfo" property="descriptionEn" >			 	
			<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.description.en" /> </h2>
			<p><!-- BREVE DESCRIÇAO DA LICENCIATURA--><bean:write name="infoDegreeInfo" property="descriptionEn" filter="false" /></p>
		</logic:notEmpty>
				 
  		<logic:notEmpty name="infoDegreeInfo" property="objectivesEn" >
			<h2> <img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.objectives.en" /></h2>
		 	   	<p><!-- OBJECTIVOS --><bean:write name="infoDegreeInfo" property="objectivesEn" filter="false" /></p>
		</logic:notEmpty>
	</div>
			  
	<div class="col_right">
		<table class="box" cellspacing="0">
			<logic:notEmpty name="infoDegreeInfo" property="additionalInfoEn" >		
				<tr>
					<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.additionalInfo.en"/></strong></td>
				</tr>						
				<tr>
					<td class="box_cell"><p><!-- TEXTO - INFORMACOES ADICIONAIS! --><bean:write name="infoDegreeInfo" property="additionalInfoEn" filter="false" /></p></td>						
				</tr>
			</logic:notEmpty>
						
			<logic:notEmpty name="infoDegreeInfo" property="linksEn" >	
				<tr>
					<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td class="box_cell"><p><!-- TEXTO - LINKS! --><bean:write name="infoDegreeInfo" property="linksEn" filter="false" /></p></td>	
				</tr>
			</logic:notEmpty>
		</table>
	</div>
				
	<logic:notEmpty name="infoDegreeInfo" property="professionalExitsEn" >
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.professionalExits.en" /></h2>
		<p><!-- TEXTO - SAÍDAS PROFISSIONAIS--><bean:write name="infoDegreeInfo" property="professionalExitsEn" filter="false" /></p>  
	</logic:notEmpty>
			  
	<logic:notEmpty name="infoDegreeInfo" property="historyEn" >
		<div class="col_left">
			<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.coordinator.degreeSite.history.en" /></h2>
			<p><!-- TEXTO - HISTORIAL DA LICENCIATURA --><bean:write name="infoDegreeInfo" property="historyEn" filter="false" /></p>
		</div>
	</logic:notEmpty>
<logic:empty name="infoDegreeInfo" property="descriptionEn">
<logic:empty name="infoDegreeInfo" property="objectivesEn">
<logic:empty name="infoDegreeInfo" property="additionalInfoEn">
<logic:empty name="infoDegreeInfo" property="linksEn">
<logic:empty name="infoDegreeInfo" property="professionalExitsEn">
<logic:empty name="infoDegreeInfo" property="historyEn">
	<p><span class="error"><bean:message key="error.public.DegreeInfoNotPresent.en" /></span></p>
</logic:empty>
</logic:empty>
</logic:empty>	
</logic:empty>	
</logic:empty>	
</logic:empty> 
</logic:present>

