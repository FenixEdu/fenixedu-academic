/*
 * Created on 2004/07/18
 * 
 */
package ServidorApresentacao.Action.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.renderer.BarChart;
import Util.renderer.TimeLineChart;
import Util.renderer.container.RequestEntry;

/**
 * @author Luis Cruz
 */
public class MonitorRequestLogsDA extends FenixDispatchAction {

	private static String logImageDir = null;

	private static final DateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss");

	private static String logFileName = null;

	public ActionForward listFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IUserView userView = SessionUtils.getUserView(request);
		ServiceUtils.executeService(userView, "ConfirmManagerIdentity", null);

		DynaActionForm actionForm = (DynaActionForm) form;
		String logDirName = (String) actionForm.get("logDirName");
		if (logDirName == null || logDirName.length() == 0
				|| logFileName == null || logFileName.length() == 0) {
			InputStream inputStream = getClass().getResourceAsStream(
					"/logAnalyser.properties");

			if (inputStream != null) {
				Properties serProps = new Properties();
				serProps.load(inputStream);

				if (logDirName == null || logDirName.length() == 0) {
					logDirName = serProps.getProperty("log.profile.dir");
				}
				if (logFileName == null || logFileName.length() == 0) {
					logFileName = serProps.getProperty("log.profile.filename");
				}
			} else {
				System.out
						.println("Failled to obtain properties for log analyser.");
				System.out.println("Using default values.");
				logDirName = "./";
				logFileName = "profileing.log";
			}
		}

		File logDir = new File(logDirName);
		SortedSet fileNames = new TreeSet();
		File[] logProfileFiles = logDir.listFiles();
		for (int i = 0; i < logProfileFiles.length; i++) {
			File fileOfDir = logProfileFiles[i];
			if (fileOfDir.isFile()
					&& fileOfDir.getName().startsWith(logFileName)) {
				fileNames.add(fileOfDir.getName());
			}
		}

		actionForm.set("logDirName", logDirName);
		request.setAttribute("monitorRequestLogsPathForm", actionForm);
		request.setAttribute("fileNames", fileNames);

		return mapping.findForward("ShowSelectionForm");
	}

	public ActionForward analyseFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IUserView userView = SessionUtils.getUserView(request);
		ServiceUtils.executeService(userView, "ConfirmManagerIdentity", null);

		DynaActionForm actionForm = (DynaActionForm) form;
		String logDirName = (String) actionForm.get("logDirName");
		String logFileName = request.getParameter("logFileName");
		if (logDirName == null || logDirName.length() == 0
				&& logFileName == null || logFileName.length() == 0) {
			return listFiles(mapping, form, request, response);
		}

		File logFile = new File(logDirName + "/" + logFileName);
		Collection profiles = processFile(logFile, request);
		request.setAttribute("profiles", profiles);
		request.setAttribute("logFileName", logFileName);

		return mapping.findForward("ShowRequestAnalysisLog");
	}

	private Collection processFile(File file, HttpServletRequest request) {
		Map profileMap = new HashMap();
		SortedSet sortedSet = null;
		String fileContents = readFileContents(file);
		if (fileContents != null) {
			StringTokenizer stringTokenizer = new StringTokenizer(fileContents,
					"\n");
			while (stringTokenizer.hasMoreElements()) {
				String line = (String) stringTokenizer.nextElement();
				if (!StringUtils.contains(line, "[Filter: profiling]")) {
					// the line contains usefull info
					processLine(profileMap, line);
				} else {
					// nothing usefull... throw this away
					//System.out.println("Invalid line: " + line);
				}
			}

			sortedSet = dumpInfo(file.getName(), profileMap, file.getName());
		}
		return sortedSet;
	}

	private SortedSet dumpInfo(String name, Map profileMap, String filename) {
		if (logImageDir == null) {
			setLogImageDir();
		}

		SortedSet sortedProfileSet = sortProfileMap(profileMap);
		new BarChart(sortedProfileSet, "Average Execution Time", logImageDir
				+ "/" + filename + ".bar.png", 20);
		new TimeLineChart(sortedProfileSet, logImageDir + "/" + filename
				+ ".timeline.png");
		return sortedProfileSet;
	}

	private void setLogImageDir() {
		InputStream inputStream = getClass().getResourceAsStream(
				"/logAnalyser.properties");
		if (inputStream != null) {
			Properties properties = new Properties();
			try {
				properties.load(inputStream);
				logImageDir = properties.getProperty("log.image.directory");
			} catch (IOException e) {
			}
		}
	}

	private SortedSet sortProfileMap(Map profileMap) {
		SortedSet sortedSet = new TreeSet(new Comparator() {
			public int compare(Object arg0, Object arg1) {
				RequestEntry requestEntry0 = (RequestEntry) arg0;
				RequestEntry requestEntry1 = (RequestEntry) arg1;
				return requestEntry1.getAverageExecutionTime()
						- requestEntry0.getAverageExecutionTime();
			}
		});
		sortedSet.addAll(profileMap.values());
		return sortedSet;
	}

	private void processLine(Map profileMap, String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, " \t[]-");
		Date logTime = parseTime(stringTokenizer.nextToken());
		String executionTimeString = stringTokenizer.nextToken();
		Integer executionTime = new Integer(executionTimeString.substring(0,
				executionTimeString.length() - 2));
		String requestPath = stringTokenizer.nextToken();

		RequestEntry requestEntry = (RequestEntry) profileMap.get(requestPath);
		if (requestEntry == null) {
			requestEntry = getNewHashKey(requestPath);
			profileMap.put(requestPath, requestEntry);
		}
		requestEntry.addEntry(executionTime, logTime);
	}

	private Date parseTime(String timeString) {
		Date result = null;
		try {
			result = dateFormat.parse(timeString);
		} catch (ParseException e) {
		}
		return result;
	}

	private String readFileContents(File file) {
		try {
			FileReader fileReader = new FileReader(file);
			// We'll initialize the buffer with the optimal value for the
			// example
			// log files we experimented with.
			char[] buffer = new char[4096];
			StringBuffer fileContents = new StringBuffer();
			int n = 0;
			while ((n = fileReader.read(buffer)) != -1) {
				fileContents.append(buffer, 0, n);
			}
			return fileContents.toString();
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Error processing file: "
					+ file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O Exception. Error processing file: "
					+ file.getAbsolutePath());
			e.printStackTrace();
		}
		return null;
	}

	public RequestEntry getNewHashKey(String requestPath) {
		return new RequestEntry(requestPath);
	}

}