<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="title.card.generation" bundle="CARD_GENERATION_RESOURCES"/></em>
<h2><bean:message key="link.manage.card.generation.upload.card.info"/></h2>

<p><html:link page="/manageCardGeneration.do?method=firstPage">« Voltar</html:link></p>

<logic:notPresent name="readingComplete">
	<p>
		<div class="warning1 mvert15">
			<p class="mvert05">
				<b>
					<bean:message key="message.card.generation.info.upload.rules" bundle="CARD_GENERATION_RESOURCES"/>
				</b>
			</p>
			<p class="mvert05">
				<bean:message key="message.card.generation.info.upload.rules.text" bundle="CARD_GENERATION_RESOURCES"/>
			</p>
		</div>
	</p>
	
	<fr:edit id="uploadBean"
		name="uploadBean"
		schema="CardInfoUpload"
		action="<%="/manageCardGeneration.do?method=readCardInfo"%>">
		
		<fr:destination name="cancel" path="<%= "/manageCardGeneration.do?method=firstPage"%>"/>
	
	</fr:edit>
</logic:notPresent>
<logic:present name="readingComplete">
	<div class="warning1 mvert15">
		<p class="mvert05">
			<bean:message key="message.card.generation.info.upload.conclusion.text" bundle="CARD_GENERATION_RESOURCES"/>
		</p>
	</div>
	<table class="tstyle4 thlight tdcenter mtop05">
		<tr>
			<th>
				<bean:message key="message.number.of.created.registers" bundle="CARD_GENERATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="message.number.of.not.created.registers" bundle="CARD_GENERATION_RESOURCES"/>
			</th>
		</tr>
		<tr>
			<td>
				<bean:write name="createdRegisters"/>
			</td>
			<td>
				<bean:write name="notCreatedRegisters"/>
			</td>
		</tr>
	</table>
	<table class="tstyle4 thlight tdcenter mtop05">
		<tr>
			<th>
				<bean:message key="message.lines.of.not.created.registers" bundle="CARD_GENERATION_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="line" name="notCreatedLines">
			<tr>
				<td>
					<bean:write name="line"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
