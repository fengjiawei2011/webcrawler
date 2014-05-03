package sjsu.cmpe282.webcawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sjsu.cmpe282.webcawler.dao.JobTitleDao;
import sjsu.cmpe282.webcawler.dao.ResumeDao;
import sjsu.cmpe282.webcawler.db.DBConnecter;
import sjsu.cmpe282.webcawler.model.JobTitle;
import sjsu.cmpe282.webcawler.model.Resume;
import sjsu.cmpe282.webcawler.util.PDFConverter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ResumeCrawler {
	
	private JobTitleDao jDao = null;
	private ResumeDao rd = null;
	private Connection con = null;
	private ResumePageUrl resumePageUrl = null;
	private WebClient webClient = null;
	int counter = 0;
	

	public static void main(String[] args) throws InterruptedException {
		ResumeCrawler crawler = new ResumeCrawler();
		/*String [] keywords = {"engineer","software","java","education","developer","trainer","analyst","association","coordinator"};
		String [] keywordss = {"development","employment",
				"facilitator","international","management","research","therapist",
				"non-profit","planning","LMSW","LSW","human-services","chemical-dependency"};*/
		
		ArrayList<JobTitle> jobTitleList = crawler.jDao.getTitleList();
		
		try {
			crawler.crawl_2(jobTitleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------Dowanload Completed---------------------------");
		
		
		
		try {
			PDFConverter.converTo("D:\\Documents\\KuaiPan\\resume-dataset-pdf", "D:\\Documents\\KuaiPan\\resume-dataset-txt");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[convert error]");
		}
		
		System.out.println("------------------Convert Completed---------------------------");
	}

	public void init() {
		webClient = new WebClient(BrowserVersion.FIREFOX_24);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		resumePageUrl = new IndeedResumeUrl();
		con = new DBConnecter().connectDatabase();
		rd = new ResumeDao(con);
		jDao = new JobTitleDao();
//		counter = rd.getCounter();
	}

	public ResumeCrawler() {
		init();
	}
	
	public String convertToIndeedFormat(String s){
		return s.trim().toLowerCase().replace(" ", "-");
	}

	public Document getDoc(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			Thread.sleep(1000);
			if (doc == null) {
				throw new IOException(" Fail to connect...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("document connection error");
		}
		return doc;
	}

	public String getResume(HtmlPage page, String name) {
		try {
			HtmlAnchor a = page.getAnchorByText(name);
			HtmlPage nextPage = (HtmlPage) a.click();
			Thread.sleep(1000 * 10);
			return nextPage.asText();
		} catch (Exception e) {
			System.out.println(String.format("do not find this [%s] resume ",name));
		}
		return "error";
	}


	public void crawl_2(List<JobTitle> keywords) {
		
		for(JobTitle j : keywords){
			System.out.println(convertToIndeedFormat(j.getJobTitle()));
			int pageStart = 0;
			resumePageUrl.setUrlParametersPageSize(pageStart);
			resumePageUrl.setMajorKeywords(convertToIndeedFormat(j.getJobTitle()));
			while (pageStart <= 950){
				try {
					System.out.println(String.format("page url --->[%s]",resumePageUrl.getResumePageUrl()));
					System.out.println(String.format("----------------page start from [%s]-------------------",pageStart));
					downloadAllResumeInOnePage(resumePageUrl.getResumePageUrl(),"D:\\Documents\\KuaiPan\\resume-dataset-pdf");
					pageStart += 50;
					resumePageUrl.setUrlParametersPageSize(pageStart);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/*
	 * download all resume in one page
	 */
	public void downloadAllResumeInOnePage(String pageUrl, String destPath) {
		ArrayList<Resume> resumes = getPageResumesList(pageUrl);
		for(Resume r : resumes){
			try {
				downloadSingleResume( r, destPath);
				if(rd.storeResumes(r) == 1) System.out.println("store resume into mysql successfully");
				counter++;
				Thread.sleep(1000 * 3);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(String.format("file download failed"));
			}
		}

	}

	/*
	 * get pdf download url map(key = name , value = link) inside resume link
	 */

	public ArrayList<Resume> getPageResumesList(String pageUrl) {
		ArrayList<Resume> resumes =  new ArrayList<Resume>();
		List<String> links = getResumesLinks(getLinkElements(pageUrl));
		for (String link : links) {
			Document doc = null;
			try {
				doc = getDoc(link);
				String name = doc.select("#resume-contact").text();
				String downloadLink = doc.select("#download_pdf_button").attr("href");
				Resume resume = new Resume();
				resume.setResumeLink(link);
				resume.setResumeName(name);
				resume.setResumeDownloadLink(downloadLink);
				resumes.add(resume);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[Document error ] , cannot get web page");
			}
		}
		return resumes;
	}

	/*
	 * using InputStream to download files by url links
	 */
	public void downloadSingleResume(Resume r, String destPath) {
		FileOutputStream fos = null;
		InputStream in = null;
		URL url = null;
		try {
			url = new URL(r.getResumeDownloadLink());
			File directory = new File(destPath);
			if (!directory.exists())
				directory.mkdir();
			in = url.openStream();
			String path = destPath + "\\"  + r.getResumeName() + "#" + r.getResumeid() + ".pdf";
			fos = new FileOutputStream(new File(path));
			System.out.println(String.format("[%s] is dowanloading......",  r.getResumeName()));
			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from
											// connection
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}
			
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println("[url error] no protocal");
		}catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("[IO error], open url stream failed");
		}finally {
			try {
				fos.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[IO error], close connection failed");
			}
		}

		System.out.println(String.format("[%s] was downloaded......",  r.getResumeName()));
	}

	public List<String> getResumesLinks(Elements htmlLinks) {
		List<String> links = new ArrayList<String>();
		for (Element e : htmlLinks) {
			String resumeLink = resumePageUrl.getAbsoluteUrl() + e.attr("href");
			links.add(resumeLink);
		}
		return links;
	}


	/******************************************************************************/
	/*	public boolean hasResume(String url) throws IOException {
	boolean hasResume = true;
	if (this.getDoc(url).select(".error_page") == null) {
		System.out
				.println("----------------finish download all resumes ------------------------");
		hasResume = false;
	}
	return hasResume;
}*/

	/*
	 * using htmlUit to get html page then parse pages into text
	 */
	public String getResumeText(String url) {
		HtmlPage resume = null;
		try {
			resume = (HtmlPage) webClient.getPage(url);
			return resume.asText();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error";
	}
/*	public void write(String name, String resumeText) {

		try {
			File directory = new File("D:\\Documents\\KuaiPan\\resume-dataset");
			File file = new File("D:\\Documents\\KuaiPan\\resume-dataset\\"
					+ name + ".txt");

			if (!directory.exists()) {
				directory.mkdir();
			}

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(resumeText);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format(" failed to save [%s] resume ", name));
		}
	}*/
	
/*	public void showLink(String url) throws IOException {
		Document doc = this.getDoc(url);
		Elements es = doc.select(".app_link");
		for (Element e : es) {
			System.out.println(e.attr("href"));
		}
	}*/
	

	public List<String> obtainPageNameList(String url) {
		try {
			List<String> nameList = new ArrayList<String>();
			Document doc = Jsoup.connect(url).get();
			Elements eList = doc.select(".app_link");
			System.out.println("size of page name list is : " + eList.size());
			for (Element e : eList) {
				nameList.add(e.text().trim());
			}
			return nameList;
		} catch (Exception e) {
			System.out.println(String.format("No Name List found!"));
		}
		return null;
	}



/*	public List<String> obtainPageNameList(Elements htmlLinks) {
		List<String> links = new ArrayList<String>();
		for (Element e : htmlLinks) {
			links.add(e.text());
		}
		return links;
	}
*/
	
	
	/*	public void crawl() throws InterruptedException, IOException {
	int pageStart = 0;
	while (this.hasResume(resumePageUrl.getResumePageUrl())) {
		resumePageUrl.setUrlParametersPageSize(pageStart);
		pageStart += 50;
		String url = resumePageUrl.getResumePageUrl();
		System.out.println("resume url = " + url);
		Elements htmlLinks = this.getLinkElements(url);
		List<String> names = obtainPageNameList(htmlLinks);
		List<String> links = getResumesLinks(htmlLinks);
		System.out.println(String.format(
				"start is [%s] name list is [%s] and link list is [%s]",
				pageStart, names.size(), links.size()));
		for (int a = 0; a < links.size(); a++) {
			String resumeText = getResumeText(links.get(a));
			if (!"error".equals(resumeText)) {
				counter++;
				this.write(counter + "-" + names.get(a), resumeText);
				System.out.println(String.format(
						"Save %s.txt successfully", names.get(a)));
			}
			Thread.sleep(1000 * 2);
		}
	}
}*/

	/*
	 * get resume links on pages
	 */
	public Elements getLinkElements(String url) {
		Document doc = null;
		try {
			doc = this.getDoc(url);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Can not get HTML Document!");
		}
		return doc.select(".app_link");
	}
}
