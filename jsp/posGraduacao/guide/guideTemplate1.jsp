<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>

<html>
    <head>
    	<title><bean:message key="title.masterDegree.administrativeOffice.printGuide"/></title>
    </head>

    <body>
     <bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="session" />
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
                    <td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br/>
                      &nbsp;<b>Secretaria da Pós-Graduação</b><br/>
                      &nbsp;<b>Centro de Custo 0212</b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b>Guia de Pagamento Nº: </b> 
                     <bean:write name="guide" property="number"/>/<bean:write name="guide" property="year"/> 
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
          <logic:present name="<%= SessionConstants.REQUESTER_NUMBER%>">
	          <tr>
	            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
	            <td> <bean:write name="<%= SessionConstants.REQUESTER_NUMBER%>" /> </td>
	          </tr>
          </logic:present>

          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
            <td> <bean:write name="guide" property="infoPerson.nome"/> </td>
          </tr>
          <logic:present name="<%= SessionConstants.STUDENT%>">
			  <tr>
				<td> <bean:message key="label.number"/> </td>
				<td> <bean:write name="<%= SessionConstants.STUDENT%>" /> </td>
			  </tr>  
          </logic:present>          
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="guide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>
          <logic:present name="graduationType">
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
            <td> <bean:message  name="graduationType" bundle="ENUMERATION_RESOURCES"/> </td>
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
            <td><bean:write name="guide" property="infoContributor.contributorNumber"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>:</td>
            <td><bean:write name="guide" property="infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td valign="top"><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>:</td>
            <td><bean:write name="guide" property="infoContributor.contributorAddress"/></td>
          </tr>
		<logic:notEmpty name="guide" property="infoContributor.areaCode">
			<tr>
				<td><bean:message key="label.person.postCode" /></td>
				<td><bean:write name="guide" property="infoContributor.areaCode" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="guide" property="infoContributor.areaOfAreaCode">
			<tr>
				<td><bean:message key="label.person.areaOfPostCode" /></td>
				<td><bean:write name="guide" property="infoContributor.areaOfAreaCode" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="guide" property="infoContributor.area">
			<tr>
				<td><bean:message key="label.person.place" /></td>
				<td><bean:write name="guide" property="infoContributor.area" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="guide" property="infoContributor.parishOfResidence">
			<tr>
				<td><bean:message key="label.person.addressParish" /></td>
				<td><bean:write name="guide" property="infoContributor.parishOfResidence" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="guide" property="infoContributor.districtSubdivisionOfResidence">
			<tr>
				<td><bean:message key="label.person.addressMunicipality" /></td>
				<td><bean:write name="guide" property="infoContributor.districtSubdivisionOfResidence" /></td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="guide" property="infoContributor.districtOfResidence">
			<tr>
				<td><bean:message key="label.person.addressDistrict" /></td>
				<td><bean:write name="guide" property="infoContributor.districtOfResidence" /></td>
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
        	<logic:iterate id="guideEntry" name="guide" property="infoGuideEntries" >
        	  <tr>
    			<td><bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
				<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
    			<logic:equal name="documentType" value="GRATUITY">
    				(referente ao Ano Lectivo de <bean:write name="guide" property="infoExecutionDegree.infoExecutionYear.year"/>)
    			</logic:equal>
    			&nbsp;<bean:write name="guideEntry" property="description"/></td>
    			<td>.........................................</td>&nbsp;
                <bean:define id="price" name="guideEntry" property="price" />
                <bean:define id="quantity" name="guideEntry" property="quantity" />
                <td><%= NumberUtils.formatNumber(new Double(new Double(pageContext.findAttribute("price").toString()).floatValue() *  new Integer(pageContext.findAttribute("quantity").toString()).intValue()), 2) %>&nbsp;<bean:message key="label.currencySymbol"/></td>
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
   			<td><strong><bean:write name="guide" property="total"/>&nbsp;<bean:message key="label.currencySymbol"/></strong></td>
    	</tr>
	   </table>
	 </td>
	 </tr>
	 <tr>
	 <td>
	 <table  width="100%" align="right">
		 <tr>	
		 <td><bean:write name="guide" property="infoGuideSituation.remarks"/></td>
		 </tr>
	 </table>
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
			<bean:write name="<%= SessionConstants.DATE %>" />			
         </td>
       </tr>
       
       <tr>
        <td>&nbsp;</td>
         <td colspan="2" valign="bottom">
           &nbsp;<div align="center">&nbsp;</div>
           <div align="center">&nbsp;</div>
           <div align="center"><b>O Funcionário</b> <br/>
            <br/>
            <br/>
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