<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>O curso seleccionado
        	&eacute;:</p>
			<strong><jsp:include page="contextNotSelectable.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>
<h2><bean:message key="title.exam.create"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/createExam">
	<html:hidden property="page" value="1"/>

		<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

		<logic:present name="nextPage">
			<html:hidden property="nextPage"
						 value="<%= pageContext.findAttribute(SessionConstants.NEXT_PAGE).toString() %>"/>
		</logic:present>

	<logic:iterate id="year" name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:equal name="year" value="1">
			<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_1 %>"
						 value="1"/>
		</logic:equal>
		<logic:equal name="year" value="2">
			<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_2 %>"
						 value="2"/>
		</logic:equal>
		<logic:equal name="year" value="3">
			<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_3 %>"
						 value="3"/>
		</logic:equal>
		<logic:equal name="year" value="4">
			<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_4 %>"
						 value="4"/>
		</logic:equal>
		<logic:equal name="year" value="5">
			<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_5 %>"
						 value="5"/>
		</logic:equal>
	</logic:iterate>

	<table cellpadding="0" cellspacing="2">
    	<tr>
        	<td nowrap="nowrap" class="formTD">
            	<bean:message key="property.exam.year"/>:
            </td>
            <td nowrap="nowrap" class="formTD">
            	<html:text property="year"/>
            </td>
            <td nowrap="nowrap" class="formTD">
            	<bean:message key="property.exam.month"/>:
            </td>
            <td nowrap="nowrap" class="formTD">
            	<html:select property="month">
		            <option value="" selected="selected"></option>
		            <html:options collection="<%= SessionConstants.LABLELIST_MONTHSOFYEAR %>" property="value" labelProperty="label"/>
	            </html:select>
            </td>
            <td nowrap="nowrap" class="formTD">
            	<bean:message key="property.exam.day"/>:
            </td>
            <td nowrap="nowrap" class="formTD">
            	<html:select property="day">
                	<option value="" selected="selected"></option>
                    <html:options name="<%= SessionConstants.LABLELIST_DAYSOFMONTH %>"/>
                </html:select>
            </td>
		</tr>
        <tr>
            <td nowrap="nowrap" class="formTD">
                <bean:message key="property.exam.beginning"/>:
            </td>
            <td nowrap="nowrap">
            	<html:select property="beginning">
                  	<option value="" selected="selected"></option>                        
                    <html:options name="<%= SessionConstants.LABLELIST_HOURS %>"/>
                </html:select>
            </td>
       	</tr>
        <tr>
            <td nowrap="nowrap" class="formTD">
                <bean:message key="property.exam.season"/>:
            </td>
            <td nowrap="nowrap">
            	<html:select property="season">
                  	<option value="" selected="selected"></option>           
		            <html:options collection="<%= SessionConstants.LABLELIST_SEASONS %>" property="value" labelProperty="label"/>                  	             
                </html:select>
            </td>
       	</tr>
	</table>
	<br/>
<html:hidden property="method" value="create"/>
<html:submit styleClass="inputbutton">
	<bean:message key="label.create"/>
</html:submit>
<html:reset value="Limpar" styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>
</html:form>