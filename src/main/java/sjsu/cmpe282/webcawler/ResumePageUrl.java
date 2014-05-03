package sjsu.cmpe282.webcawler;

public abstract class ResumePageUrl {
	private String absoluteUrl;
	private String resumesREST;
	private String majorKeywords;
	private String urlParametersCo;
	private int urlParametersPageSize;
	private String resumePageUrl;
	
	public String getAbsoluteUrl() {
		return absoluteUrl;
	}
	public void setAbsoluteUrl(String absoluteUrl) {
		this.absoluteUrl = absoluteUrl;
	}
	public String getResumesREST() {
		return resumesREST;
	}
	public void setResumesREST(String resumes) {
		this.resumesREST = resumes;
	}
	public String getMajorKeywords() {
		return majorKeywords;
	}
	public void setMajorKeywords(String majorKeywords) {
		this.majorKeywords = majorKeywords;
	}
	public String getUrlParametersCo() {
		return urlParametersCo;
	}
	public void setUrlParametersCo(String urlParametersCo) {
		this.urlParametersCo = urlParametersCo;
	}

	public int getUrlParametersPageSize() {
		return urlParametersPageSize;
	}
	public void setUrlParametersPageSize(int urlParametersPageSize) {
		this.urlParametersPageSize = urlParametersPageSize;
	}
	public abstract String getResumePageUrl();
	
}
