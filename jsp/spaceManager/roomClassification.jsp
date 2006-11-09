<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.title"/></h2>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:notPresent name="roomClassificationEditor">
	<h4 class="mtop15 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.create"/></h4>
	<fr:hasMessages for="create" type="conversion">
		<p>
			<span class="error0">			
				<fr:message for="create" show="message"/>
			</span>
		</p>
	</fr:hasMessages>

	<fr:edit id="create" name="roomClassificationCreator"
			type="net.sourceforge.fenixedu.domain.space.RoomClassification$RoomClassificationFactoryCreator"
			schema="RoomClassificationFactory"
			action="/roomClassification.do?method=executeFactoryMethod"	>
		<fr:destination name="exception" path="/roomClassification.do?method=viewRoomClassifications"/>	
		<fr:destination name="invalid" path="/roomClassification.do?method=viewRoomClassifications"/>	
		<fr:destination name="cancel" path="/roomClassification.do?method=viewRoomClassifications"/>				
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:notPresent>

<logic:present name="roomClassificationEditor">
	<h4 class="mtop15 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.edit"/></h4>
	<fr:hasMessages for="edit" type="conversion">
		<p>
			<span class="error0">			
				<fr:message for="edit" show="message"/>
			</span>
		</p>
	</fr:hasMessages>
	<fr:edit id="edit" name="roomClassificationEditor"
			type="net.sourceforge.fenixedu.domain.space.RoomClassification$RoomClassificationFactoryEditor"
			schema="RoomClassificationFactory"
			action="/roomClassification.do?method=executeFactoryMethod"	>
		<fr:destination name="exception" path="/roomClassification.do?method=viewRoomClassifications"/>	
		<fr:destination name="invalid" path="/roomClassification.do?method=viewRoomClassifications"/>	
		<fr:destination name="cancel" path="/roomClassification.do?method=viewRoomClassifications"/>				
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:present>


<h4 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.list"/></h4>

<fr:view name="roomClassifications"	schema="RoomClassificationInList">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight mtop05"/>
        <fr:property name="columnClasses" value="aleft,aleft,aleft"/>
		<fr:property name="link(edit)" value="/roomClassification.do?method=prepareRoomClassification"/>
		<fr:property name="param(edit)" value="idInternal/roomClassificationID"/>
        <fr:property name="key(edit)" value="space.manager.room.link.edit"/>
        <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
		<fr:property name="link(delete)" value="/roomClassification.do?method=deleteRoomClassification"/>
		<fr:property name="param(delete)" value="idInternal/roomClassificationID"/>
        <fr:property name="key(delete)" value="space.manager.room.link.delete"/>
        <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
	</fr:layout>
</fr:view>
