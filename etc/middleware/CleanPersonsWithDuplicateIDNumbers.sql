	
select concat('delete from mw_PESSOA where documentidnumber = ', mwpwdi.documentidnumber,' and idinternal = ', mwp.idinternal,';') as ""
	from mw_PERSON_WITH_DUPLICATE_ID mwpwdi inner join mw_PESSOA mwp on mwp.documentidnumber = mwpwdi.documentidnumber
	where mwp.idinternal <> mwpwdi.maxidinternal;

select concat('delete from mw_ALUNO_temp where number = ', mwswvn.number,';') as ""
	from mw_STUDENTS_WITH_VARIOUS_NUMBERS mwswvn inner join mw_ALUNO_temp mwa on mwa.documentidnumber = mwswvn.documentidnumber
	where mwa.number <> mwswvn.number;
	
	
