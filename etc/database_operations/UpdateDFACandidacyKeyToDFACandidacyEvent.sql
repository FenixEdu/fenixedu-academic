select concat('update CANDIDACY set KEY_DFA_CANDIDACY_EVENT = ',
	ID_INTERNAL,
	' where ID_INTERNAL = ', 
	KEY_CANDIDACY, ';') 
as "" from ACCOUNTING_EVENT WHERE OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent';
