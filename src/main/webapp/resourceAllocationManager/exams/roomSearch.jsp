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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>

<html:form action="/roomSearch" focus="day">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

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