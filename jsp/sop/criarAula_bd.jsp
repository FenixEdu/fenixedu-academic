<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
    	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
              </td>
          </tr>
        </table>
		<br />
        <h2><bean:message key="title.criarAula"/></h2>
        <span class="error"><html:errors/></span>
        <html:form action="/criarAulaForm">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td nowrap class="formTD">
                        <bean:message key="property.aula.weekDay"/>
                    </td>
                    <td nowrap class="formTD">
                        <html:select property="diaSemana"  size="1">
                        	<option value="" selected="selected">escolher</option>
                            <html:options collection="diasSemana" property="value" labelProperty="label"/>
                        </html:select>
                   </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.aula.time.begining"/>
                    </td>
                    <td nowrap="nowrap">
                        <html:select property="horaInicio"  size="1">
                        	<option value="" selected="selected">[Horas]</option>                        
                            <html:options name="horas"/>
                        </html:select> :
                        <html:select property="minutosInicio" size="1">
                            <html:options name="minutos"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.aula.time.end"/>
                    </td>
                    <td nowrap="nowrap">
                        <html:select property="horaFim"  size="1">
                        	<option value="" selected="selected">[Horas]</option>                        
                            <html:options name="horas"/>
                        </html:select> :
                        <html:select property="minutosFim"  size="1">
                            <html:options name="minutos"/>
                        </html:select>
                    </td> 
                </tr> 
                <tr>
                    <td nowrap class="formTD">
                        <bean:message key="property.aula.type"/>
                    </td>
                    <td nowrap class="formTD">
                        <html:select property="tipoAula" size="1">
                        	<option selected="selected">escolher</option>
                            <html:options collection="tiposAula" property="value" labelProperty="label"/>
                        </html:select>
                  </td>
                </tr>
                <tr>
                    <td nowrap class="formTD">
                        <bean:message key="property.aula.disciplina"/>
                    </td>
                    <td align="left" height="40">
                    	<jsp:include page="selectExecutionCourseList.jsp"/>
                    </td>
                </tr>
          </table>
            <br/>
            <table align="lef">
                <tr align="center">
                    <td>
                        <html:submit property="operation" styleClass="inputbutton">
                        	<bean:message key="lable.chooseRoom"/>
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:reset value="Limpar" styleClass="inputbutton">
                            <bean:message key="label.clear"/>
                        </html:reset>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>
