<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
    <head>
    	<title><bean:message key="title.masterDegree.administrativeOffice.printGuide"/></title>
    </head>

    <body>
     <bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="session" />

     <table width="100%" height="100%" border="0">
      <tr> 
        <td height="104" colspan="2">
          <table border="0" width="100%" height="104" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="104">
               <img src="/posgrad/Imagens/istlogo2.gif" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr align="left"> 
                    <td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br>
                      &nbsp;<b>Secretaria da Pós-Graduação</b><br>
                      &nbsp;<b>Morada:</b>&nbsp;Av. Rovisco Pais, 1 1049 - 001 Lisboa Codex<br>
                      &nbsp;<b>Centro de Custo 0212</b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b>Guia de Pagamento Nº: </b> 
                     <bean:write name="guide" property="number"/> 
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          &nbsp;
        </td>
        <td>
          &nbsp;
        </td>
      </tr>

      <tr>
        <td width="20%"><strong>PROCESSO DE:</strong></td>
        <td width="80%">&nbsp;</td>
      </tr>
<%--
      <tr>
        <td> Nº de Aluno </td>
        <td> <bean:write scope="session" name="guiaForm" property="numeroAluno"/>
        </td>
      </tr>
--%>
      <tr>
        <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
        <td> <bean:write name="guide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>

      <tr> 
        <td><strong>Entidade Pagadora:</strong> </td>
        <td>&nbsp;</td>
      </tr>
      <tr> 
        <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/></td>
        <td><bean:write name="guide" property="infoContributor.contributorNumber"/></td>
      </tr>
      <tr> 
        <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/></td>
        <td><bean:write name="guide" property="infoContributor.contributorName"/></td>
      </tr>
      <tr> 
        <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/></td>
        <td><bean:write name="guide" property="infoContributor.contributorAddress"/></td>
      </tr>


	  <tr>
    	<logic:iterate id="guideEntry" name="guide" property="infoGuideEntries" >
    	  <tr>
			<td> <bean:write name="guideEntry" property="documentType"/> <bean:write name="guideEntry" property="description"/></td>
			<td><bean:write name="guideEntry" property="price"/></td>
    	  </tr>
    	</logic:iterate >
	  </tr>

<%--
      <tr> 
        <td colspan="2">
          <table align="center" width="100%">
            <tr>
              <td align="center">
                <b>
                  Quant.
                </b>
              </td>
              <td align="center">
                <b>
                  Designação
                </b>
              </td>
              <td align="center">
                <b>
                  Montante
                </b>
              </td>
            </tr>
    	<logic:iterate id="iter1" name="guia" type="util.AuxArray">
            <tr>
              <td>
                &nbsp;
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                &nbsp;
              </td>
            </tr>
    	  <tr>
    	    <td>
    	      &nbsp;
    	    </td>
    	    <td align="left">
    	      <bean:write name="iter1" property="label" />
    	    </td>
    	    <td>
    	      &nbsp;
    	    </td>
    	  </tr>
    	  <logic:iterate id="iter2" name="iter1" property="value" indexId="index" type="java.util.ArrayList">
    	    <tr>
    	      <logic:iterate id="iter3" name="iter2" indexId="index1">
    	        <logic:equal name="index" value="0">
                      <td align="center">
    	        </logic:equal>
    	        <logic:equal name="index" value="1">
    	          <td align="left">
    	        </logic:equal>
    	        <logic:equal name="index" value="2">
    	          <td align="center">
    	        </logic:equal>
    	        <bean:write name="iter3" />
    	        </td>
    	      </logic:iterate>
    	    </tr>
    	  </logic:iterate>
    	</logic:iterate>
    	<tr>
              <td>
                &nbsp;
              </td>
    	  <td>
    	    <hr align="center" size="1" />
    	  </td>
    	  <td>
    	    <hr align="center" size="1" />
    	  </td>
    	</tr>
            <tr>
              <td>
                &nbsp;
              </td>
              <td align="left">
                <b>
                  A liquidar a importância de:
                </b>
              </td>
              <td align="center">
                <bean:write name="guiaForm" property="total" />
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2" valign="bottom"> <div align="center"><b>O Funcionário</b> <br>
            <br>
            <br>
          </div>
          <hr align="center" width="300" size="1"> </td>
      </tr>
      <tr>
        <td colspan="2" valign="bottom"> <div align="center"><font size="2"> Documento 
            processado por computador; Só é válido como recibo após o carimbo de pago 
            e devidamente assinado. </font> </div>
          <hr align="center" size="1"> <div align="center"><font size="2"> Av. Rovisco 
            Pais, 1 1049-001 Lisboa Codex Telefone: 218417336 Fax: 218419531 Contribuinte 
            Nº: 501507930 </font> </div></td>
      </tr>
--%>   
     </table>
    </body>
</html>