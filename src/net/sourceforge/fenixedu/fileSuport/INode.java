/*
 * Created on 16/Set/2003
 *
 */
package net.sourceforge.fenixedu.fileSuport;

/**
 * fenix-head fileSuport
 * 
 * @author João Mota 16/Set/2003
 *  
 */
public interface INode {

    abstract public String getSlideName();

    abstract public INode getParentNode();

}