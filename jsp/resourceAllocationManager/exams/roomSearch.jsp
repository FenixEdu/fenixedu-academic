<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<html:form action="/roomSearch" focus="day">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<em><bean:message key="link.writtenEvaluationManagement"/></em>
	<h2><bean:message key="link.exams.searchAvailableRooms"/></h2>

	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

	<table class="tstyle5 thlight thright">
		<tr>
			<th>
			   	<bean:message key="property.exam.date"/>:
			</th>
			<td>
   				<html:text bundle="HTMLALT_RESOURCES" altKey="text.day" maxlength="2" size="2" property="day"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.month" maxlength="2" size="2" property="month"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" maxlength="4" size="4" property="year"/>
			   	(ex: 24/12/2004)
			</td>
		</tr>
		<tr>
			<th>
			    <bean:message key="property.exam.beginning"/>:
			</th>
			<td>
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginningHour" maxlength="2" size="2" property="beginningHour"/>
   				:
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginningMinute" maxlength="2" size="2" property="beginningMinute"/>
			</td>
		</tr>
		<tr>
			<th>
    			<bean:message key="property.exam.end"/>:
			</th>
			<td>
			   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.endHour" maxlength="2" size="2" property="endHour"/>
			   	:
			   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.endMinute" maxlength="2" size="2" property="endMinute"/>
			</td>
		</tr>
		<tr>
			<th>
    			<bean:message key="property.room.capacity.normal"/>:			
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.normal" maxlength="3" size="3" property="normal"/> (facultativo)
			</td>
		</tr>
		<tr>
			<th>
    			<bean:message key="property.room.capacity.exame"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.exam" maxlength="3" size="3" property="exam"/> (facultativo)
			</td>
		</tr>
	</table>

	<p>	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="lable.choose"/>
		</html:submit>
	</p>
	
<%--
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" value="Cancelar" styleClass="inputbutton">
		<bean:message key="label.cancel"/>
	</html:cancel>
--%>
</html:form>	