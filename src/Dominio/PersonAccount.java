/*
 * Created on Jul 22, 2004
 *
 */
package Dominio;

import java.util.List;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class PersonAccount extends DomainObject implements IPersonAccount {
    private Integer keyPerson;

    private Double balance;

    private IPessoa person;

    private List transactions;

}