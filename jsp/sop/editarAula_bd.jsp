<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.editAula"/> </b> </font></center>
        <br/>
        <br/>
        <span class="error"><html:errors/></span>
        <html:form action="/editarAulaForm">
            <table align="center" cellspacing="10"> 
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.aula.weekDay"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="diaSemana">
                            <html:options collection="diasSemana" property="value" labelProperty="label"/>
                        </html:select>
                   </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.aula.time.begining"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="horaInicio">
                            <html:options name="horas"/>
                        </html:select> :
                        <html:select property="minutosInicio">
                            <html:options name="minutos"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.aula.time.end"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="horaFim">
                            <html:options name="horas"/>
                        </html:select> :
                        <html:select property="minutosFim">
                            <html:options name="minutos"/>
                        </html:select>
                    </td>
                 </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.aula.type"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="tipoAula">
                            <html:options collection="tiposAula" property="value" labelProperty="label"/>
                        </html:select>
                     </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.aula.sala"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="nomeSala" size="1">
                            <html:options collection="listaSalas" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
            </table>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit>
                            <bean:message key="label.save"/>
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:reset>
                            <bean:message key="label.clear"/>
                        </html:reset>
                    </td>
                </tr>
            </table>
        </html:form>
