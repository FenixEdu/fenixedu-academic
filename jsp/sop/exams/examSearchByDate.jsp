
<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/ExamSearchByDate" focus="day">

	<html:hidden property="method" value="search"/>
	<html:hidden property="page" value="1"/>
	<h2>Procurar Exames por Data</h2>
	<span class="error"><html:errors/></span>
	<table>
		<tr>
			<td>
			   	<bean:message key="property.exam.date"/>:
			</td>
			<td>
   				<html:text maxlength="2" size="2" property="day"/>
			   	/
		  		<html:text maxlength="2" size="2" property="month"/>
			   	/
		  		<html:text maxlength="4" size="4" property="year"/>
			   	(ex: 24/12/2004)
			</td>
		</tr>
		<tr>
			<td>
			    <bean:message key="property.exam.beginning"/>:
			</td>
			<td>
		  		<html:text maxlength="2" size="2" property="beginningHour"/>
   				:
		  		<html:text maxlength="2" size="2" property="beginningMinute"/> 
		  		(Opcional)
			</td>
		</tr>
		<tr>
			<td>
    			<bean:message key="property.exam.end"/>:
			</td>
			<td>
			   	<html:text maxlength="2" size="2" property="endHour"/>
			   	:
			   	<html:text maxlength="2" size="2" property="endMinute"/>
			   	(Opcional)
			</td>
		</tr>
	</table>
	<br/>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="lable.choose"/>
	</html:submit>
	
	<html:cancel value="Cancelar" styleClass="inputbutton">
		<bean:message key="label.cancel"/>
	</html:cancel>

</html:form>