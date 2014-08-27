package database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class TransGoodsAgent {
	
	public static void main(String[] args) {
		
		try {
			System.out.println(new TableData().getTransazioni("province")+"\n");
			System.out.println(new TableData().getTransazioni("province").get(0)+"\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = new TableData().getTables("STSN_MAIN");
			while (rs.next()){
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
