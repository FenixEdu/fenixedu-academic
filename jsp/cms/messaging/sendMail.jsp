<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<style>
	.floatedli {
	float: left;
	width: 250px;
	}
</style>

<jsp:include page="/cms/manager/context.jsp"/>

<logic:present name="groupsToChooseFrom">
	<bean:define id="groups" name="sendMailForm" property="groupsToChooseFrom"/>
</logic:present>

<html:form method="post" action="/mailSender">
	<html:hidden property="method" value="send"/>
	<html:hidden property="returnURL"/>
	<html:hidden property="group"/>
	
	<%
	java.util.Map parameters =  request.getParameterMap();
	StringBuffer hiddenField = new StringBuffer();
	for(Object key : parameters.keySet())
	{
		if (!key.equals("method") && !key.equals("returnURL") && !key.equals("group") && key instanceof String)
		{
			String keyString = (String) key;
			Object value = request.getParameter(keyString);
			if (value instanceof String)
			{
				String valueString = (String) value;
				hiddenField.delete(0,hiddenField.length());
				hiddenField.append("<input type=\"hidden\" name=\"").append(keyString).append("\" value=\"").append(valueString).append("\">");
				out.write(hiddenField.toString());
			}
		}
	}
	 %>

<logic:present name="groups">
	<div style="width:800px">
		<ul>
			<logic:iterate id="group" name="groups" type="net.sourceforge.fenixedu.domain.PersonalGroup">
				<li class="floatedli">
					<html:multibox property="selectedPersonalGroupsIds">
	       				 <bean:write name="group" property="idInternal"/>
				    </html:multibox>
			        <bean:write name="group" property="name"/>
				</li>
			</logic:iterate>			
		</ul>			
	</div>
</logic:present>

<div style="clear: both;">
	<br/>
	<fieldset class="lfloat2">
	<p><label>Nome do Remetente:</label><html:text property="fromPersonalName" size="50"/></p>	
	<p><label>Endereço do Remetente:</label> <html:text property="fromAddress" size="50"/></p>	
	<p><label>Assunto:</label> <html:text property="subject" size="50"/></p>
	<p><label>Mensagem:</label> <html:textarea rows="20" cols="100" property="message"/></p>
	</fieldset>
</div>

<html:submit onclick="this.form.method.value='send';this.form.submit();">
	Enviar
</html:submit>

<html:submit onclick="this.form.method.value='goBack';this.form.submit();">
	Voltar
</html:submit>

</html:form>