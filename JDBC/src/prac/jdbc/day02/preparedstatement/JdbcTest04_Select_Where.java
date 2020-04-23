package prac.jdbc.day02.preparedstatement;

import java.sql.*;
import java.util.*;

public class JdbcTest04_Select_Where {

	public static void main(String[] args) {
		
		// Statement 를 PreparedStatement로 수정하기	
		
		Connection conn = null;
		// Connection conn은 오라클 데이터베이스 서버와 연결을 맺어주는 객체
		
		PreparedStatement pstmt = null;
		// conn에 전송할 SQL문은 PreparedStatement pstmt를 통해 전송된다.
		
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
			
			// >>> 3. SQL문 장성
			String sql = "select no, name, msg\n"+
						 ",to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday   \n"+
						 "from jdbc_tbl_memo2\n"+
						 "order by no desc";
						
			
			// >>> 4. PreparedStatement 객체 생성하기 <<<
			pstmt = conn.prepareStatement(sql);
			
			
			// >>> 5. PreparedStatement pstmt 객체가 SQL문을 오라클서버에 보내서 실행되어지도록 한다.
			rs = pstmt.executeQuery();
			
			
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
			
			String menuNo = "";
			
			do {
				
				sql = "select no, name, msg\n"+
					  ",to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday   \n"+
					  "from jdbc_tbl_memo2\n";
											
				System.out.println("\n >>> 검색대상 선택 <<< \n"
								 + " 1. 글번호   2. 글쓴이   3. 글내용   4. 전체조회   5. 종료\n");
				
				System.out.print("▶ 번호를 선택하세요 : ");
				menuNo = sc.nextLine();
				
				switch (menuNo) {
					case "1" : // 글번호
						System.out.print("▶  검색할 글번호 : ");
						String searchNo = sc.nextLine();
						sql += " where no = " + searchNo;
						
						break;
					case "2" : // 글쓴이
						System.out.print("▶  검색할 글쓴이 : ");
						String searchName = sc.nextLine();
						sql += " where name = '" + searchName +"'" ;
						
						break;
					case "3" : // 글내용
						System.out.print("▶  검색할 글내용 : ");
						String searchMsg = sc.nextLine();
						sql += " where msg like '%" + searchMsg +"%'" ;
						
						break;
					case "4" : // 전체조회
						
						break;
					case "5" : // 종료
						
						break;

					default:
						System.out.println(">> 메뉴에 없는 번호를 선택하셨습니다. \n ");
						break;
				} // end of switch (menuNo)-----------------
				
				if ("1".equals(menuNo) || "2".equals(menuNo) ||
					"3".equals(menuNo) || "4".equals(menuNo) ) {
					
					rs = pstmt.executeQuery(sql);
					
					System.out.println("-----------------------------------------------------------");
				    System.out.println("글번호\t글쓴이\t글내용\t\t\t작성일자");
				    System.out.println("-----------------------------------------------------------");
				    
					while (rs.next()) {
						
						int no = rs.getInt(1);
						String name = rs.getString(2);
						String msg = rs.getString(3);
						String writeday = rs.getString(4);
						
						System.out.println(no +"\t"+name+"\t"+msg+"\t"+writeday );
						
					}					
				}
				
			} while (!"5".equals(menuNo));
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없거나 라이브러리에 등록되지 않았습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				try {
					// >>> 6. 사용하였던 자원반납하기 <<<
					if (rs != null) rs.close();
					// SQLException 예외처리하기
					if (pstmt != null) pstmt.close();
					if (conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}			
		}
		
		sc.close();
		System.out.println(" ~~~~~~ 프로그램 종료 ~~~~~~");

	} // end of void main()-------------------------------

}


