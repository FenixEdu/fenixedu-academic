<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<h2><bean:message key="title.search.rooms"/></h2>
<br/>
	<table width="100%">
		<tr>
			<td class="infoop"><bean:message key="message.search.rooms" />
			</td>
		</tr>
	</table>
        <html:errors/>
        <html:form action="/chooseRoomsForm" method="GET">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="tall-td">
                        <bean:message key="property.room.name"/>
                    </td>
                    <td>
                        <html:text property="name" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td class="tall-td">
                        <bean:message key="property.room.building"/>
                    </td>
                    <td>
                        <html:select property="building" size="1">
                            <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td class="tall-td">
                        <bean:message key="property.room.floor"/>
                    </td>
                    <td>
                        <html:text property="floor" size="2" maxlength="2"/>
                    </td>
                </tr>
                <tr>
                    <td class="tall-td">
                        <bean:message key="property.room.type"/>
                    </td>
                    <td>
                        <html:select property="type" size="1">
                         <html:options collection="publico.types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td class="tall-td">
                        <bean:message key="property.room.capacity.normal"/>
                    </td>
                    <td>
                        <html:text property="capacityNormal" size="3" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td class="tall-td">
                        <bean:message key="property.room.capacity.exame"/>
                    </td>
                    <td height="0">
                        <html:text property="capacityExame" size="3" maxlength="4"/>
                    </td>
                </tr>
            </table>
           
            <br/>
                        <html:submit styleClass="inputbutton">
                            <bean:message key="label.submit"/>
                        </html:submit>
                        <html:reset styleClass="inputbutton">
                            <bean:message key="label.clear"/>
                        </html:reset>
        </html:form>