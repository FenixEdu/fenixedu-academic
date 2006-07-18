<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

        <center>
            <font color='#034D7A' size='5'> <b> Ver Sala </b> </font>
        </center>
        <br/>
        <table align="center" cellspacing="10">
            <tr>
                <td align="right" height="40">
                    Nome :
                </td>
                <td align="left" height="40">
                    <b>
                        <bean:write name="salaFormBean" property="nome"
                                    scope="request" filter="true"/>
                    </b>
                </td>
            </tr>
            <tr>
                <td align="right" height="40">
                    Edificio :
                </td>
                <td align="left" height="40">
                    <b>
                        <bean:write name="salaFormBean" property="edificio"
                                    scope="request" filter="true"/>
                    </b>
                </td>
            </tr>
            <tr>
                <td align="right" height="40">
                    Piso :
                </td>
                <td align="left" height="40">
                    <b>
                        <bean:write name="salaFormBean" property="piso"
                                    scope="request" filter="true"/>
                    </b>
                </td>
            </tr>
            <tr>
                <td align="right" height="40">
                    Tipo :
                </td>
                <td align="left" height="40">
                    <b>
                        <bean:write name="salaFormBean" property="tipo"
                                    scope="request" filter="true"/>
                    </b>
                </td>   
            </tr>
            <tr>
                <td align="right" height="40">
                    Capacidade Normal :
                </td>
                <td align="left" height="40">
                    <b>
                        <bean:write name="salaFormBean" property="capacidadeNormal"
                                    scope="request" filter="true"/>
                    </b>
                </td>   
            </tr>
            <tr>
                <td align="right" height="40">
                    Capacidade Exame :
                </td>
                <td align="left" height="40">
                    <b>
                        <bean:write name="salaFormBean" property="capacidadeExame"
                                    scope="request" filter="true"/>
                    </b>
                </td>   
            </tr>
        </table>
        <br/>
        <table align="center">
            <tr align="center">
                <td>
                    <html:link page="/viewRoomOcupation.do"> Ver Ocupação </html:link>
                </td>
            </tr>
        </table>
        <br/>

