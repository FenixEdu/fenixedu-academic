<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.editTurno"/> </b> </font></center>
        <br/>
        <br/>
        <span class="error"><html:errors/></span>
        <br/>
        <html:form action="/editarTurnoForm">
            <table align="center" cellspacing="10">
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.turno.name"/>
                    </td>
                    <td align="left" height="40">
                        <html:text property="nome" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.turno.capacity"/>
                    </td>
                    <td align="left" height="40">
                        <html:text property="lotacao" size="11" maxlength="20"/>
                    </td>
                </tr>
            </table>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:link page="/prepararEditarAulasDeTurno.do"> <bean:message key="link.add.remove.aulas"/> </html:link>
                    </td>
                </tr>
            </table>
            <br/>
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


