package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InvalidMarksServiceException extends FenixServiceException {
	private List<DomainException> exceptionList;
	
	public InvalidMarksServiceException(List<DomainException> exceptionlist) {
		this.exceptionList = exceptionlist;
	}
	
	public List<DomainException> getExceptionList(){
		return this.exceptionList;
	}
}
