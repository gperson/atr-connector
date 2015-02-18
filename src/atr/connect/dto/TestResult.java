package atr.connect.dto;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

import org.json.simple.JSONObject;

import atr.connect.enums.TestStatus;

public class TestResult implements Serializable {

	private static final long serialVersionUID = 6904962531860463203L;
	private String name;
	private String param;
	private String error;
	private Date start;
	private Date end;
	private TestStatus status;
	private String extra;
	private String runInfo;

	public TestResult() {
		this.start = new Date();
	}

	public TestResult(String name, String params, String extra) {
		this.setName(name);
		this.setParam(params);
		this.start = new Date();
		this.setExtra(extra);
	}
	
	public TestResult(String name, String params) {
		this.setName(name);
		this.setParam(params);
		this.start = new Date();
	}
	
	public TestResult(String name, String runInfo, String params, String extra) {
		this.setName(name);
		this.setParam(params);
		this.start = new Date();
		this.setExtra(extra);
		this.setRunInfo(runInfo);
	}

	public TestResult(TestStatus status, String name, Throwable error, String runInfo, String params, Date start, Date end, String extra) {
		this.setName(name);
		this.setParam(params);
		this.status = status;
		this.setError(error);
		this.start = start;
		this.end = end;
		this.setExtra(extra);
		this.setRunInfo(runInfo);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = JSONObject.escape(name);
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = JSONObject.escape(param);
	}

	public String getError() {
		return this.error;
	}

	/**
	 * Sets the error by converting the throwable object to a string,
	 * limits its length to 1000 chars,
	 * @param error Error thrown by the test
	 */
	public void setError(Throwable error) {
		if (error == null) {
			this.error = "";
		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			error.printStackTrace(pw);
			String str = sw.toString();
			JSONObject.escape(str);
			if (str.length() > 1000) {
				this.error = str.substring(0, 999);
			} else {
				this.error = str;
			}
		}
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * Converts the enum to the class used by the UI
	 * @return Status string which is used to set the color of the row on the UI
	 */
	public String getStatus() {
		if (this.status == TestStatus.ERROR) {
			return "warning";
		} else if (this.status == TestStatus.SUCCESS) {
			return "success";
		} else if (this.status == TestStatus.DEFAULT) {
			return "default";
		} else {
			return "danger";
		}
	}

	public void setStatus(TestStatus status) {
		this.status = status;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = JSONObject.escape(extra);
	}

	public String getRunInfo() {
		return runInfo;
	}

	public void setRunInfo(String runInfo) {
		this.runInfo = JSONObject.escape(runInfo);
	}

	/**
	 * Returns a JSON object representation for the TestResult instance
	 * @return JSONObject of the TestResult object
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getTestResultJSON() {
		if (this.name == null) {
			this.name = "";
		}
		if (this.param == null) {
			this.param = "";
		}
		if (this.status == null) {
			this.status = TestStatus.DEFAULT;
		}
		if (this.start == null) {
			this.start = new Date();
		}
		if (this.end == null) {
			this.end = new Date();
		}
		if (this.extra == null) {
			this.extra = "";
		}
		if (this.runInfo == null) {
			this.runInfo = "";
		}

		JSONObject obj = new JSONObject();
		obj.put("name", this.name);
		obj.put("runInfo", this.runInfo);
		obj.put("param", this.param);
		obj.put("status", this.getStatus());
		obj.put("error", this.getError());
		obj.put("start", this.start.getTime());
		obj.put("end", this.end.getTime());
		obj.put("extra", this.extra);
		return obj;
	}
}
