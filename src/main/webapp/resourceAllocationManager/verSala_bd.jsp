<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

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

