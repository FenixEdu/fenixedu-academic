package Dominio.precedences;

import Dominio.IDomainObject;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestriction extends IDomainObject
{
	public abstract IPrecedence getPrecedence();

	public abstract void setPrecedence(IPrecedence precedence);

	public abstract boolean evaluate(PrecedenceContext precedenceContext);
}