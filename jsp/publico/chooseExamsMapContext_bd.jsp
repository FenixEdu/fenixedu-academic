<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/chooseExamsMapContextDA" method="get">
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />	

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>

<%--
	<p style="font-size:12px">
		<b style="color: #f00">Nota:</b> Para os exames das <b>Licenciaturas LERCI, LESIM e LEGI</b> <u>onde se lï¿? 9:00 dever-se-ï¿? ler 10:00</u>, com excepï¿?ï¿?o de <b>PE</b>
		 e <b>CG</b> que se mantï¿?m ï¿?s 9:00.
	</p>
	<br/>
--%>
	<p><strong><font color="#FF0000">Atenï¿?ï¿?o&nbsp;-&nbsp;</font>Se pretende consultar informação relativa a disciplinas de 4ï¿? ou 5ï¿? em 2003/2004 ou 5ï¿? em 2004/2005 do curso de Informï¿?tica - Alameda, deve seleccionar o plano curricular "Licenciatura em Engenharia Informï¿?tica e de Computadores - LEIC - Currï¿?culo Antigo"</strong></p>

	<p class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</p>
	<p><bean:message key="property.context.degree"/>:
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
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
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/><bean:message key="label.exam.year"/><br/>
		</logic:iterate>
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllCurricularYears" property="selectAllCurricularYears">
			<bean:message key="checkbox.show.all"/><br/>
		</html:checkbox>
	</logic:present>
	<br/>
   <p>
	   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
	   		<bean:message key="label.next"/>
	   </html:submit>
   </p>
</html:form>
