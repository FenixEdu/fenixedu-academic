<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="person">
	<fr:view name="person" schema="PersonNameAndUsername">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop05"/>
	   		<fr:property name="columnClasses" value="aleft,"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:present>

<logic:notPresent name="professorships">
	Não está a leccionar nenhuma aula
</logic:notPresent>

<logic:present name="professorships">
	<logic:empty name="professorships">
			Não está a leccionar nenhuma aula
	</logic:empty>
	<logic:iterate id="professorship" name="professorships">
		<strong><bean:write name="professorship" property="executionCourse.nome"/>				
		(<bean:write name="professorship" property="degreeSiglas"/>)</strong>
		<br/>
		
		<logic:notEmpty name="professorship" property="nonRegularTeachingServices">
			<fr:view name="professorship" property="nonRegularTeachingServices">
				<fr:schema type="net.sourceforge.fenixedu.domain.NonRegularTeachingService" bundle="APPLICATION_RESOURCES">
					<fr:slot name="shift.presentationName" key="label.class.name" readOnly="true"/>
					<fr:slot name="percentage">
						<fr:property name="size" value="2"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="sortBy" value="shift.nome" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="professorship" property="nonRegularTeachingServices">
			Não foram encontrados registos de aulas.
		</logic:empty>
		
		 <logic:present name="canEdit">
			<p class="mtop0 pleft1">
				<bean:define id="professorshipOID" name="professorship" property="externalId"/>
				<html:link page='<%= "/manageNonRegularTeachingService.do?method=prepareEditNonRegularTeachingService&amp;professorshipOID="+professorshipOID%>'>
					<bean:message key="label.change" bundle="APPLICATION_RESOURCES" />
				</html:link>															
			</p>						
		</logic:present>
	</logic:iterate>
</logic:present>

