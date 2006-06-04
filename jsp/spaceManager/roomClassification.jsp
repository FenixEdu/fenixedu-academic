<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.title"/>
</h2>
<br/>

<logic:notPresent name="roomClassification">
	<h4>
		<bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.create"/>
	</h4>

	<fr:create type="net.sourceforge.fenixedu.domain.space.RoomClassification"
			schema="RoomClassification"
			action="/roomClassification.do?method=viewRoomClassifications"
			>
		<fr:layout name="tabular" >
			<fr:property name="classes" value="style1,style1,"/>
	        <fr:property name="columnClasses" value="listClasses,listClasses,"/>
    	    <fr:property name="style" value="align: left"/>
		</fr:layout>
	</fr:create>
</logic:notPresent>

<logic:present name="roomClassification">
	<h4>
		<bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.edit"/>
	</h4>

	<fr:edit name="roomClassification"
			type="net.sourceforge.fenixedu.domain.space.RoomClassification"
			schema="RoomClassification"
			action="/roomClassification.do?method=viewRoomClassifications"
			>
		<fr:layout name="tabular" >
			<fr:property name="classes" value="style1,style1,"/>
	        <fr:property name="columnClasses" value="listClasses,listClasses,"/>
        	<fr:property name="style" value="align: left"/>
		</fr:layout>
	</fr:edit>
</logic:present>

<br/>

<h4>
	<bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.list"/>
</h4>

<fr:view name="roomClassifications"
		schema="RoomClassification"
		>
	<fr:layout name="tabular" >
		<fr:property name="classes" value="style1"/>
        <fr:property name="columnClasses" value="listClasses,listClasses,listClasses,listClasses"/>
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
