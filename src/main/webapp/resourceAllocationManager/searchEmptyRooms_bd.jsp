<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<em><bean:message key="title.manage.rooms"/></em>
<h2><bean:message key="title.search.empty.rooms"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/searchEmptyRoomsDA">
	<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
    	<input alt="input.method" type="hidden" name="method" value="doSearch"/>
    	
   	<table class="tstyle5 thlight thright">
<!--
		<tr>
        	<td nowrap class="formTD"><bean:message key="property.executionPeriod"/></td>
          	<td nowrap class="formTD">
          		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodIndex" property="executionPeriodIndex" size="1">
					<html:options property="value" labelProperty="label" collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>" />
				</html:select></td>
      	</tr>                
-->
       	<!-- added by rspl -->
	    <tr>
        	<th> De:</th>
            <td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startDay" maxlength="2" size="2" property="startDay"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.startMonth" maxlength="2" size="2" property="startMonth"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.startYear" maxlength="4" size="4" property="startYear"/>                        	
			    &nbsp;a&nbsp;
   				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endDay" maxlength="2" size="2" property="endDay"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.endMonth" maxlength="2" size="2" property="endMonth"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.endYear" maxlength="4" size="4" property="endYear"/>
			</td>
      	</tr>
	    <tr>
        	<th><bean:message key="property.aula.weekDay"/>:</th>
        	<td>
     			<html:select bundle="HTMLALT_RESOURCES" property="weekDay"  size="1">
                <!-- TODO : Change query to allow wildcards with weekdays (all weekdays)-->
                	<option value="1" selected="selected"><!--  w3c Complient --></option>
                    <html:options collection="weekDays" property="value" labelProperty="label"/>
            	</html:select>
           	</td>
       	</tr>                
        <tr>
        	<th><bean:message key="property.aula.time.begining"/>:</th>
            <td>
            	<html:select bundle="HTMLALT_RESOURCES" property="startHour"  size="1">
                	<option value="" selected="selected"><!--  w3c Complient --></option>                        
                    <html:options name="hours"/>
               	</html:select> :
                <html:select bundle="HTMLALT_RESOURCES" property="startMinutes" size="1">
                	<option value="" selected="selected"><!--  w3c Complient --></option>                        
                	<html:options name="minutes"/>
                </html:select>
           	</td>
      	</tr>
        <tr>
        	<th><bean:message key="property.aula.time.end"/>:</th>
           	<td>
            	<html:select bundle="HTMLALT_RESOURCES" property="endHour" size="1">
                	<option value="" selected="selected"><!--  w3c Complient --></option>                        
                  	<html:options name="hours"/>
               	</html:select> :
               	<html:select bundle="HTMLALT_RESOURCES" property="endMinutes" size="1">
                	<option value="" selected="selected"><!--  w3c Complient --></option>                        
                    <html:options name="minutes"/>
               	</html:select>
          	</td> 
        </tr> 
        <tr>
        	<th><bean:message key="property.room.capacity.normal"/>:</th>
           	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.normalCapacity" property="normalCapacity" size="3" maxlength="4"/></td>
       	</tr>
	</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
		<bean:message key="label.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</p>

</html:form>