<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<br />
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exam" name="component" property="infoExam"/>
<h2><bean:message key="label.exam.information"/></h2>
<table>
<tr>
	<td class="listClasses-header" ><bean:message key="label.season"/></td>
	<td class="listClasses-header" ><bean:message key="label.day"/></td>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
</tr>
<tr>
	<td class="listClasses"><bean:write name="exam" property="season"/></td>
	<td class="listClasses"><bean:write name="exam" property="date"/></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>		
</tr>
</table>
<br/>

<h2> <bean:message key="label.exam.enrollment.period"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/examEnrollmentEditionManager" >
	
<logic:empty name="component" property="infoExamEnrollment">	
<html:hidden property="method" value="insertExamEnrollment"/>

<h3><bean:message key="label.begin.exam.enrollment"/></h3>

<table>
<tr>
	<td class="listClasses-header"><bean:message key="label.day"/></td>
	<td class="listClasses-header"><bean:message key="label.month"/></td>
	<td class="listClasses-header"><bean:message key="label.exam.enrollment.year"/></td>
	<td></td>
	<td class="listClasses-header"><bean:message key="label.hour"/></td>
	<td class="listClasses-header"><bean:message key="label.minutes"/></td>	
</tr>	
<tr>
	<td class="listClasses"><html:select property="beginDay">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="days"/>
                   	</html:select>
	</td>
	<td class="listClasses"><html:select property="beginMonth">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="months"/>
                   	</html:select></td>
	<td class="listClasses"><html:select property="beginYear">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="years"/>
                   	</html:select></td>
                   	<td></td>
	<td class="listClasses"><html:select property="beginHour">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="hours"/>
                   	</html:select></td>
	<td class="listClasses"><html:select property="beginMinutes">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="minutes"/>
                   	</html:select></td>	
</tr>	
</table>
<br/>
<h3><bean:message key="label.end.exam.enrollment"/></h3>
<table>
	
<tr>
	
	<td class="listClasses-header"><bean:message key="label.day"/></td>
	<td class="listClasses-header"><bean:message key="label.month"/></td>
	<td class="listClasses-header"><bean:message key="label.exam.enrollment.year"/></td>
	<td></td>
	<td class="listClasses-header"><bean:message key="label.hour"/></td>
	<td class="listClasses-header"><bean:message key="label.minutes"/></td>	
</tr>	
<tr>
	<td class="listClasses"><html:select property="endDay">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="days"/>
                   	</html:select>
	</td>
	<td class="listClasses"><html:select property="endMonth">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="months"/>
                   	</html:select></td>
	<td class="listClasses"><html:select property="endYear">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="years"/>
                   	</html:select></td>
                   	<td></td>
	<td class="listClasses"><html:select property="endHour">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="hours"/>
                   	</html:select></td>
	<td class="listClasses"><html:select property="endMinutes">
                   <option value="" selected="selected"></option>
                    <html:options name="component" property="minutes"/>
                   	</html:select></td>	
</tr>	
</table>
</logic:empty>

<logic:notEmpty name="component" property="infoExamEnrollment">	
<html:hidden property="method" value="editExamEnrollment"/>
<bean:define id="examEnrollment" name="component" property="infoExamEnrollment"/>
<h3><bean:message key="label.begin.exam.enrollment"/></h3>

<table>
<tr>
	<td class="listClasses-header"><bean:message key="label.day"/></td>
	<td class="listClasses-header"><bean:message key="label.month"/></td>
	<td class="listClasses-header"><bean:message key="label.exam.enrollment.year"/></td>
	<td></td>
	<td class="listClasses-header"><bean:message key="label.hour"/></td>
	<td class="listClasses-header"><bean:message key="label.minutes"/></td>	
</tr>	
<tr>	<bean:define id="beginDay" name="examEnrollment" property="beginDay" />
	<td class="listClasses"><html:select property="beginDay">
                   <option value="<%= beginDay %>" selected="selected"><bean:write name="beginDay"/></option>
                    <html:options name="component" property="days"/>
                   	</html:select>
	</td>
		<bean:define id="beginMonth" name="examEnrollment" property="beginMonth" />
	<td class="listClasses"><html:select property="beginMonth">
                   <option value="<%= beginMonth %>" selected="selected"><bean:write name="beginMonth"/></option>
                    <html:options name="component" property="months"/>
                   	</html:select></td>
          <bean:define id="beginYear" name="examEnrollment" property="beginYear" />
	<td class="listClasses"><html:select property="beginYear">
                   <option value="<%= beginYear %>" selected="selected"><bean:write name="beginYear"/></option>
                    <html:options name="component" property="years"/>
                   	</html:select></td>
                   	<td></td>
             <bean:define id="beginHour" name="examEnrollment" property="beginHour"/>      	
	<td class="listClasses"><html:select property="beginHour">
                   <option value="<%= beginHour %>" selected="selected"><bean:write name="examEnrollment" property="beginHourString"/></option>
                    <html:options name="component" property="hours"/>
                   	</html:select></td>
                   <bean:define id="beginMinutes" name="examEnrollment" property="beginMinutes"	/>
	<td class="listClasses"><html:select property="beginMinutes">
                   <option value="<%= beginMinutes %>" selected="selected"><bean:write name="examEnrollment" property="beginMinutesString"/></option>
                    <html:options name="component" property="minutes"/>
                   	</html:select></td>	
</tr>	
</table>
<br/>
<h3><bean:message key="label.end.exam.enrollment"/></h3>
<table>
	
<tr>
	
	<td class="listClasses-header"><bean:message key="label.day"/></td>
	<td class="listClasses-header"><bean:message key="label.month"/></td>
	<td class="listClasses-header"><bean:message key="label.exam.enrollment.year"/></td>
	<td></td>
	<td class="listClasses-header"><bean:message key="label.hour"/></td>
	<td class="listClasses-header"><bean:message key="label.minutes"/></td>	
</tr>	
<tr> <bean:define id="endDay" name="examEnrollment" property="endDay"	/>
	<td class="listClasses"><html:select property="endDay">
                   <option value="<%= endDay%>" selected="selected"><bean:write name="endDay"/></option>
                    <html:options name="component" property="days"/>
                   	</html:select>
	</td> <bean:define id="endMonth" name="examEnrollment" property="endMonth"	/>
	<td class="listClasses"><html:select property="endMonth">
                   <option value="<%= endMonth%>" selected="selected"><bean:write name="endMonth"/></option>
                    <html:options name="component" property="months"/>
                   	</html:select></td>
             <bean:define id="endYear" name="examEnrollment" property="endYear"	/>       	
	<td class="listClasses"><html:select property="endYear">
                   <option value="<%= endYear%>" selected="selected"><bean:write name="endYear"/></option>
                    <html:options name="component" property="years"/>
                   	</html:select></td>
                   	<td></td>
                   	 <bean:define id="endHour" name="examEnrollment" property="endHour"	/>
	<td class="listClasses"><html:select property="endHour">
                   <option value="<%= endHour%>" selected="selected"><bean:write name="examEnrollment" property="endHourString"/></option>
                    <html:options name="component" property="hours"/>
                   	</html:select></td>
                   	 <bean:define id="endMinutes" name="examEnrollment" property="endMinutes"	/>
	<td class="listClasses"><html:select property="endMinutes">
                   <option value="<%= endMinutes %>" selected="selected"><bean:write name="examEnrollment" property="endMinutesString"/></option>
                    <html:options name="component" property="minutes"/>
                   	</html:select></td>	
</tr>	
</table>
<bean:define id="examEnrollmentCode" name="examEnrollment" property="idInternal"/>
<html:hidden property="examEnrollmentCode" value="<%= pageContext.findAttribute("examEnrollmentCode").toString() %>" />	
</logic:notEmpty>
<br/>
<br/>

<html:hidden property="examCode" value="<%= pageContext.findAttribute("examCode").toString() %>" />
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>
</html:form>






