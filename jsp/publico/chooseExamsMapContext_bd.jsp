<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<html:form action="/chooseExamsMapContextDA" method="GET">
	<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />	

	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="choose"/>

<%--
	<p style="font-size:12px">
		<b style="color: #f00">Nota:</b> Para os exames das <b>Licenciaturas LERCI, LESIM e LEGI</b> <u>onde se lê 9:00 dever-se-á ler 10:00</u>, com excepção de <b>PE</b>
		 e <b>CG</b> que se mantém às 9:00.
	</p>
	<br/>
--%>
	<br />
	<strong><font color="#FF0000">Aviso:</font></strong>
	<br />
	<strong>Se pretende consultar informação relativa a disciplinas de 4º ou 5º do curso de Informática - Alameda, deve seleccionar o plano curricular "Licenciatura em Engenharia Informática e de Computadores - LEIC - Currículo Antigo"</strong>
	<br /><br />

	<p class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</p>
	<p><bean:message key="property.context.degree"/>:
	<html:select property="index" size="1">
    	<html:options collection="degreeList" property="value" labelProperty="label"/>
    </html:select>
	</p>
	<br />
	<p class="infoop">
		<bean:message key="label.select.curricularYears" />
	</p>
   	<bean:message key="property.context.curricular.year"/>:<br/>
	<logic:present name="curricularYearList" >
		<logic:iterate id="item" name="curricularYearList">
			<html:multibox property="selectedCurricularYears">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/><bean:message key="label.exam.year"/><br/>
		</logic:iterate>
		<html:checkbox property="selectAllCurricularYears">
			<bean:message key="checkbox.show.all"/><br/>
		</html:checkbox>
	</logic:present>
	<br/>
   <p>
	   <html:submit value="Submeter" styleClass="inputbutton">
	   		<bean:message key="label.next"/>
	   </html:submit>
   </p>
</html:form>
