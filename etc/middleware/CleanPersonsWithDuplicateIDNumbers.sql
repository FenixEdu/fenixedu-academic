	
select concat('delete from mw_PERSON where documentidnumber = ', mwpwdi.documentidnumber,' and idinternal = ', mwp.idinternal,';') as ""
	from mw_PERSON_WITH_DUPLICATE_ID mwpwdi inner join mw_PERSON mwp on mwp.documentidnumber = mwpwdi.documentidnumber
	where mwp.idinternal <> mwpwdi.maxidinternal;

select concat('delete from mw_STUDENT_AUXILIARY_TABLE where number = ', mwswvn.number,';') as ""
	from mw_STUDENTS_WITH_VARIOUS_NUMBERS mwswvn inner join mw_STUDENT_AUXILIARY_TABLE mwa on mwa.documentidnumber = mwswvn.documentidnumber
	where mwa.number <> mwswvn.number;
	
	
