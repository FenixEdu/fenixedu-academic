<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<span class="error"><html:errors/></span>

<logic:present name="infoDegreeInfo">

	<table id="bigtable" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>	
			<td width="100%" colspan="3" id="main">
				<div class="breadcumbs"><a href="/index.shtml">IST</a> > <a href="/html/ensino/ensino.shtml">Ensino</a> &gt; 
					<bean:write name="infoDegreeInfo" property="infoDegree.nome" />
				</div>
				
				<!-- PÁGINA EM INGLÊS -->
				<div class="version"><span class="px10"><a href="/html/en/teaching.shtml">english version</a> <img src="/img/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div>
			 	<div class="clear"></div> 
			  
			  <h2 class="degree"><bean:write name="infoDegreeInfo" property="infoDegree.tipoCurso" /></h2>
			  <h1><bean:write name="infoDegreeInfo" property="infoDegree.nome" /></h1>
			  
			  <logic:present name="infoDegreeInfo" property="infoDegree.infoCampus" >
			  	<bean:size id="campusSize" name="infoDegreeInfo" property="infoDegree.infoCampus" />
			  	<logic:iterate id="campus" name="infoDegreeInfo" property="infoDegree.infoCampus" indexId="indexCampus" >
					  <h2 class="blue"><bean:write name="campus" property="name" /></h2>
					  <logic:lessThan name="indexCampus" value="campusSize" >
					  	&nbsp;<bean:message key="label.and" />
					  </logic:lessThan>
				  </logic:iterate>
			  </logic:present>
			  
			  <br />
				
				<strong><bean:message key="label.coordinators" />:</strong>
				<bean:message key="label.title.coordinator" /> <!-- <a href="">NOME DO PROFESSOR</a> -->
				<br />
			 	<br />
			 
			 	<div class="degree_imageplacer">
			 		<!-- IMAGEM REFERENTE À LICENCIATURA  width="250" height="150"-->
			 	</div>
			 
			 	<div class="col_left">			 	
			 		<logic:present name="infoDegreeInfo" property="description" >			 	
				 	<h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.description" /> </h2>
				 	<p><!-- BREVE DESCRIÇAO DA LICENCIATURA--><bean:write name="infoDegreeInfo" property="description" /></p>
				 	</logic:present>
				 
  			  <logic:present name="infoDegreeInfo" property="objectives" >
				 	<h2> <img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.objectives"/></h2>
		 	   	<p><!-- OBJECTIVOS --><bean:write name="infoDegreeInfo" property="objectives" /></p>
					</logic:present>
				</div>
			  
			  <div class="col_right">
					<table class="box" cellspacing="0">
						<logic:present name="infoDegreeInfo" property="additionalInfo" >		
						<tr>
							<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
						</tr>						
						<tr>
							<td class="box_cell"><p><!-- TEXTO - INFORMACOES ADICIONAIS! --><bean:write name="infoDegreeInfo" property="additionalInfo" /></p></td>						
						</tr>
						</logic:present>
						
						<logic:present name="infoDegreeInfo" property="links" >	
						<tr>
							<td class="box_header"><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
						</tr>
						<tr>
							<td class="box_cell"><p><!-- TEXTO - LINKS! --><bean:write name="infoDegreeInfo" property="links" /></p></td>	
						</tr>
						</logic:present>
					</table>
				</div>
				
				<logic:present name="infoDegreeInfo" property="professionalExits" >
			  <h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.professionalExits"/></h2>
			  <p><!-- TEXTO - SAÍDAS PROFISSIONAIS--><bean:write name="infoDegreeInfo" property="professionalExits" /></p>  
			  </logic:present>
			  
			  <logic:present name="infoDegreeInfo" property="history" >
			  <div class="col_left">
			  	<h2><img alt="" height="12" src="/img/icon_arrow.gif" width="12" /><bean:message key="label.coordinator.degreeSite.history"/></h2>
				  <p><!-- TEXTO - HISTORIAL DA LICENCIATURA --><bean:write name="infoDegreeInfo" property="history" /></p>
				</div>
				</logic:present>
				
		  </td>
		</tr>
	</table>

</logic:present>