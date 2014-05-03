package sjsu.cmpe282.webcawler;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sjsu.cmpe282.webcawler.dao.JobTitleDao;
import sjsu.cmpe282.webcawler.model.JobTitle;

public class JobTitleCrawler {

	private String url = "http://jobsearch.about.com/od/job-title-samples/a/it-job-titles.htm";
	private String tagClass = "";
	private String tagId = "articlebody";
	
	public static void main(String[] args) {
		try {
			 new JobTitleCrawler().storeJobTitle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void storeJobTitle(){
		JobTitleDao jDao = new JobTitleDao();
		try {
			jDao.insertJobTitle(getJobTitleList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList<JobTitle> getJobTitleList() throws Exception{
		ArrayList<JobTitle> list = new ArrayList<JobTitle>();
		if(this.url.equals("")){
			throw new Exception("url cannot be null");
		}
		
		ResumeCrawler rc = new ResumeCrawler();
		Document doc = rc.getDoc(url);
		Elements elements = doc.select("#articlebody li");
		for(Element e : elements){
			JobTitle j = new JobTitle();
			j.setJobTitle(e.text());
			list.add(j);
			//System.out.println(e.text());
		}
		return list;
	}

	public String getTagClass() {
		return tagClass;
	}

	public void setTagClass(String tagClass) {
		this.tagClass = tagClass;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
