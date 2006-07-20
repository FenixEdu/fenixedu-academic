<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>

<html>
    <head>
    	<title><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.reimbursementGuideTitle"/></title>
    </head>

    <body>
     <bean:define id="reimbursementGuide" name="<%= SessionConstants.REIMBURSEMENT_GUIDE %>" scope="request" />
   	
    <table width="100%" height="100%" border="0">
    <tr height="30"><td>
     <table width="100%" border="0" valign="top">
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="104" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="100">
               <img src="<%= request.getContextPath() %>/posGraduacao/guide/images/istlogo.gif" alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr align="left"> 
                    <td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br>
                      &nbsp;<b>Secretaria da Pós-Graduação</b><br>
                      &nbsp;<b>Centro de Custo 0212</b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> 
                    	<b>Guia de Reembolso Nº: </b><bean:write name="reimbursementGuide" property="number"/> <br/>
                    	<b>relativa à Guia de Pagamento nº </b>
                    		<bean:write name="reimbursementGuide" property="infoGuide.number"/>/<bean:write name="reimbursementGuide" property="infoGuide.year"/>                   	                      
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
	</table>

	</td>
	</tr>
    <tr valign="top" >
    <td>

	<table width="100%" border="0">
	 <tr>
	 <td>
      <table width="100%" border="0">
          <tr>
            <td width="20%"><strong>Processo de:</strong></td>
            <td width="80%">&nbsp;</td>
          </tr>

          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
            <td> <bean:write name="reimbursementGuide" property="infoGuide.infoPerson.nome"/> </td>
          </tr>
          
          <logic:present name="<%= SessionConstants.STUDENT %>">
	          <bean:define id="student" name="<%= SessionConstants.STUDENT %>"/>
	          <tr>
	            <td> <bean:message key="label.number"/> </td>
	            <td> <bean:write name="student" property="number"/> </td>
	          </tr>          
	      </logic:present>
          
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="reimbursementGuide" property="infoGuide.infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>
          <logic:present name="graduationType">
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
            <td> <bean:write name="graduationType"/> </td>
          </tr>
          <tr>
          </logic:present>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
    
          <tr> 
            <td width="30%"><strong>Entidade Pagadora:</strong> </td>
            <td width="70%" >&nbsp;</td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>:</td>
            <td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.contributorNumber"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>:</td>
            <td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td valign="top"><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>:</td>
            <td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.contributorAddress"/></td>
          </tr>
		<logic:notEmpty name="reimbursementGuide" property="infoGuide.infoContributor.areaCode">
			<tr>
				<td><bean:message key="label.person.postCode" /></td>
				<td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.areaCode" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="reimbursementGuide" property="infoGuide.infoContributor.areaOfAreaCode">
			<tr>
				<td><bean:message key="label.person.areaOfPostCode" /></td>
				<td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.areaOfAreaCode" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="reimbursementGuide" property="infoGuide.infoContributor.area">
			<tr>
				<td><bean:message key="label.person.place" /></td>
				<td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.area" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="reimbursementGuide" property="infoGuide.infoContributor.parishOfResidence">
			<tr>
				<td><bean:message key="label.person.addressParish" /></td>
				<td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.parishOfResidence" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="reimbursementGuide" property="infoGuide.infoContributor.districtSubdivisionOfResidence">
			<tr>
				<td><bean:message key="label.person.addressMunicipality" /></td>
				<td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.districtSubdivisionOfResidence" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="reimbursementGuide" property="infoGuide.infoContributor.districtOfResidence">
			<tr>
				<td><bean:message key="label.person.addressDistrict" /></td>
				<td><bean:write name="reimbursementGuide" property="infoGuide.infoContributor.districtOfResidence" /></td>
			</tr>
		</logic:notEmpty>          
	  </table>
	 </td>
	 </tr>
	 </table>
	 </td>
	 </tr>
	
	 <tr>
	 <td> 
	   <table align="right">
	   	    <tr>
				<td >Reembolso relativo a:</td>
           	</tr>
           	<tr>
		        <td>&nbsp;</td>
            </tr>
		   	<% double total = 0; %>
        	<logic:iterate id="reimbursementGuideEntry" name="reimbursementGuide" property="infoReimbursementGuideEntries" >
        	  <tr>
    			<td>
    				<bean:define id="documentType"><bean:write name="reimbursementGuideEntry" property="infoGuideEntry.documentType"/></bean:define>
					<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />&nbsp;<bean:write name="reimbursementGuideEntry" property="infoGuideEntry.description"/></td>
    			<td>.........................................</td>&nbsp;
    			<bean:define id="value" name="reimbursementGuideEntry" property="value" />
    			<td><bean:write name="value" />
                <% total = total + new Double(pageContext.findAttribute("value").toString()).floatValue(); %>
                
                <td>&nbsp;<bean:message key="label.currencySymbol"/></td>
        	  </tr>
        	</logic:iterate >
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    	<tr>
    	  	<td><strong>A liquidar a importância de </strong></td>
   			<td>_____________________</td>&nbsp;
   			<td><strong><%= NumberUtils.formatNumber(new Double(total), 2) %>&nbsp;<bean:message key="label.currencySymbol"/></strong></td>
    	</tr>
	   </table>
	 </td>
	 </tr>
	 <tr>
	 <td>

	 </td>
	 </tr>
	 <tr>
	 <td>&nbsp;
	 </td>
	 </tr>
	 <tr>
	 <td>&nbsp;
	 </td>
	 </tr>
	 <tr valign="bottom">
	 <td>
     <table valign="bottom" width="100%" border="0">
       <tr>
         <td>
			<bean:message key="label.city"/>,&nbsp;<bean:write name="<%= SessionConstants.DATE %>" />		
         </td>
       </tr>
       
       <tr>
        <td>&nbsp;</td>
         <td colspan="2" valign="bottom">
           &nbsp;<div align="center">&nbsp;</div>
           <div align="center">&nbsp;</div>
           <div align="center"><b>O Funcionário</b> <br>
            <br>
            <br>
           </div>
          <hr align="center" width="300" size="1">
         </td>
       </tr>

	 </table>
	 </td>
	 </tr>
	 
     <tr>	 
	 <td>
     <table width="100%" border="0">
      <tr>	 
 	  <td>
	 	<table align="center" width="100%" valign="bottom">
	      <tr>
          <td colspan="2" valign="bottom" >
            <div align="center">
              <font size="2"> Documento processado por computador. Só é válido como recibo após o carimbo de pago e devidamente assinado.</font> 
            </div>
            <hr size="1" color="#000000" width="100%">
            <div align="center">
              <font size="2"> Av. Rovisco Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte Nº: 501507930</font>
            </div>
          </td>
          </tr>
        </table>
     </td>	 
	 </tr>
	</table>

    </td>
    </tr> 
	
    </table>
    
	
    </body>
</html>