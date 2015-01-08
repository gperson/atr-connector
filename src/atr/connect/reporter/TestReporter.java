package atr.connect.reporter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import atr.connect.dto.TestResult;
import atr.connect.enums.PostTestStatus;
import atr.connect.enums.TestStatus;

public class TestReporter {

	private String url;
	private TestResult testResult;
	public boolean status = true;
	private String projectTable = "tests_Default";
	private final static Logger LOGGER = Logger.getLogger(TestReporter.class.getName());

	/**
	 * Creates a TestReporter instance, posting tests to given URL
	 * 
	 * @param serverPostTestURL
	 *            URL to post results to
	 */
	public TestReporter(String serverPostTestURL) {
		this.url = serverPostTestURL;
	}

	/**
	 * Creates a TestReporter instance, posting tests to given URL and database table
	 * 
	 * @param serverPostTestURL
	 *            URL to post results to
	 * @param projectTable
	 *            Name of the table to post tests to
	 */
	public TestReporter(String serverPostTestURL, String projectTable) {
		this.url = serverPostTestURL;
		this.projectTable = projectTable;
	}

	/**
	 * Gets the table where the test results are being posted
	 * 
	 * @return
	 */
	public String getProjectTable() {
		return projectTable;
	}

	/**
	 * Sets the table for where you want to post the tests
	 * 
	 * @param projectTable
	 *            DB table name i.e. tests_ExampleProject
	 */
	public void setProjectTable(String projectTable) {
		this.projectTable = projectTable;
	}

	/**
	 * Posts the results of a test to the tests server
	 * 
	 * @param testResult
	 *            TestResult object to post to server
	 * @return Status of the posted test
	 */
	@SuppressWarnings("unchecked")
	public void setResults(TestResult testResult) {
		try {
			this.testResult.setEnd(new Date());
			
			URL obj;
			if(this.url.endsWith("/")){
				obj = new URL(this.url + "addTest");
			}
			else{
				obj = new URL(this.url + "/addTest");
			}
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "TestReporterJar");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("result", testResult.getTestResultJSON());
			json.put("table", this.projectTable);
			wr.writeBytes(json.toJSONString());
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			if (responseCode == 200) {
				LOGGER.info(PostTestStatus.SUCCESSFUL_POST.toString());
			} else {
				LOGGER.severe(PostTestStatus.FAILED_TO_POST.toString());
			}

		} catch (MalformedURLException e) {
			LOGGER.severe(PostTestStatus.INVALID_SERVER_URL.toString());
		} catch (IOException e) {
			LOGGER.severe(PostTestStatus.IO_ERROR_WITH_SERVER.toString());
		}
	}

	/**
	 * Creates a test if there isn't one already and sets the RunInfo of the test
	 * 
	 * @param runInfo
	 *            The current run info i.e "Regression, Sprint 1"
	 */
	public void setTestInfo(String runInfo) {
		if (this.testResult == null) {
			this.testResult = new TestResult();
		}
		this.testResult.setRunInfo(runInfo);
	}

	/**
	 * Creates a test if there isn't one already
	 * 
	 * @param name
	 *            Name of test
	 * @param runInfo
	 *            Run information for test i.e. "Smoke Test"
	 * @param params
	 *            Parameters for the test
	 * @param extra
	 *            Any extra info
	 */
	public void setTestInfo(String name, String runInfo, String params, String extra) {
		if (this.testResult == null) {
			this.testResult = new TestResult(name, runInfo, params, extra);
		} else {
			this.testResult.setName(name);
			this.testResult.setRunInfo(runInfo);
			this.testResult.setParam(params);
			this.testResult.setExtra(extra);
		}

	}
	
	/**
	 * Creates a test if there isn't one already
	 * 
	 * @param name
	 *            Name of test
	 * @param params
	 *            Parameters for the test
	 * @param extra
	 *            Any extra info
	 */
	public void setTestInfo(String name, String params, String extra) {
		if (this.testResult == null) {
			this.testResult = new TestResult(name, params, extra);
		} else {
			this.testResult.setName(name);
			this.testResult.setParam(params);
			this.testResult.setExtra(extra);
		}

	}
	
	/**
	 * Creates a test if there isn't one already
	 * 
	 * @param name
	 *            Name of test
	 * @param params
	 *            Parameters for the test
	 */
	public void setTestInfo(String name, String params) {
		if (this.testResult == null) {
			this.testResult = new TestResult(name, params);
		} else {
			this.testResult.setName(name);
			this.testResult.setParam(params);
		}

	}

	/**
	 * Creates a test if there isn't one already and sets the parameters field for the test
	 * 
	 * @param params
	 *            Parameters for the test
	 */
	public void setParams(String params) {
		if (this.testResult == null) {
			this.testResult = new TestResult();
		}
		this.testResult.setParam(params);
	}

	/**
	 * Creates a test if there isn't one already and sets the run info
	 * 
	 * @param runInfo
	 *            Run info for a test i.e "Smoke Test"
	 */
	public void setRunInfo(String runInfo) {
		if (this.testResult == null) {
			this.testResult = new TestResult();
		}
		this.testResult.setRunInfo(runInfo);
	}
	
	/**
	 * Creates a test if there isn't one already and sets the name
	 * 
	 * @param name
	 *            Name of the test
	 */
	public void setName(String name) {
		if (this.testResult == null) {
			this.testResult = new TestResult();
		}
		this.testResult.setName(name);
	}

	/**
	 * Creates a test if there isn't one already and sets the extra data field
	 * 
	 * @param extra
	 *            Any extra information to be displayed
	 */
	public void setExtra(String extra) {
		if (this.testResult == null) {
			this.testResult = new TestResult();
		}
		this.testResult.setExtra(extra);
	}

	/**
	 * Sets the start time for the test
	 */
	public void setStartTime() {
		if (this.testResult == null) {
			this.testResult = new TestResult();
		}
		this.testResult.setStart(new Date());
	}

	/**
	 * Posts the test result with a SUCCESS status to the DB
	 */
	public void passTest() {
		if(this.testResult == null){
			this.testResult = new TestResult();
		}
		this.testResult.setError(null);
		this.testResult.setStatus(TestStatus.SUCCESS);
		setResults(this.testResult);
		this.testResult = null;
	}

	/**
	 * Posts the test result with a FAILURE status to the DB
	 * 
	 * @param e
	 *            The error that was thrown by the test
	 * @throws Throwable
	 *             Throws the error after its posted
	 */
	public void failTest(Throwable e) throws Throwable {
		if(this.testResult == null){
			this.testResult = new TestResult();
		}
		this.testResult.setError(e);
		this.testResult.setStatus(TestStatus.FAILURE);
		setResults(this.testResult);
		this.status = false;
		this.testResult = null;
		throw e;
	}

	/**
	 * Posts the test result with a 'non-default' status to the DB
	 * 
	 * @param status
	 *            The TestStatus to set the test status to
	 * @param e
	 *            The error that was thrown by the test
	 * @throws Throwable
	 *             Throws the error after its posted
	 */
	public void failTestNonDefault(TestStatus status, Throwable e) throws Throwable {
		if(this.testResult == null){
			this.testResult = new TestResult();
		}
		this.testResult.setError(e);
		this.testResult.setStatus(status);
		setResults(this.testResult);
		this.status = false;
		this.testResult = null;
		throw e;
	}
}
