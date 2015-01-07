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
		this.name = name;
		this.param = params;
		this.start = new Date();
		this.extra = extra;
	}
	
	public TestResult(String name, String params) {
		this.name = name;
		this.param = params;
		this.start = new Date();
	}
	
	public TestResult(String name, String runInfo, String params, String extra) {
		this.name = name;
		this.param = params;
		this.start = new Date();
		this.extra = extra;
		this.runInfo = runInfo;
	}

	public TestResult(TestStatus status, String name, Throwable error, String runInfo, String params, Date start, Date end, String extra) {
		this.name = name;
		this.param = params;
		this.status = status;
		this.setError(error);
		this.start = start;
		this.end = end;
		this.extra = extra;
		this.runInfo = runInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getError() {
		return this.error;
	}

	public void setError(Throwable error) {
		if (error == null) {
			this.error = "";
		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			error.printStackTrace(pw);
			String str = sw.toString();
			if (str.length() > 700) {
				this.error = str.substring(0, 699);
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
		this.extra = extra;
	}

	public String getRunInfo() {
		return runInfo;
	}

	public void setRunInfo(String runInfo) {
		this.runInfo = runInfo;
	}

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
