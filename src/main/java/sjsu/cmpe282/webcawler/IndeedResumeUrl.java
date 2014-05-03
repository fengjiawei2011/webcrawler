package sjsu.cmpe282.webcawler;

public class IndeedResumeUrl extends ResumePageUrl{
	
	public IndeedResumeUrl(){
		this.setAbsoluteUrl("http://www.indeed.com");
		this.setResumesREST("resumes");
		this.setMajorKeywords("software");
		this.setUrlParametersPageSize(0);
		this.setUrlParametersCo("US");
	}

	public String getResumePageUrl(){
		return this.getAbsoluteUrl() + "/" + 
		this.getResumesREST() + "/" + 
		this.getMajorKeywords() + "?start=" + 
		this.getUrlParametersPageSize() + "&co=" +
		this.getUrlParametersCo();
	}
}
