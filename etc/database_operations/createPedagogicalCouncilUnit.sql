insert into PARTY_TYPE (`TYPE`) values ('PEDAGOGICAL_COUNCIL');
insert into PARTY_TYPE (`TYPE`) values ('SCIENTIFIC_COUNCIL');

UPDATE PARTY 
SET OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit'
WHERE CLASSIFICATION = 'PEDAGOGICAL_COUNCIL';

update PARTY set CLASSIFICATION = 'CENTRAL_ORG' where CLASSIFICATION = 'PEDAGOGICAL_COUNCIL';
update PARTY set KEY_PARTY_TYPE = (select ID_INTERNAL from PARTY_TYPE where TYPE = 'PEDAGOGICAL_COUNCIL') where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit';