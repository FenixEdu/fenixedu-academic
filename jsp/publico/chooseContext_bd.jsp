<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error">
	<html:errors/>
</span>
<p><strong><font color="#FF0000">Atenção&nbsp;-&nbsp;</font>Se pretende consultar informação relativa a disciplinas de 4ï¿½ ou 5ï¿½ em 2003/2004 ou 5ï¿½ em 2004/2005 do curso de Informï¿½tica - Alameda, deve seleccionar o plano curricular "Licenciatura em Engenharia Informï¿½tica e de Computadores - LEIC - Currï¿½culo Antigo"</strong></p>
<p>	<strong><font color="#FF0000">Atenção&nbsp;-&nbsp;</font>Devido ï¿½ alteraï¿½ï¿½o do calendï¿½rio das Licenciaturas em Engenharia Civil, em Engenharia do Territï¿½rio, e em Arquitectura (despacho do Conselho Directivo do passado dia 29 de Julho), as salas de aula dessas trï¿½s Licenciaturas foram alteradas</strong></p>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:parameter id="nextPage" name="nextPage" />
<bean:parameter id="inputPage" name="inputPage" />
<html:form action="<%=path%>" >
	<input alt="input.method" type="hidden" name="method" value="nextPagePublic"/>
	<input alt="input.nextPage" type="hidden" name="nextPage" value="<%= nextPage %>"/>
	<input alt="input.inputPage" type="hidden" name="inputPage" value="<%= inputPage %>"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</p>
	<p>
		<bean:message key="property.context.degree"/>
		:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
			<html:options collection="degreeList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<p class="infoop">
		<bean:message key="label.chooseYear" />
	</p>
	<p>
	<p>
		<bean:message key="property.context.curricular.year"/>
		:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.curYear" property="curYear" size="1">
			<html:options collection="curricularYearList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
		<bean:message key="label.next"/>
	</html:submit>
</html:form>