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
      <h2><bean:message key="title.createRoom"/></h2>
        <br/>
        <span class="error"><!-- Error messages go here --><html:errors /></span>
        <html:form action="/criarSalaForm">
            <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.name"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.building"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:select bundle="HTMLALT_RESOURCES" property="building" size="1">
                            <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.floor"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" property="floor" size="2" maxlength="2"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.type"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:select bundle="HTMLALT_RESOURCES" property="type" size="1">
                            <html:options collection="publico.types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.capacity.normal"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityNormal" property="capacityNormal" size="3" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.capacity.exame"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityExame" property="capacityExame" size="3" maxlength="4"/>
                    </td>
                </tr>
            </table>
            <br/>
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
                            <bean:message key="label.save"/>
                        </html:submit>
                    </td>
                    <td width="10"></td>
                    <td>
                        <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
                            <bean:message key="label.clear"/>
                        </html:reset>
                    </td>
                </tr>
            </table>
        </html:form>