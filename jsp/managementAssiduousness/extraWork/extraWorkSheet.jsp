<%@page contentType="text/html"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="Util.DiaSemana" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<h2><bean:message key="titulo.extraWork.authorization" /><br />
<bean:message key="label.moth" />:&nbsp;<bean:write name="moth" />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="label.year" />:&nbsp;<bean:write name="year" />
</h2>

<html:form action="/extraWorkByEmployee">
	<html:hidden property="method" value="prepareInputs"/>
	<html:hidden property="page" value="0"/>
	<bean:size id="sizeList" name="infoExtraWorkList" />		
<table>
    <tr>
      <td> 
        <span class="error"><html:errors/></span>
      </td>
    </tr>
    <tr>
 	  	<td class="listClasses-header" width="1%" nowrap><bean:message key="label.employee"/></td>	
        <td>
	      	<bean:write name="infoEmployee" property="employeeNumber" />	
	      	<bean:define id="employee" name="infoEmployee" property="employeeNumber" />	      	
			<html:hidden property="employeeNumber" value="<%= employee.toString() %>" />
	    </td>
	</tr>
    <tr>
    	<td class="listClasses-header" width="20%" nowrap><bean:message key="prompt.nome"/></td>	
		<td>
	    	<bean:write name="infoEmployee" property="person.nome" />
	    </td>
	</tr>
    <tr>
    	<td class="listClasses-header" width="20%" nowrap><bean:message key="label.code.cost.center"/></td>	
		<td><bean:write name="infoEmployee" property="workingPlaceInfoCostCenter.code"  />
          <bean:write name="infoEmployee" property="workingPlaceInfoCostCenter.departament" /><br>
          <bean:write name="infoEmployee" property="workingPlaceInfoCostCenter.section1" /><br>
          <bean:write name="infoEmployee" property="workingPlaceInfoCostCenter.section2" /><br>           
      </td>
    </tr>
    <tr><td colspan="2"><br /></td></tr>    
</table>
<table border="0">
	<tr>
		<td class="listClasses-header" colspan="2"><bean:message key="label.day" /></td>
		<td class="listClasses-header" colspan="2"><bean:message key="label.period" /></td>
		<td class="listClasses-header"><bean:message key="prompt.saldoHN" /></td>
		<td class="listClasses-header"><bean:message key="label.lunch" /></td>
		<td class="listClasses-header" colspan="2"><bean:message key="label.diurnal" /></td>
		<td class="listClasses-header" colspan="2"><bean:message key="label.nocturnal" /></td>
		<td class="listClasses-header"><bean:message key="label.weekEnd.holiday" /></td>
	<tr>
	<tr>
		<td class="listClasses-header"><bean:message key="label.moth" /></td>
		<td class="listClasses-header"><bean:message key="label.week" /></td>
		<td class="listClasses-header"><bean:message key="label.begin" /></td>
		<td class="listClasses-header"><bean:message key="label.end" /></td>
		<td class="listClasses-header">&nbsp;</td>
		<td class="listClasses-header">&nbsp;</td>
		<td class="listClasses-header"><bean:message key="label.first.hour" /></td>
		<td class="listClasses-header"><bean:message key="label.more.second.hour" /></td>
		<td class="listClasses-header"><bean:message key="label.first.hour" /></td>
		<td class="listClasses-header"><bean:message key="label.more.second.hour" /></td>
		<td class="listClasses-header"><bean:message key="label.200.percentage" /></td>
	<tr>
	
	<% 
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		simpleDateFormat.setLenient(false); 
				
		SimpleDateFormat simpleDayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		simpleDayDateFormat(false); 
	%>
	<logic:iterate id="infoExtraWork" name="infoExtraWorkList" indexId="indice" type="DataBeans.managementAssiduousness.InfoExtraWork">		
		<bean:define id="isPar">
			<%= indice.intValue() % 2  %>
		</bean:define>
		<logic:equal name="isPar" value="0"><tr class="listClassesWhite"></logic:equal>
		<logic:notEqual name="isPar" value="0"><tr class="listClasses"></logic:notEqual>
			<bean:define id="day" name="infoExtraWork" property="day" type="java.util.Date"/>
			<% 
				Calendar calendar = Calendar.getInstance(); 
			   	calendar.setTimeInMillis(day.getTime()); 
			%>
			<html:hidden name="infoExtraWork" property="day" value="<%= simpleDayDateFormat.format(day)%>" indexed="true" />
			<td rowspan="2"><!-- Dia do Mês -->
				<%= calendar.get(Calendar.DAY_OF_MONTH) %>
			</td>			
			<td rowspan="2"><!-- Dia da Semana -->
				<%= new DiaSemana(calendar.get(Calendar.DAY_OF_WEEK)).toString() %>
			</td>
			<td rowspan="2"><!-- Início do TE -->&nbsp;				
				<logic:notEmpty name="infoExtraWork" property="beginHour" >
					<bean:define id="beginHour" name="infoExtraWork" property="beginHour" type="java.util.Date"/>
					<%= simpleDateFormat.format(beginHour)	%>
					<html:hidden name="infoExtraWork" property="beginHour" value="<%= simpleDateFormat.format(beginHour)%>" indexed="true" />
				</logic:notEmpty>
			</td>
			<td rowspan="2"><!-- Início do TE -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="endHour" >
					<bean:define id="endHour" name="infoExtraWork" property="endHour" type="java.util.Date"/>
					<%= simpleDateFormat.format(endHour)	%>
					<html:hidden name="infoExtraWork" property="endHour" value="<%= simpleDateFormat.format(endHour)%>" indexed="true" />
				</logic:notEmpty>
			</td>
			<td rowspan="2"><!-- Saldo -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="totalExtraWork" >
					<bean:define id="totalExtraWork" name="infoExtraWork" property="totalExtraWork" type="java.util.Date"/>
					<%= simpleDateFormat.format(totalExtraWork)	%>
				</logic:notEmpty>
			</td>
			<td><!-- Subsidio do Almoço -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="mealSubsidy" >
					<bean:define id="mealSubsidy" name="infoExtraWork" property="mealSubsidy" type="java.util.Date"/>
					<bean:write name="infoExtraWork" property="mealSubsidy" />
				</logic:notEmpty>
			</td>
			<td><!-- 1ª hora Diurna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="diurnalFirstHour" >
					<bean:define id="diurnalFirstHour" name="infoExtraWork" property="diurnalFirstHour" type="java.util.Date"/>
					<%= simpleDateFormat.format(diurnalFirstHour)	%>
				</logic:notEmpty>
			</td>
			<td><!-- 2ª hora Diurna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="diurnalAfterSecondHour" >
					<bean:define id="diurnalAfterSecondHour" name="infoExtraWork" property="diurnalAfterSecondHour" type="java.util.Date"/>
					<%= simpleDateFormat.format(diurnalAfterSecondHour)	%>
				</logic:notEmpty>
			</td>
			<td><!-- 1ª hora Nocturna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="nocturnalFirstHour" >
					<bean:define id="nocturnalFirstHour" name="infoExtraWork" property="nocturnalFirstHour" type="java.util.Date"/>
					<%= simpleDateFormat.format(nocturnalFirstHour)	%>
				</logic:notEmpty>
			</td>
			<td><!-- 2ª hora Nocturna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="nocturnalAfterSecondHour" >
					<bean:define id="nocturnalAfterSecondHour" name="infoExtraWork" property="nocturnalAfterSecondHour" type="java.util.Date"/>
					<%= simpleDateFormat.format(nocturnalAfterSecondHour)	%>
				</logic:notEmpty>	
			</td>			
			<td><!-- Fim de semana e Feriados -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="restDay" >
					<bean:define id="restDay" name="infoExtraWork" property="restDay" type="java.util.Date"/>
					<%= simpleDateFormat.format(restDay)	%>
				</logic:notEmpty>		
			</td>
		</tr>			
		<logic:equal name="isPar" value="0"><tr class="listClassesWhite"></logic:equal>
		<logic:notEqual name="isPar" value="0"><tr class="listClasses"></logic:notEqual>
			<td><!-- Subsidio do Almoço -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="mealSubsidy" >
					<html:hidden name="infoExtraWork" property="mealSubsidy" indexed="true" />
					<html:checkbox name="infoExtraWork" property="mealSubsidyAuthorized" value="true" indexed="true" />
				</logic:notEmpty>
			</td>
			<td><!-- 1ª hora Diurna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="diurnalFirstHour" >
					<html:hidden name="infoExtraWork" property="diurnalFirstHour" indexed="true" />				
					<html:checkbox name="infoExtraWork" property="diurnalFirstHourAuthorized" value="true" indexed="true" />				
				</logic:notEmpty>
			</td>
			<td><!-- 2ª hora Diurna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="diurnalAfterSecondHour" >
					<html:hidden name="infoExtraWork" property="diurnalAfterSecondHour" indexed="true" />
					<html:checkbox name="infoExtraWork" property="diurnalAfterSecondHourAuthorized" value="true" indexed="true" />					
				</logic:notEmpty>
			</td>
			<td><!-- 1ª hora Nocturna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="nocturnalFirstHour" >
					<html:hidden name="infoExtraWork" property="nocturnalFirstHour" indexed="true" />				
					<html:checkbox name="infoExtraWork" property="nocturnalFirstHourAuthorized" value="true" indexed="true" />				
				</logic:notEmpty>
			</td>
			<td><!-- 2ª hora Nocturna -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="nocturnalAfterSecondHour" >
					<html:hidden name="infoExtraWork" property="nocturnalAfterSecondHour" indexed="true" />
					<html:checkbox name="infoExtraWork" property="nocturnalAfterSecondHourAuthorized" value="true" indexed="true" />
				</logic:notEmpty>
			</td>
			<td><!-- Fim de semana e Feriados -->&nbsp;
				<logic:notEmpty name="infoExtraWork" property="restDay" >
					<html:hidden name="infoExtraWork" property="restDay" indexed="true" />
					<html:checkbox name="infoExtraWork" property="restDayAuthorized" value="true" indexed="true" />
				</logic:notEmpty>
			</td>
		</tr>
	</logic:iterate>
</table>
<table border="0">
	<tr><td><br /><br /></td></tr>
	<tr><td>
	<bean:message key="label.compensation" />:&nbsp;<br />
	<bean:message key="label.compensation.a" />&nbsp;<html:radio property="compensation" value="a"/><br />
	<bean:message key="label.compensation.b" />&nbsp;<html:radio property="compensation" value="b"/><br />
	<bean:message key="label.compensation.c" /><html:radio property="compensation" value="c"/>
	</td></tr>
	<tr><td><br /><br /></td></tr>
	<tr><td>		
		<html:submit styleClass="inputbutton">
			<bean:message key="botao.avançar"/>
		</html:submit>
	</tr></td>
</table>
<table border="0">
	<tr><td>
	<br/>
	<bean:message key="label.description.justification" />:&nbsp;_____________________________________________________________
	_________________________________________________________________________________
	</td></tr>
	<tr><td>
	<bean:message key="label.responsable.service" />&nbsp;____________________________
	<bean:message key="label.employee.number.abr" />&nbsp;________
	<bean:message key="prompt.data" />___/___/____
	</td></tr>
</table>
</html:form>
  