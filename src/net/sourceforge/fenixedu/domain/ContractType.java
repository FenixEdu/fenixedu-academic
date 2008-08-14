package net.sourceforge.fenixedu.domain;

public enum ContractType {

    EFFECTIVE, ON_TERM, RECEIPT_CONTRACT, SCHOLARSHIP, PROFESSIONAL_INTERNSHIP, INDEPENDENT_WORKER, INDEPENDENT_WORKER_WITH_EMPLOYEES;

    public String getName() {
	return name();
    }
}
