package sjsu.cmpe282.webcawler.model;

public class JobTitle {
	
	private int id;
	private String jobTitle;
	private int isCrawled;
	private int jobType = 0;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public int getIsCrawled() {
		return isCrawled;
	}
	public void setIsCrawled(int isCrawled) {
		this.isCrawled = isCrawled;
	}
	public int getJobType() {
		return jobType;
	}
	public void setJobType(int jobType) {
		this.jobType = jobType;
	}
	
	

}
