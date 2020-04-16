package jdbc.day01.statement;

import java.sql.*;
import java.util.*;

public class JdbcTest04_Select_Where {

	public static void main(String[] args) {
		
		Connection conn = null;
		// Connection conn은 오라클 데이터베이스 서버와 연결을 맺어주는 객체
		
		Statement stmt = null;
		// conn에 전송할 SQL문은 Statement stmt를 통해 전송된다.
		
		ResultSet rs = null;
		// select 되어진 결과물을 저장하는 용도
		
		Scanner sc = new Scanner (System.in);
		
		
		
		try {
			// >>> 1. 오라클 드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// ClassNotFoundException 예외처리하기
			
			// >>> 2. 어떤 오라클 서버에 연결할지?
			System.out.print("▶ 연결할 오라클 서버의 IP 주소 : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":1521:xe", "myorauser", "eclass");
			// SQLException 예외처리하기
			
			conn.setAutoCommit(false); 
			// 수동 커밋으로 전환
			
			// >>> 3. Statement 객체 생성하기 <<<
			stmt = conn.createStatement();
			
			// >>> 4. SQL문 장성
			String sql = "select no, name, msg\n"+
						 ",to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday   \n"+
						 "from jdbc_tbl_memo\n"+
						 "order by no desc";
			
			// >>> 5. stmt 객체가 SQL문을 오라클서버에 보내서 실행되어지도록 한다.
			rs = stmt.executeQuery(sql);
			
			
			System.out.println("------------------------------------------------------");
			System.out.println(" 글번호\t글쓴이\t글내용\t\t작성일자");
			System.out.println("------------------------------------------------------");
			
			
			while (rs.next()) {
				// rs.next()는 select 되어진 결과물에서 커서의 위치(행의 위치)를 다음으로 옮긴 후
				// 행이 존재하면 true, 행이 없으면 false를 리턴시켜준다.
				
				int no = rs.getInt(1);				// 또는 rs.getInt("no");	또는 rs.getInt("NO"); 컬럼이름은 대소문자 구분 없다.
				String name = rs.getString(2);		// 또는 rs.getString("name");		또는 rs.getString("NAME");
				String msg = rs.getString(3);		// 또는 rs.getString("msg");		또는 rs.getString("MSG");
				String writeday = rs.getString(4);	// 또는 rs.getString(writeday);	또는 rs.getString(WRITEDAY);
				
				System.out.println(no + "\t" +name+ "\t" +msg+ "\t"+writeday);

			} // end of while (rs.next())--------------------
			
			//////////////////////////////////////////////////////////////////////////////
			// === where 절에 해당하는 메뉴 보여주기
			
			

			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					if (rs != null) rs.close();
					// SQLException 예외처리하기
					if (stmt != null) stmt.close();
					if (conn != null) stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}			
		}
		
		sc.close();
		System.out.println(" ~~~~~~ 프로그램 종료 ~~~~~~");

	} // end of void main()-------------------------------

}
