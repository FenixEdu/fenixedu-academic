/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ClearCache {

    @Service
    public static Boolean run() {
        return Boolean.TRUE;
    }

}