<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<div class="row text-center">
    <h1><bean:message key="link.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></h1>
</div>

<div class="row">
    <div class="col-md-3">
    <h2><bean:message key="link.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditCandidacyPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
    <div class="col-md-3">
    <h2><bean:message key="link.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showCandidacyPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
    <div class="col-md-3">
    <h2><bean:message key="link.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditVotingPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
    <div class="col-md-3">
    <h2><bean:message key="link.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/></h2>
    <p><bean:message key="label.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></p>
    <p><html:link styleClass="btn btn-default" page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showVotingPeriods"><bean:message key="label.open"/> &raquo;</html:link></p>
    </div>
</div>
