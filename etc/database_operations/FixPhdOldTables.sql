update CANDIDACY set CANDIDACY.KEY_PHD_CANDIDACY_EVENT = NULL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacy.PHDProgramCandidacy';

alter table REGISTRATION drop KEY_PHD_REGISTRATION_EVENT;

alter table CANDIDACY drop KEY_PHD_CANDIDACY_EVENT;

update CANDIDACY set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.phd.candidacy.PHDProgramCandidacy' where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacy.PHDProgramCandidacy';

