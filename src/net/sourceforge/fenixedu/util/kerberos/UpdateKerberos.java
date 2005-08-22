package net.sourceforge.fenixedu.util.kerberos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class UpdateKerberos {
	
	public static String changeKerberosPass(String user, String pass) throws ExcepcaoPersistencia{
		String returnCode = null;
		String script = PropertiesManager.getProperty("changePassScript");
		StringBuilder cmd = new StringBuilder();
		cmd.append(script);
		cmd.append(" ");
		cmd.append(user);
		
		RunScript scriptThread = new RunScript(cmd.toString(), pass);
		scriptThread.start();
		
		long delayMillis = 5000; // 5 seconds
	    try {
	    	scriptThread.join(delayMillis);
	        if (scriptThread.isAlive()) {
	            throw new ExcepcaoPersistencia("Timeout");
	        } else {
	             if(scriptThread.getExitCode() == -1)
	            	 throw new ExcepcaoPersistencia(scriptThread.getReturnCode());
	             else if(scriptThread.getExitCode() > 0)
	            	 returnCode = scriptThread.getReturnCode();
	        }
	        return returnCode;
	    } catch (InterruptedException e) {
	        throw new ExcepcaoPersistencia(e.getMessage());
	    }
	}
	
	public static String createUser(String user, String pass) throws ExcepcaoPersistencia{
		String returnCode = null;
		String script = PropertiesManager.getProperty("createUserScript");
		StringBuilder cmd = new StringBuilder();
		cmd.append(script);
		cmd.append(" ");
		cmd.append(user);
		
		try {
			Process process = Runtime.getRuntime().exec(cmd.toString());

			BufferedWriter outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			outCommand.write(pass);
			outCommand.newLine();
			outCommand.flush();
			outCommand.close();

			if (process.waitFor() != 0) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				returnCode = bufferedReader.readLine();
				bufferedReader.close();
			}
			return returnCode;
		} catch (Exception e) {
			throw new ExcepcaoPersistencia(e.getMessage());
		} 
	}
	
	private static class RunScript extends Thread{
		private Integer exitCode = null;
		private String returnCode = null;
		private String cmd = null;
		private String pass = null;
		
		public RunScript(String cmd, String pass) {
			this.cmd = cmd;
			this.pass = pass;
		}
		
		public void run() {
			try {
				Process process = Runtime.getRuntime().exec(cmd.toString());
				BufferedWriter outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				outCommand.write(pass);
				outCommand.newLine();
				outCommand.flush();
				outCommand.close();
				
				exitCode = process.waitFor();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				returnCode = bufferedReader.readLine();
				bufferedReader.close();

			} catch(Exception e) {
				this.exitCode = -1;
				this.returnCode = e.getMessage();
			}
        }
		
		public Integer getExitCode() {
			return this.exitCode;
		}
		
		public String getReturnCode() {
			return this.returnCode;
		}
	}
	
	public static class TestScript extends Thread{
		private Integer exitCode = null;
		private String returnCode = null;
		private String cmd = null;
		private String cmd2 = null;
		
		public TestScript(String cmd, String cmd2) {
			this.cmd = cmd;
			this.cmd2 = cmd2;
		}
		
		public void run() {
			try {
				Process process = Runtime.getRuntime().exec(cmd);
				BufferedWriter outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				outCommand.write(cmd2);
				outCommand.newLine();
				outCommand.flush();
				/*outCommand.write("Ola Ola");
				outCommand.newLine();
				outCommand.flush();*/
				outCommand.close();

				exitCode = process.waitFor();
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				returnCode = bufferedReader.readLine();
				bufferedReader.close();
				
			} catch(Exception e) {
				e.printStackTrace();
				this.exitCode = -1;
			}
        }
		
		public Integer getExitCode() {
			return this.exitCode;
		}
		
		public String getReturnCode() {
			return this.returnCode;
		}
	}
	
	public static void test() {
		TestScript test = new TestScript("/bin/bash", "sleep 10");
		test.start();
		
		long delayMillis = 5000; // 5 seconds
	    try {
	    	test.join(delayMillis);
	    
	        if (test.isAlive()) {
	            System.out.println("Thread is runnioing");
	        } else {
	            System.out.println("Exit Code :" + test.getExitCode());
	            System.out.println("Return Code :" + test.getReturnCode());
	        }
	    } catch (InterruptedException e) {
	        System.out.println("excption");
	    }
	}
	
	public static void main(String[] args) {
		UpdateKerberos.test();
	}
}
