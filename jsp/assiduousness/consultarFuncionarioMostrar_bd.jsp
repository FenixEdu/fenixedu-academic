<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<div align="left">
  <html:form action="/consultarFuncionarioEscolha" focus="diaInicioEscolha">
    <table >
      <tr bgcolor='#ffffff'>
        <td> 
          <html:errors/>
        </td>
      </tr>
      <tr>
        <td> 
          <div style='width=580' align="right">
            <font size="5" face="Arial, Helvetica, sans-serif">
              <bean:message key="consultarFuncionario.titulo" />&nbsp;&nbsp;
              <bean:write name="ConsultarFuncionarioMostrarForm" property="numMecanografico" filter="true"/>
            </font>
          </div><br>
        </td>
      </tr>

      <tr>
        <td> 
          <table width="100%" border="0">
            <tr>
              <td colspan='2' align="center">
                &nbsp;&nbsp;&nbsp; <p></p>
              </td>
            </tr>
            <tr>
              <td colspan='2' align="center">
                <table width="100%" border="0">
                  <tr>
                    <td bgcolor="#FFFFCC" align="right" width='15%'>
                      <font size="2" face="Arial, Helvetica, sans-serif">
                        <bean:message key="prompt.consultar"/>:
                      </font>
                    </td>
                    <td align="left">
                      <font size="2" face="Arial, Helvetica, sans-serif">
                      	<html:select name="ConsultarFuncionarioMostrarForm" property="escolha">
                          <app:options name="ConsultarFuncionarioMostrarForm" property="listaEscolhas"/>
                        </html:select> 
                        <bean:message key="prompt.de"/>
                        <html:text name="ConsultarFuncionarioMostrarForm" property="diaInicioEscolha" size="2" maxlength="2"/> / 
                        <html:text name="ConsultarFuncionarioMostrarForm" property="mesInicioEscolha" size="2" maxlength="2"/> / 
                        <html:text name="ConsultarFuncionarioMostrarForm" property="anoInicioEscolha" size="4" maxlength="4"/>
                        <bean:message key="prompt.ate"/>
                        <html:text name="ConsultarFuncionarioMostrarForm" property="diaFimEscolha" size="2" maxlength="2"/> / 
                        <html:text name="ConsultarFuncionarioMostrarForm" property="mesFimEscolha" size="2" maxlength="2"/> / 
                        <html:text name="ConsultarFuncionarioMostrarForm" property="anoFimEscolha" size="4" maxlength="4"/>
                      </font>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            
            <tr>
              <td colspan='2' align="center">
                &nbsp;&nbsp;&nbsp; <p></p><p></p>
              </td>
            </tr>

            <tr>
              <td align="center" colspan='2'>
                <html:submit property="submit" style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                    &nbsp;&nbsp;&nbsp;<bean:message key="botao.ok"/>&nbsp;&nbsp;&nbsp;
                </html:submit>
                <html:reset style="color:#000080; width=75px; background-color:#B1BBD6; height=20px; font-size=10px">
                    <bean:message key="botao.apagar"/>
                </html:reset>
              </td>
            </tr>

            <tr>
              <th align="center" colspan='2'>
                <p>&nbsp;</p>
                <hr>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.dadosPessoais" />
                </font>
              </th>
            </tr>
            <tr>
              <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>  
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.nome"/>
                </font>
              </td>
              <td align="left">
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:write name="ConsultarFuncionarioMostrarForm"  property="nome" filter="true"/> 
                </font>
              </td>
            </tr>
            <tr>
              <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.morada"/>
                </font>
              </td>
              <td align="left">
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="morada" filter="true"/>
                </font>
              </td>
            </tr>
            <tr>
              <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.codigoPostal"/>
                </font>
              </td>
              <td align="left">
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="codigoPostal" filter="true"/>&nbsp;
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="localidade" filter="true"/>
                </font>
              </td>
            </tr>
            <tr>
              <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.telefone"/>
                </font>
              </td>
              <td align="left">
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="telefone" filter="true"/>
                </font>
              </td>
            </tr>
            <tr>
              <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.email"/>
                </font>
              </td>
              <td align="left">
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="email" filter="true"/>
                </font>
              </td>
            </tr>
            <%--
            <tr>
              <td colspan='2' align="right">
                <br>
                <html:link forward="PortalAssiduidadeAction"><bean:message key="link.menu"/></html:link>&nbsp;&nbsp;
              </td>
            </tr>--%>

            <tr>
              <td colspan='2'><br></td>       
            </tr>      

            <tr>
              <th colspan='2' align="center">
                <hr>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.dadosAssiduidade" />
                </font>
              </th>
            </tr>

            <tr>
              <td colspan='2'><br></td>       
            </tr>  
              
			<tr>
              <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:message key="prompt.statusAssiduidade"/>:
                </font>
              </td>
              <td align="left">
                <font size="2" face="Arial, Helvetica, sans-serif">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="statusAssiduidade" filter="true"/>
                </font>
              </td>
            </tr>
            
            <logic:iterate id="horario" name="ConsultarFuncionarioMostrarForm" property="rotacaoHorario">
              <tr>
                <td colspan='2'><br></td>       
              </tr>               
              
              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.inicioHorario"/>:
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:write name="horario" property="diaCumprir" /> / 
                    <bean:write name="horario" property="mesCumprir" /> / 
                    <bean:write name="horario" property="anoCumprir" />
                  </font>
                </td>
              </tr>  

              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.horarioNormal"/>
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioHN1Horas" /><b>:</b><bean:write name="horario" property="inicioHN1Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorHN1" />
                    <bean:message key="prompt.as"/> <bean:write  name="horario" property="fimHN1Horas" /><b>:</b><bean:write name="horario" property="fimHN1Minutos" />&nbsp;<bean:write name="horario" property="diaSeguinteHN1" />
                    <bean:message key="prompt.e"/> <bean:message key="prompt.das"/> <bean:write  name="horario" property="inicioHN2Horas" /><b>:</b><bean:write name="horario" property="inicioHN2Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorHN2" />
                    <bean:message key="prompt.as"/> <bean:write  name="horario" property="fimHN2Horas" /><b>:</b><bean:write name="horario" property="fimHN2Minutos" />&nbsp;<bean:write name="horario" property="diaSeguinteHN2" />
                  </font>
                </td>
              </tr>

              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.periodoFixo"/>:
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioPF1Horas" /><b>:</b><bean:write name="horario" property="inicioPF1Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorPF1" />
                    <bean:message key="prompt.as"/> <bean:write name="horario" property="fimPF1Horas" /><b>:</b><bean:write name="horario" property="fimPF1Minutos" />&nbsp;<bean:write name="horario" property="diaSeguintePF1" />
                    <bean:message key="prompt.e"/> <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioPF2Horas" /><b>:</b><bean:write name="horario" property="inicioPF2Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorPF2" />
                    <bean:message key="prompt.as"/> <bean:write name="horario" property="fimPF2Horas" /><b>:</b><bean:write name="horario" property="fimPF2Minutos" />&nbsp;<bean:write name="horario" property="diaSeguintePF2" />
                   </font>
                 </td>
              </tr>

          <tr>
            <td bgcolor="#FFFFCC" align="right" width='30%'>
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.expediente"/>:
              </font>
            </td>
            <td align="left">
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioExpedienteHoras" /><b>:</b><bean:write name="horario" property="inicioExpedienteMinutos" />&nbsp;<bean:write name="horario" property="diaAnteriorExpediente" />
                <bean:message key="prompt.as"/> <bean:write name="horario" property="fimExpedienteHoras" /><b>:</b><bean:write name="horario" property="fimExpedienteMinutos" />&nbsp;<bean:write name="horario" property="diaSeguinteExpediente" />
              </font>
            </td>
          </tr>
          
          <tr>
          	<td bgcolor="#FFFFCC" align="right" width='30%'>
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.intervaloRefeicao"/>:
              </font>
            </td>
            <td align="left">
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioRefeicaoHoras" /><b>:</b><bean:write name="horario" property="inicioRefeicaoMinutos" />&nbsp;<bean:write name="horario" property="diaAnteriorRefeicao" />
                <bean:message key="prompt.as"/> <bean:write name="horario" property="fimRefeicaoHoras" /><b>:</b><bean:write name="horario" property="fimRefeicaoMinutos" />&nbsp;<bean:write name="horario" property="diaSeguinteRefeicao" />
              </font>
            </td>
          </tr>             
          <tr>
            <td bgcolor="#FFFFCC" align="right" width='30%'>
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.intervaloMinimoRefeicao"/>:
              </font>
            </td>
            <td align="left">
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.de"/> <bean:write name="horario" property="intervaloMinimoHoras" /><b>:</b><bean:write name="horario" property="intervaloMinimoMinutos" />
              </font>
            </td>
          </tr>
          <tr>
            <td bgcolor="#FFFFCC" align="right" width='30%'>
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.descontoObrigatorioRefeicao"/>:
              </font>
            </td>
            <td align="left">
              <font size="2" face="Arial, Helvetica, sans-serif">
                <bean:message key="prompt.de"/> <bean:write name="horario" property="descontoObrigatorioHoras" /><b>:</b><bean:write name="horario" property="descontoObrigatorioMinutos" />
              </font>
            </td>
          </tr>   

              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.numHorasSemanais"/>
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:write name="horario" property="duracaoSemanal" />
                  </font>
                </td>
              </tr>

              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.horario"/>:
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message name="horario" property="modalidade" />
                  </font>
                </td>
              </tr>

              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.regime"/>:
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <logic:iterate id="listaRegimes" name="horario" property="listaRegimes">
                      <bean:message name="listaRegimes"/>&nbsp;
                    </logic:iterate>
                  </font>
                </td>     
              </tr>

              <tr>
                <td bgcolor="#FFFFCC" height='25px' align="right" width='30%'>
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.validade"/>
                  </font>
                </td>
                <td align="left">
                  <font size="2" face="Arial, Helvetica, sans-serif">
                    <bean:message key="prompt.de"/>
                    <bean:write name="horario" property="diaInicio" /> / 
                    <bean:write name="horario" property="mesInicio" /> / 
                    <bean:write name="horario" property="anoInicio" />
                    <bean:message key="prompt.ate"/>
                    <bean:write name="horario" property="diaFim" /> / 
                    <bean:write name="horario" property="mesFim" /> / 
                    <bean:write name="horario" property="anoFim" />
                  </font>
                </td>
              </tr>

            </logic:iterate>          
			<%--
            <tr>
              <td colspan='2' align="right">
                <br>
                <html:link forward="PortalAssiduidadeAction"><bean:message key="link.menu"/></html:link>&nbsp;&nbsp;
              </td>
            </tr>        
            --%>
          </table>
        </td>
      <tr>
    </table>
  </html:form>
</div>