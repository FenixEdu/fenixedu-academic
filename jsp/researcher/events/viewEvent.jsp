<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="eventId" name="selectedEvent" property="idInternal" />
	
	<em>Eventos</em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.viewEvent.useCasetitle"/></h2>
  	

	<%-- PARTICIPATIONS --%>	
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants"/></strong></p>
	<fr:view name="participations" layout="tabular" schema="eventParticipation.summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editParticipants" />
			</html:link>
		</li>
	</ul>


	<%-- DATA --%>		
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.data"/></strong></p>
	<fr:view name="selectedEvent" schema="event.view-defaults">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright thtop"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/events/editEvent.do?method=prepareEditData&eventId="+eventId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editData" />
			</html:link>
		</li>
	</ul>
 	
	<%-- PROJECTS --%>		
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.projects"/></strong></p>
	<fr:view name="selectedEvent" property="associatedProjects" schema="eventProjectAssociation.project-summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 mvert05"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/events/editEvent.do?method=prepareEditProjectAssociations&eventId="+eventId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editAssociatedProjects" />
			</html:link>
		</li>
	</ul>
	
	<%-- UNITS --%>		
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.units"/></strong></p>
	<fr:view name="unitParticipations" layout="tabular" schema="eventUnitParticipation.summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 mvert05"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/events/editEvent.do?method=prepareEditParticipantUnits&eventId="+eventId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editAssociatedUnits" />
			</html:link>
		</li>
	</ul>
	

		
	
	
</logic:present>
		
<br/>