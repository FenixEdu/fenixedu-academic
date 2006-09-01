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

<bean:define id="sendMailActioName" name="sendMailForm" property="sendMailActioName"/>
<jsp:include page="/cms/manager/context.jsp"/>

<logic:present name="groupsToChooseFrom">
	<bean:define id="groups" name="sendMailForm" property="groupsToChooseFrom"/>
</logic:present>

<html:form method="post" action="<%=sendMailActioName.toString()%>">
	<html:hidden property="method" value="send"/>
	<html:hidden property="group"/>
	<html:hidden property="returnURL"/>
	<html:hidden property="previousRequestParameters"/>

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
	<p><label>Cópia para:</label> <html:text property="copyTo" size="50"/><font size="-2"> (separe os endereços por vírgulas)</font></p>
	<p><label>Cópia para o remetente</label><html:checkbox property="copyToSender"/>(assinale caso deseje receber uma cópia da mensagem no endereço do remetente)</p> 
	<p><label>Assunto:</label> <html:text property="subject" size="50"/></p>
	<p><label>Mensagem:</label> <html:textarea rows="20" cols="100" property="message"/></p>
	</fieldset>
</div>

<html:submit onclick="this.form.method.value='send';this.form.submit();">
	Enviar
</html:submit>

</html:form>