<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error">
	<html:errors/>
</span>
<p><strong><font color="#FF0000">Atenção&nbsp;-&nbsp;</font>Se pretende consultar informação relativa a disciplinas de 4º ou 5º em 2003/2004 ou 5ª em 2004/2005 do curso de Informática - Alameda, deve seleccionar o plano curricular "Licenciatura em Engenharia Informática e de Computadores - LEIC - Currículo Antigo"</strong></p>
<p>	<strong><font color="#FF0000">Atenção&nbsp;-&nbsp;</font>Devido à alteração do calendário das Licenciaturas em Engenharia Civil, em Engenharia do Território, e em Arquitectura (despacho do Conselho Directivo do passado dia 29 de Julho), as salas de aula dessas três Licenciaturas foram alteradas</strong></p>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:parameter id="nextPage" name="nextPage" />
<bean:parameter id="inputPage" name="inputPage" />
<html:form action="<%=path%>" >
	<input type="hidden" name="method" value="nextPagePublic"/>
	<input type="hidden" name="nextPage" value="<%= nextPage %>"/>
	<input type="hidden" name="inputPage" value="<%= inputPage %>"/>
	<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden property="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</p>
	<p>
		<bean:message key="property.context.degree"/>
		:
		<html:select property="index" size="1">
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
		<html:select property="curYear" size="1">
			<html:options collection="curricularYearList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<br />
	<html:submit value="Submeter" styleClass="inputbutton">
		<bean:message key="label.next"/>
	</html:submit>
</html:form>