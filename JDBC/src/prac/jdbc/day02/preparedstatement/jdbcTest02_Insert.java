package prac.jdbc.day02.preparedstatement;

/*	
	==== Statement 와 PreparedStatement ====
	Statement 와 PreparedStatement 의 가장 큰 차이점은 캐시 사용유무 이다.
	이들은 쿼리문장 (SQL문)을 분석([==파싱 parsing], 문법검사, 인덱스 사용유무) 하고 컴파일 후 실행된다.
	Statement는 매번 쿼리문장 (SQL문)을 수행할 떄 마다 모든 단계 (파싱 parsing)을 거치지만
	PreparedStatement 는 처음 한번만 모든 단계 (파싱 parsing)를 수행한 후 캐시에 담아 재사용한다.
	그러므로 동일한 쿼리문장 (SQL문)을 수행시 PreparedStatement 가 DB에 훨씬 적은 부하를 주므로 성능이 좋아진다.
	
	또한 Statement 는 사용자가 입력한 단어(검색어 또는 입력단어)들이 보여지지만
	PreparedStatement는 위치홀더(?)를 사용하므로 입력한 단어(검색어 또는 입력단어)들이 보여지지 않으므로
	Statement 보다 PreparedStatement 가 보안성이 높아 PreparedStatement 를 주로 사용한다.
*/

import java.sql.*;
import java.util.*;

public class jdbcTest02_Insert {

	public static void main(String[] args) {
		
		// Statement 를 PreparedStatement로 수정하기	
		
		Connection conn = null;
		// Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체이다.
		
		PreparedStatement pstmt = null;
		/*
			개발자가 작성한 SQL문을 어느 오라클 서버에서 실행해야할지 결정해야한다.
			이떄, 오라클서버는 Connection conn에서 알고,
			Connection conn에 전송할 SQL문은 Statement stmt를 통해 전송된다.
		*/

		Scanner sc = new Scanner(System.in);
				
		try {
			// >>> 1. 오라클 드라이버 로딩 <<<
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. 어떤 오라클 서버에 연결할 것인지 선택
			System.out.print("▶ 연결할 오라클 서버의 IP주소 : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" +ip+ ":1521:xe", "myorauser", "eclass");
			// 127.0.0.1:1521 ==> IP:포트번호 ==> 소켓프로그램
			
			// >>> 수동커밋으로 전환하기
			conn.setAutoCommit(false);
			
			// >>> 3. SQL문 작성하기 <<<
			System.out.print("▶ 글쓴이 : ");
			String name = sc.nextLine();
			
			System.out.print("▶ 글내용 : ");
			String msg = sc.nextLine();
			
			String sql = "insert into jdbc_tbl_memo2(no, name, msg)\n"+
						 "values (JDBC_SEQ_MEMO2.nextval, ?, ?)";

			// >>> 4. 연결한 오라클서버(conn)에 SQL문을 전달할 Statement 객체 생성하기 <<<
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  name);  // 1번 ?에 글쓴이 넣어라
			pstmt.setString(2, msg);	// 2번 ?에 글내용 넣어라
			
			System.out.println("sql : " + sql);
			// 입력한 데이터 name과 msg는 보안되어 ? 로 출력된다.
			
			// >>> 5. PreparedStatement pstmt 객체가 작성된 SQL문을 오라클서버에 보내서 실행되어지도록 한다
			int n = pstmt.executeUpdate(); // 4번 과정에서 sql문이 이미 pstmt에 들어있다.
			
			/*
				pstmt.executeUpdate(); 에서
				실행되어질 sql문이 DDL문 (create, alter, drop, truncate) 이라면 리턴값 0이 나온다.
				실행되어질 sql문이 DML문 (insert, update, delete) 이라면 리턴값이 실행된 행의개수가 나온다. 
			 */
			
			System.out.println(n + "개 행이 입력됨\n");
			
			if (n==1) {
				
				do {
					System.out.print("▶ 정말로 게시글을 업로드 하시겠습니까? [Y/N] : ");
					String yn = sc.nextLine();
					
					if ("Y".equalsIgnoreCase(yn)) {
						conn.commit();
						System.out.println(">> 업로드 성공!! <<\n");
						break;						
					}
					else if ("N".equalsIgnoreCase(yn)) {
						conn.rollback();
						System.out.println(">> 업로드 취소 !! <<\n");
						break;
					}
					else {
						System.out.println(">> [에러] Y 또는 N 만 입력 가능합니다! << \n");
					}
					
				} while (true);

			} // end of if (n==1)-------------------------------
			else {
				System.out.println(">> 데이터 입력에 오류가 발생하였습니다. << \n");
			}
			
			conn.setAutoCommit(true); // 자동커밋으로 원상복구
			

		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없거나 라이브러리에 등록되지 않았습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. 사용하였던 자원 반납하기 <<<
			// 반납 순서는 생성순서의 역순으로 한다.
			
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		sc.close();
		System.out.println("~~~~~~~~~ 프로그램 종료 ~~~~~~~~");

	} // end of void main()----------------------------------

}
