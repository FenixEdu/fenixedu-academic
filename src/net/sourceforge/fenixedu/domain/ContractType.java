package net.sourceforge.fenixedu.domain;

public enum ContractType {

    EFFECTIVE, 
    ON_TERM, 
    INDEPENDENT_WORKER, 
    INDEPENDENT_WORKER_WITH_EMPLOYEES,
    INDEPENDENT_WORKER_WITHOUT_EMPLOYEES,
    RECEIPT_CONTRACT, 
    PROFESSIONAL_INTERNSHIP, 
    SCHOLARSHIP, 
    OTHER;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return ContractType.class.getSimpleName() + "." + name();
    }

}
