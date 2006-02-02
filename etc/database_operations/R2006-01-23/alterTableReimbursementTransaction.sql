alter table REIMBURSEMENT_TRANSACTION add column CLASS_NAME varchar(255);
update REIMBURSEMENT_TRANSACTION set CLASS_NAME = 'net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction';
