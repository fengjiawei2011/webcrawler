package sjsu.cmpe282.webcawler.model;

import sjsu.cmpe282.webcawler.util.IDProducer;

public class Resume {
	private String resumeid;
	private String resumeName;
	private String resumeDownloadLink;
	private String resumeLink;
	private int bachelor_school;
	private int masterSchool;
	private int doctorSchool;
	private String workingExp;
	private String city;
	private String state;
	private String country;
	
	public Resume(){
		resumeid = IDProducer.getId();
	}
	
	public String getResumeid() {
		return resumeid;
	}
	public String getResumeName() {
		return resumeName;
	}
	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}
	public String getResumeDownloadLink() {
		return resumeDownloadLink;
	}
	public void setResumeDownloadLink(String resumeDownloadLink) {
		this.resumeDownloadLink = resumeDownloadLink;
	}
	public String getResumeLink() {
		return resumeLink;
	}
	public void setResumeLink(String resumeLink) {
		this.resumeLink = resumeLink;
	}
	public int getBachelor_school() {
		return bachelor_school;
	}
	public void setBachelor_school(int bachelor_school) {
		this.bachelor_school = bachelor_school;
	}
	public int getMasterSchool() {
		return masterSchool;
	}
	public void setMasterSchool(int masterSchool) {
		this.masterSchool = masterSchool;
	}
	public int getDoctorSchool() {
		return doctorSchool;
	}
	public void setDoctorSchool(int doctorSchool) {
		this.doctorSchool = doctorSchool;
	}
	public String getWorkingExp() {
		return workingExp;
	}
	public void setWorkingExp(String workingExp) {
		this.workingExp = workingExp;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
