package sjsu.cmpe282.webcawler.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sjsu.cmpe282.webcawler.model.Resume;

public class ResumeDao {
	
	Connection con = null;
	
	public ResumeDao(Connection con){
		this.con = con;
	}
	
	public int storeResumes(Resume r){
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("insert into resume (resume_id, resume_name, resume_download_link,resume_link) value(?,?,?,?)");
			ps.setString(1, r.getResumeid());
			ps.setString(2, r.getResumeName());
			ps.setString(3, r.getResumeDownloadLink());
			ps.setString(4, r.getResumeLink());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	public int getCounter(){
		Statement ps = null;
		try {
			ps = con.createStatement();
			ResultSet rs = ps.executeQuery("select count from counter");
			if(rs.next()) return rs.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("get counter failed");
		}
		return -1;
	}
	
	public boolean updateCounter(int count){
		Statement ps = null;
		try {
			ps = con.createStatement();
			if(ps.execute("update counter set count = " + count)) return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("update counter failed");
		}
		return false;
	}

}
