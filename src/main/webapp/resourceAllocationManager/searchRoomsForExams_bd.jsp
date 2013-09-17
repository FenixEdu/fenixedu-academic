<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2><bean:message key="title.chooseRooms"/></h2>
<html:errors/>
        <html:form action="/chooseRoomToViewForExams">
            <table>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.room.name"/>
                    </td>
                    <td>
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.room.building"/>
                    </td>
                    <td>
                        <html:select bundle="HTMLALT_RESOURCES" altKey="select.building" property="building" size="1">
                            <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.room.floor"/>
                    </td>
                    <td>
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" property="floor" size="2" maxlength="2"/>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.room.type"/>
                    </td>
                    <td>
                        <html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type" size="1">
                            <html:options collection="publico.types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.room.capacity.normal"/>
                    </td>
                    <td>
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityNormal" property="capacityNormal" size="3" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.room.capacity.exame"/>
                    </td>
                    <td>
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityExame" property="capacityExame" size="3" maxlength="4"/>
                    </td>
                </tr>
            </table>
            <br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.choose"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
</html:form>
