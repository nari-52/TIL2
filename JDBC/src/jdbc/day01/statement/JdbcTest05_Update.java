package jdbc.day01.statement;

import java.sql.*;
import java.util.*;

public class JdbcTest05_Update {

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
						 "from jdbc_tbl_memo\n";
			
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
			
			String menuNo = "";
			
			do {
				
				sql = "select no, name, msg\n"+
					  ",to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday   \n"+
					  "from jdbc_tbl_memo\n";
															
				System.out.println("\n -->>> 데이터 수정하기 <<<-- \n"
								 + " 1. 변경대상 글번호   2. 종료\n"
								 + " -----------------------");
				
				System.out.print("▶ 메뉴번호를 선택하세요 : ");
				menuNo = sc.nextLine();
				
				switch (menuNo) {
					case "1" : // 변경대상 글번호
						System.out.print("▶  변경대상 글번호 : ");
						String updateNo = sc.nextLine();	// 변경 글번호

						sql += " where no = " + updateNo;
						
						rs = stmt.executeQuery(sql);		
						
						if (rs.next()) {

							int no = rs.getInt(1);
							String name = rs.getString(2);
							String msg = rs.getString(3);
							String writeday = rs.getString(4);
							
							System.out.println("-----------------------------------------------------------");
						    System.out.println("글번호\t글쓴이\t글내용\t\t\t작성일자");
						    System.out.println("-----------------------------------------------------------");
						    							
							System.out.println(no +"\t"+name+"\t"+msg+"\t"+writeday );
							
							System.out.print("\n▶  변경할 글내용 입력 : ");
							String changMsg = sc.nextLine();	// 변경할 글내용
													
							sql = " update jdbc_tbl_memo set msg = '"+changMsg+"'\n"+
								  " where no = " + updateNo ;
							
							int n = stmt.executeUpdate(sql);
							
							if (n == 1) {
								
								String yn = "";
								
								do {
									System.out.print(">> 정말로 수정하시겠습니까? [Y/N] : ");
									yn = sc.nextLine();
									
									if ( "Y".equalsIgnoreCase(yn) ) {
										conn.commit();
										System.out.println(">>> 데이터 수정 성공! <<<");									
										break;
									}
									else if ( "N".equalsIgnoreCase(yn) ) {
										conn.rollback();
										System.out.println(">>> 데이터 수정 실패! <<<");									
										break;
									}
									else {
										System.out.println(">>> Y 또는 N만 입력 가능합니다.");
									}	
									
								} while (true);							
							} // end of if (n == 1)--------------------					

						}
					case "2" : // 종료
						
						break;

					default:
						System.out.println(">> 메뉴에 없는 번호를 선택하셨습니다. \n ");
						break;
				} // end of switch (menuNo)-----------------

			} while (!"2".equals(menuNo));
			
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
