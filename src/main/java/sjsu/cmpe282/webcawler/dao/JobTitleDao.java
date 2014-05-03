package sjsu.cmpe282.webcawler.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sjsu.cmpe282.webcawler.db.DBConnecter;
import sjsu.cmpe282.webcawler.model.JobTitle;


public class JobTitleDao {
	
	public int insertJobTitle(ArrayList<JobTitle> list){
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = new DBConnecter().connectDatabase();
			for(JobTitle j : list){
				ps = con.prepareStatement("insert into job_title_list (job_title) value(?)");
				ps.setString(1, j.getJobTitle());
				ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			DBConnecter.closeStatement(ps);
			DBConnecter.closeConnection(con);
		}
		return 1;
	}
	
	public ArrayList<JobTitle> getTitleList(){
		ArrayList<JobTitle> list = new ArrayList<JobTitle>();
		Connection con = new DBConnecter().connectDatabase();
		Statement s = null;
		ResultSet rs = null;
		try {
			s = con.createStatement();
			rs = s.executeQuery("select * from job_title_list where is_crawled = 0 ");
			while(rs.next()){
				JobTitle j = new JobTitle();
				j.setId(rs.getInt("id"));
				j.setIsCrawled(rs.getInt("is_crawled"));
				j.setJobTitle(rs.getString("job_title"));
				j.setJobType(rs.getInt("job_type"));
				list.add(j);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return list;
	}
	
	public int updateIsCrawled(int status, int id){
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = new DBConnecter().connectDatabase();
			ps = con.prepareStatement("update job_title_list set is_crawled = ? where id = ?");
			ps.setInt(1,status);
			ps.setInt(2,id);
			ps.execute();
		} catch (SQLException e){
			e.printStackTrace();
			return 0;
		}finally{
			DBConnecter.closeStatement(ps);
			DBConnecter.closeConnection(con);
		}
		return 1;
	}
	
	public static void main(String[] args) {
		JobTitleDao dao = new JobTitleDao();
		for(int i = 1; i < 13 ; ++i){
			dao.updateIsCrawled(1, i);
		}
	}
}
