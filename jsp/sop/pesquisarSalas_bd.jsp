<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.chooseRooms"/> <b> </font></center>
        <br/>
        <html:errors/>
        <html:form action="/pesquisarSala">
            <table align="center" cellspacing="10">
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.name"/>
                    </td>
                    <td align="left" height="40">
                        <html:text property="name" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.building"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="building" size="1">
                            <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.floor"/>
                    </td>
                    <td align="left" height="40">
                        <html:text property="floor" size="2" maxlength="2"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.type"/>
                    </td>
                    <td align="left" height="40">
                        <html:select property="type" size="1">
                            <html:options collection="publico.types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.capacity.normal"/>
                    </td>
                    <td align="left" height="40">
                        <html:text property="capacityNormal" size="3" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.capacity.exame"/>
                    </td>
                    <td align="left" height="40">
                        <html:text property="capacityExame" size="3" maxlength="4"/>
                    </td>
                </tr>
            </table>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit>
                            <bean:message key="label.choose"/>
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
