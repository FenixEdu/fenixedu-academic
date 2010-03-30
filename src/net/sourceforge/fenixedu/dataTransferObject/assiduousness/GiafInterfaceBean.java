package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Employee;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class GiafInterfaceBean implements Serializable {

    private Integer employeeNumber;
    private LocalDate localDate;
    private transient InputStream fileInputStream;
    private String filename;
    private byte[] fileByteArray;

    public GiafInterfaceBean() {
	super();
	setLocalDate(new LocalDate());
    }

    public Employee getEmployee() {
	return getEmployeeNumber() != null ? Employee.readByNumber(getEmployeeNumber()) : null;
    }

    public LocalDate getLocalDate() {
	return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
	this.localDate = localDate;
    }

    public Integer getEmployeeNumber() {
	return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
	this.employeeNumber = employeeNumber;
    }

    public InputStream getFileInputStream() {
	return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
	this.fileInputStream = fileInputStream;
    }

    public byte[] getFileByteArray() {
	return fileByteArray;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public void consume() {
	if (getFileInputStream() == null)
	    fileByteArray = null;
	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	try {
	    try {
		FileUtils.copy(getFileInputStream(), byteArrayOutputStream);
		byteArrayOutputStream.flush();
		fileByteArray = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} finally {
	    try {
		getFileInputStream().close();
		byteArrayOutputStream.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}
