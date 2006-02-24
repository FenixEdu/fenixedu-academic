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

<bean:define id="groups" name="sendMailForm" property="groupsToChooseFrom"/>

<html:form method="get" action="/mailSender">
	<html:hidden property="method" value="send"/>
	<html:hidden property="returnURL"/>

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