<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
      <h2><bean:message key="title.search.empty.rooms"/></h2>
        <br/>
        <span class="error"><html:errors/></span>
        <html:form action="/searchEmptyRoomsDA">
        	<html:hidden property="page" value="1"/>
        	<input type="hidden" name="method" value="doSearch"/>
            <table cellspacing="0" cellpadding="0" border="0">
	            <tr>
                    <td nowrap class="formTD">
                        <bean:message key="property.executionPeriod"/>
                    </td>
                    <td nowrap class="formTD">
						<html:select property="executionPeriodIndex" size="1">
					 		<html:options property="value"
					 		              labelProperty="label" 
 	  									  collection="<%= SessionConstants.EXECUTION_PERIOD_LIST%>" />
						</html:select>
                   </td>
                </tr>                
	            <tr>
                    <td nowrap class="formTD">
                        <bean:message key="property.aula.weekDay"/>
                    </td>
                    <td nowrap class="formTD">
                        <html:select property="weekDay"  size="1">
                            <!-- TODO : Change query to allow wildcards with weekdays (all weekdays)-->
                        	<option value="1" selected="selected">Dia da Semana</option>
                            <html:options collection="weekDays" property="value" labelProperty="label"/>
                        </html:select>
                   </td>
                </tr>                
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.aula.time.begining"/>
                    </td>
                    <td nowrap="nowrap">
                        <html:select property="startHour"  size="1">
                        	<option value="" selected="selected">[Horas]</option>                        
                            <html:options name="hours"/>
                        </html:select> :
                        <html:select property="startMinutes" size="1">
                        	<option value="" selected="selected">[Minutos]</option>                        
                            <html:options name="minutes"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.aula.time.end"/>
                    </td>
                    <td nowrap="nowrap">
                        <html:select property="endHour"  size="1">
                        	<option value="" selected="selected">[Horas]</option>                        
                            <html:options name="hours"/>
                        </html:select> :
                        <html:select property="endMinutes"  size="1">
                        	<option value="" selected="selected">[Minutos]</option>                        
                            <html:options name="minutes"/>
                        </html:select>
                    </td> 
                </tr> 
                <tr>
                    <td nowrap="nowrap" class="formTD">
                        <bean:message key="property.room.capacity.normal"/>:
                    </td>
                    <td nowrap="nowrap" class="formTD">
                        <html:text property="normalCapacity" size="3" maxlength="4"/>
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