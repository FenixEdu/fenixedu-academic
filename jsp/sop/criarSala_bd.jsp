<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
      <h2><bean:message key="title.createRoom"/></h2>
        <br/>
        <span class="error"><html:errors/></span>
        <html:form action="/criarSalaForm">
            <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.name"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text property="name" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.building"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:select property="building" size="1">
                            <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.floor"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text property="floor" size="2" maxlength="2"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.type"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:select property="type" size="1">
                            <html:options collection="publico.types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.capacity.normal"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text property="capacityNormal" size="3" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.capacity.exame"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text property="capacityExame" size="3" maxlength="4"/>
                    </td>
                </tr>
            </table>
            <br/>
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <html:submit value="Submeter" styleClass="inputbutton">
                            <bean:message key="label.save"/>
                        </html:submit>
                    </td>
                    <td width="10"></td>
                    <td>
                        <html:reset value="Limpar" styleClass="inputbutton">
                            <bean:message key="label.clear"/>
                        </html:reset>
                    </td>
                </tr>
            </table>
        </html:form>