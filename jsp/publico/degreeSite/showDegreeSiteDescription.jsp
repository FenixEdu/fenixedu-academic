<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<span class="error"><html:errors/></span>

<logic:present name="infoDegreeInfo">

				<div class="breadcumbs"><a href="/index.shtml">IST</a> > <a href="/html/ensino/ensino.shtml">Ensino</a> &gt; 
					<bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" />&nbsp<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
				</div>
				
				<!-- PÁGINA EM INGLÊS -->
				<div class="version"><span class="px10"><a href="/html/en/teaching.shtml">english version</a> <img src="/img/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div>
			 	<div class="clear"></div> 
			  
			  <!-- NOME DO CURSO -->
			  <h2 class="degree"><bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" /></h2>
			  <h1><bean:write name="infoDegreeInfo" property="infoDegree.nome" /></h1>
			  
			  <!-- CAMPUS BEGIN-->
			  <logic:present name="infoExecutionDegrees" >
			  	<bean:size id="campusSize" name="infoExecutionDegrees" />
			  	<h2 class="blue">
				  	<logic:iterate id="executionDegree" name="infoExecutionDegrees" indexId="indexCampus" >
						  <bean:write name="executionDegree" property="infoCampus.name" />
							
							<logic:greaterThan name="campusSize" value="1">
							  <logic:lessThan name="indexCampus" value="<%= String.valueOf(campusSize.intValue() - 2) %>" >
							  	&nbsp;,&nbsp;
							  </logic:lessThan>						  
							  <logic:equal name="indexCampus" value="<%= String.valueOf(campusSize.intValue() - 1) %>" >
							  	&nbsp;<bean:message key="label.and" />&nbsp;
							  </logic:equal>
						  </logic:greaterThan>
					  </logic:iterate>
				  </h2>
			  </logic:present>
			  <!-- CAMPUS END-->
			  
			  <br />
				
				<!-- COORDINATOR BEGIN-->
 		    <logic:present name="infoExecutionDegrees" >
	 		    <strong><bean:message key="label.coordinators" /></strong>
	 		    <br />
			  	<bean:size id="campusSize" name="infoExecutionDegrees" />

			  	<logic:iterate id="executionDegree" name="infoExecutionDegrees" indexId="indexCampus" >
				  	<bean:message key="label.title.coordinator" />&nbsp; 
					
						<logic:present name="executionDegree" property="infoCoordinator.infoPerson.enderecoWeb">
							<bean:define id="homepage" name="executionDegree" property="infoCoordinator.infoPerson.enderecoWeb" />						

							<a href=" <%= homepage %>">
							<bean:write name="executionDegree" property="infoCoordinator.infoPerson.nome" />						
							</a>
						</logic:present>		
						
						<logic:notPresent name="executionDegree" property="infoCoordinator.infoPerson.enderecoWeb">
							<logic:present name="executionDegree" property="infoCoordinator.infoPerson.email">
								<bean:define id="email" name="executionDegree" property="infoCoordinator.infoPerson.email" />
							
								<a href="mailto: <%= email %>">
								<bean:write name="executionDegree" property="infoCoordinator.infoPerson.nome" />						
								</a>											
							</logic:present>						
						</logic:notPresent>		
						
						<logic:notPresent name="executionDegree" property="infoCoordinator.infoPerson.enderecoWeb">
							<logic:notPresent name="executionDegree" property="infoCoordinator.infoPerson.email">
								<bean:write name="executionDegree" property="infoCoordinator.infoPerson.nome" />											
							</logic:notPresent>						
						</logic:notPresent>	
						
						<logic:lessThan name="indexCampus" value="campusSize" >
					  	<br />
					  </logic:lessThan>
				  </logic:iterate>
			  </logic:present>			
			  <!-- COORDINATOR END-->
				<br />
			 	<br />
			 
			 	<div class="degree_imageplacer">
			 		<!-- IMAGEM REFERENTE À LICENCIATURA  width="250" height="150"-->
			 	</div>
			 
			 	<div class="col_left">			 	
			 		<logic:present name="infoDegreeInfo" property="description" >			 	
				 	<h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.description" /> </h2>
				 	<p><!-- BREVE DESCRIÇAO DA LICENCIATURA--><bean:write name="infoDegreeInfo" property="description" filter="false" /></p>
				 	</logic:present>
				 
  			  <logic:present name="infoDegreeInfo" property="objectives" >
				 	<h2> <img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.objectives" /></h2>
		 	   	<p><!-- OBJECTIVOS --><bean:write name="infoDegreeInfo" property="objectives" filter="false" /></p>
					</logic:present>
				</div>
			  
			  <div class="col_right">
					<table class="box" cellspacing="0">
						<logic:present name="infoDegreeInfo" property="additionalInfo" >		
						<tr>
							<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
						</tr>						
						<tr>
							<td class="box_cell"><p><!-- TEXTO - INFORMACOES ADICIONAIS! --><bean:write name="infoDegreeInfo" property="additionalInfo" filter="false" /></p></td>						
						</tr>
						</logic:present>
						
						<logic:present name="infoDegreeInfo" property="links" >	
						<tr>
							<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
						</tr>
						<tr>
							<td class="box_cell"><p><!-- TEXTO - LINKS! --><bean:write name="infoDegreeInfo" property="links" filter="false" /></p></td>	
						</tr>
						</logic:present>
					</table>
				</div>
				
				<logic:present name="infoDegreeInfo" property="professionalExits" >
			  <h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.professionalExits" /></h2>
			  <p><!-- TEXTO - SAÍDAS PROFISSIONAIS--><bean:write name="infoDegreeInfo" property="professionalExits" filter="false" /></p>  
			  </logic:present>
			  
			  <logic:present name="infoDegreeInfo" property="history" >
			  <div class="col_left">
			  	<h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.history" /></h2>
				  <p><!-- TEXTO - HISTORIAL DA LICENCIATURA --><bean:write name="infoDegreeInfo" property="history" filter="false" /></p>
				</div>
				</logic:present>

</logic:present>