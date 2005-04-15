<%@ page language="java" %>
<%@ page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<p><span class="error"><html:errors/></span></p>


 	<html:form action="/consultarFuncionarioEscolha" focus="diaInicioEscolha">
    	<table>
    		<tr>
    			<td>
    			<h2>
      				<bean:message key="consultarFuncionario.titulo" />:
        			<bean:write name="ConsultarFuncionarioMostrarForm" property="numMecanografico" filter="true"/>
   				</h2>
				<br />
    			</td> 
    		</tr>
    		<tr>
    			<td colspan="2">
    				<span class="error"><html:errors/></span>
    			</td>
    		</tr>
        	<tr>
            	<td><bean:message key="prompt.consultar"/>:
            	    <html:select name="ConsultarFuncionarioMostrarForm" property="escolha">
                   	<app:options name="ConsultarFuncionarioMostrarForm" property="listaEscolhas"/>
                   	</html:select>
                   	<bean:message key="prompt.de" />
                  	<html:text name="ConsultarFuncionarioMostrarForm" property="diaInicioEscolha" size="2" maxlength="2"/> / 
                   	<html:text name="ConsultarFuncionarioMostrarForm" property="mesInicioEscolha" size="2" maxlength="2"/> / 
                   	<html:text name="ConsultarFuncionarioMostrarForm" property="anoInicioEscolha" size="4" maxlength="4"/>
                   	<bean:message key="prompt.ate"/>
                    <html:text name="ConsultarFuncionarioMostrarForm" property="diaFimEscolha" size="2" maxlength="2"/> / 
                   	<html:text name="ConsultarFuncionarioMostrarForm" property="mesFimEscolha" size="2" maxlength="2"/> / 
                    <html:text name="ConsultarFuncionarioMostrarForm" property="anoFimEscolha" size="6" maxlength="4"/>
               	</td>
        	</tr>
            <tr>
              <td class="tall-td" colspan="2"><br />
                <html:submit property="submit" styleClass="inputbutton">
                	<bean:message key="botao.ok"/>
                </html:submit>
                <html:reset styleClass="inputbutton">
                    <bean:message key="botao.apagar"/>
                </html:reset>
              </td>
            </tr>
       </table>
       <br />
	   <table class="infotable">
            <tr>
              <td colspan="2" class="bottomborder" align="left">
                <h2 style="display: inline;"><bean:message key="prompt.dadosPessoais" /></h2>
              </td>
            </tr>
            <tr>
              <td width="30%">  
                  <bean:message key="prompt.nome"/>
              </td>
              <td class="greytxt">
                  <bean:write name="ConsultarFuncionarioMostrarForm"  property="nome" filter="true"/> 
              </td>
            </tr>
            <tr>
              <td width="30%">
                  <bean:message key="prompt.morada"/>
              </td>
              <td class="greytxt">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="morada" filter="true"/>
              </td>
            </tr>
            <tr>
              <td width="30%">
                  <bean:message key="prompt.codigoPostal"/>
              </td>
              <td class="greytxt">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="codigoPostal" filter="true"/>&nbsp;
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="localidade" filter="true"/>
              </td>
            </tr>
            <tr>
              <td width="30%">
                  <bean:message key="prompt.telefone"/>
              </td>
              <td class="greytxt">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="telefone" filter="true"/>
              </td>
            </tr>
            <tr>
              <td width="30%">
                  <bean:message key="prompt.email"/>
              </td>
              <td class="greytxt">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="email" filter="true"/>
              </td>
            </tr>

            <tr>
              <td colspan="2"><br></td>       
            </tr>      
            <tr>
             	<td colspan="2" class="bottomborder" align="left">
                <h2 style="display: inline;"><bean:message key="prompt.dadosAssiduidade" /></h2>      
              </td>
            </tr>
			<tr>
              <td width="30%">
                  <bean:message key="prompt.statusAssiduidade"/>:
              </td>
              <td class="greytxt">
                  <bean:write name="ConsultarFuncionarioMostrarForm" property="statusAssiduidade" filter="true"/>
              </td>
            </tr>
    			<logic:iterate id="horario" name="ConsultarFuncionarioMostrarForm" property="rotacaoHorario">
              <tr>
                <td colspan="2">
                </td>       
              </tr>                       
              <tr>
                <td width="30%">
                    <bean:message key="prompt.inicioHorario"/>:
                </td>
                <td class="greytxt">
                    <bean:write name="horario" property="diaCumprir" /> / 
                    <bean:write name="horario" property="mesCumprir" /> / 
                    <bean:write name="horario" property="anoCumprir" />
                </td>
              </tr>  
              <tr>
                <td width="30%">
                    <bean:message key="prompt.horarioNormal"/>
                </td>
                <td class="greytxt">
                    <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioHN1Horas" /><b>:</b><bean:write name="horario" property="inicioHN1Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorHN1" />
                    <bean:message key="prompt.as"/> <bean:write  name="horario" property="fimHN1Horas" /><b>:</b><bean:write name="horario" property="fimHN1Minutos" />&nbsp;<bean:write name="horario" property="diaSeguinteHN1" />
                    <bean:message key="prompt.e"/> <bean:message key="prompt.das"/> <bean:write  name="horario" property="inicioHN2Horas" /><b>:</b><bean:write name="horario" property="inicioHN2Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorHN2" />
                    <bean:message key="prompt.as"/> <bean:write  name="horario" property="fimHN2Horas" /><b>:</b><bean:write name="horario" property="fimHN2Minutos" />&nbsp;<bean:write name="horario" property="diaSeguinteHN2" />
                </td>
              </tr>
              <tr>
                <td width="30%">
                    <bean:message key="prompt.periodoFixo"/>:
                </td>
                <td class="greytxt">
                    <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioPF1Horas" /><b>:</b><bean:write name="horario" property="inicioPF1Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorPF1" />
                    <bean:message key="prompt.as"/> <bean:write name="horario" property="fimPF1Horas" /><b>:</b><bean:write name="horario" property="fimPF1Minutos" />&nbsp;<bean:write name="horario" property="diaSeguintePF1" />
                    <bean:message key="prompt.e"/> <bean:message key="prompt.das"/> <bean:write name="horario" property="inicioPF2Horas" /><b>:</b><bean:write name="horario" property="inicioPF2Minutos" />&nbsp;<bean:write name="horario" property="diaAnteriorPF2" />
                    <bean:message key="prompt.as"/> <bean:write name="horario" property="fimPF2Horas" /><b>:</b><bean:write name="horario" property="fimPF2Minutos" />&nbsp;<bean:write name="horario" property="diaSeguintePF2" />
                 </td>
              </tr>
          	 <tr>
             	<td width="30%">
                	<bean:message key="prompt.expediente"/>:
            	</td>
            	<td class="greytxt">
                	<bean:message key="prompt.das"/> <bean:write name="horario" property="inicioExpedienteHoras" /><b>:</b><bean:write name="horario" property="inicioExpedienteMinutos" />&nbsp;<bean:write name="horario" property="diaAnteriorExpediente" />
                	<bean:message key="prompt.as"/> <bean:write name="horario" property="fimExpedienteHoras" /><b>:</b><bean:write name="horario" property="fimExpedienteMinutos" />&nbsp;<bean:write name="horario" property="diaSeguinteExpediente" />
           		</td>
          	</tr>
          	<tr>
	          	<td width="30%">
    	            <bean:message key="prompt.intervaloRefeicao"/>:
        	    </td>
            	<td class="greytxt">
                	<bean:message key="prompt.das"/> <bean:write name="horario" property="inicioRefeicaoHoras" /><b>:</b><bean:write name="horario" property="inicioRefeicaoMinutos" />&nbsp;<bean:write name="horario" property="diaAnteriorRefeicao" />
                	<bean:message key="prompt.as"/> <bean:write name="horario" property="fimRefeicaoHoras" /><b>:</b><bean:write name="horario" property="fimRefeicaoMinutos" />&nbsp;<bean:write name="horario" property="diaSeguinteRefeicao" />
            	</td>
          	</tr>             
          	<tr>
            	<td width="30%">
                	<bean:message key="prompt.intervaloMinimoRefeicao"/>:
            	</td>
            	<td class="greytxt">
                	<bean:message key="prompt.de"/> <bean:write name="horario" property="intervaloMinimoHoras" /><b>:</b><bean:write name="horario" property="intervaloMinimoMinutos" />
            	</td>
          	</tr>
          	<tr>
            	<td width="30%">
                	<bean:message key="prompt.descontoObrigatorioRefeicao"/>:
            	</td>
            	<td class="greytxt">
                	<bean:message key="prompt.de"/> <bean:write name="horario" property="descontoObrigatorioHoras" /><b>:</b><bean:write name="horario" property="descontoObrigatorioMinutos" />
            	</td>
          	</tr>   
            <tr>
          		<td width='30%'>
              		<bean:message key="prompt.trabalhoConsecutivo"/>:
              	</td>
            	<td class="greytxt">
                	<bean:message key="prompt.de"/> <bean:write name="horario" property="trabalhoConsecutivoHoras" /><b>:</b><bean:write name="horario" property="trabalhoConsecutivoMinutos" />
        		</td>
          	</tr>             	
            <tr>
                <td width="30%">
                    <bean:message key="prompt.numHorasSemanais"/>
                </td>
                <td class="greytxt">
                    <bean:write name="horario" property="duracaoSemanal" />
                </td>
              </tr>
              <tr>
                <td width="30%">
                    <bean:message key="prompt.horario"/>:
                </td>
                <td class="greytxt">
	                <bean:define id="modalidade" name="horario" property="modalidade"/>
                    <app:message key="modalidade"/>
                </td>
              </tr>
              <tr>
                <td width="30%">
                    <bean:message key="prompt.regime"/>:
                </td>
                <td class="greytxt">
                   <logic:iterate id="listaRegime" name="horario" property="listaRegimes">
                      <app:message key="listaRegime"/>&nbsp;
                   </logic:iterate>
                </td>     
              </tr>
              <tr>
                <td width="30%">
                    <bean:message key="prompt.validade"/>
                </td>
                <td class="greytxt">
                    <bean:message key="prompt.de"/>
                    <bean:write name="horario" property="diaInicio" /> / 
                    <bean:write name="horario" property="mesInicio" /> / 
                    <bean:write name="horario" property="anoInicio" />
                    <bean:message key="prompt.ate"/>
                    <bean:write name="horario" property="diaFim" /> / 
                    <bean:write name="horario" property="mesFim" /> / 
                    <bean:write name="horario" property="anoFim" />
                </td>
              </tr>
            </logic:iterate>          
      </table>
    <br />
    <br />
  </html:form>