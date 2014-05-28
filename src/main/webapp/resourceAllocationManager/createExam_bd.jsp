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
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>O curso seleccionado
        	&eacute;:</p>
			<strong><jsp:include page="contextNotSelectable.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>
<h2><bean:message key="title.exam.create"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/createExam">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

		<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

		<logic:present name="nextPage">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.nextPage" property="nextPage"
						 value="<%= pageContext.findAttribute(PresentationConstants.NEXT_PAGE).toString() %>"/>
		</logic:present>

	<logic:iterate id="year" name="<%= PresentationConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:equal name="year" value="1">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_1 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_1 %>"
						 value="1"/>
		</logic:equal>
		<logic:equal name="year" value="2">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_2 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_2 %>"
						 value="2"/>
		</logic:equal>
		<logic:equal name="year" value="3">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_3 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_3 %>"
						 value="3"/>
		</logic:equal>
		<logic:equal name="year" value="4">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_4 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_4 %>"
						 value="4"/>
		</logic:equal>
		<logic:equal name="year" value="5">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_5 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_5 %>"
						 value="5"/>
		</logic:equal>
	</logic:iterate>

	<table cellpadding="0" cellspacing="2">
    	<tr>
        	<td nowrap="nowrap" class="formTD">
            	<bean:message key="property.exam.year"/>:
            </td>
            <td nowrap="nowrap" class="formTD">
            	<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/>
            </td>
            <td nowrap="nowrap" class="formTD">
            	<bean:message key="property.exam.month"/>:
            </td>
            <td nowrap="nowrap" class="formTD">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.month" property="month">
		            <option value="" selected="selected"></option>
		            <html:options collection="<%= PresentationConstants.LABLELIST_MONTHSOFYEAR %>" property="value" labelProperty="label"/>
	            </html:select>
            </td>
            <td nowrap="nowrap" class="formTD">
            	<bean:message key="property.exam.day"/>:
            </td>
            <td nowrap="nowrap" class="formTD">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.day" property="day">
                	<option value="" selected="selected"></option>
                    <html:options name="<%= PresentationConstants.LABLELIST_DAYSOFMONTH %>"/>
                </html:select>
            </td>
		</tr>
        <tr>
            <td nowrap="nowrap" class="formTD">
                <bean:message key="property.exam.beginning"/>:
            </td>
            <td nowrap="nowrap">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.beginning" property="beginning">
                  	<option value="" selected="selected"></option>                        
                    <html:options name="<%= PresentationConstants.LABLELIST_HOURS %>"/>
                </html:select>
            </td>
       	</tr>
        <tr>
            <td nowrap="nowrap" class="formTD">
                <bean:message key="property.exam.season"/>:
            </td>
            <td nowrap="nowrap">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.season" property="season">
                  	<option value="" selected="selected"></option>           
		            <html:options collection="<%= PresentationConstants.LABLELIST_SEASONS %>" property="value" labelProperty="label"/>                  	             
                </html:select>
            </td>
       	</tr>
	</table>
	<br/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="label.create"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>
</html:form>