<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
        <h2><bean:message key="title.exams"/></h2>
        <span class="error"><html:errors/></span>
        <html:form action="/chooseDayAndShift">
			<input type="hidden" name="method" value="choose"/>
            <!--<html:hidden property="method" value="choose"/>-->
			<input type="hidden" name="page" value="1"/>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td nowrap class="formTD">
                        <bean:message key="property.exam.year"/>
                    </td>
                    <td nowrap class="formTD">
                        <html:select property="year"  size="4">
                        	<option value="" selected="selected">[Ano]</option>
                            <html:options name="years"/>
                        </html:select>
                   </td>
                    <td nowrap class="formTD">
                        <bean:message key="property.exam.month"/>
                    </td>
                    <td nowrap class="formTD">
                        <html:select property="month"  size="3">
                        	<option value="" selected="selected">[Mês]</option>
                            <html:options name="monthsOfYear"/>
                        </html:select>
                   </td>
                    <td nowrap class="formTD">
                        <bean:message key="property.exam.year"/>
                    </td>
                    <td nowrap class="formTD">
                        <html:select property="day"  size="3">
                        	<option value="" selected="selected">[Dia]</option>
                            <html:options name="daysOfMonth"/>
                        </html:select>
                   </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.exam.beginning"/>
                    </td>
                    <td nowrap="nowrap">
                        <html:select property="beginning"  size="5">
                        	<option value="" selected="selected">[Turno]</option>                        
                            <html:options name="horas"/>
                        </html:select>
                    </td>
                </tr>
          </table>
            <br/>
            <table align="lef">
                <tr align="center">
                    <td>
                        <html:submit styleClass="inputbutton">
                        	<bean:message key="lable.choose"/>
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
