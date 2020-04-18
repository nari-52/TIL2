package prac.jdbc.day01.statement;

import java.sql.*;
import java.util.*;

public class jdbcTest02_Insert {

	public static void main(String[] args) {
		
		Connection conn = null;
		// Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체이다.
		
		Statement stmt = null;
		/*
			개발자가 작성한 SQL문을 어느 오라클 서버에서 실행해야할지 결정해야한다.
			이떄, 오라클서버는 Connection conn에서 알고,
			Connection conn에 전송할 SQL문은 Statement stmt를 통해 전송된다.
		*/

		Scanner sc = new Scanner(System.in);
		
		
		try {
			// >>> 1. 오라클 드라이버 로딩 <<<
			/*
				=== OracleDriver(오라클 드라이버)의 역할 ===
				1) OracleDriver 를 메모리에 로딩시켜준다.
				2) OracleDriver 객체를 생성해준다.
				3) OracleDriver 객체를 DriverManager에 등록시켜준다.
					--> DriverManager는 여러 드라이버들을 Vector에 저장하여 관리해주는 클래스이다.
			 */
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. 어떤 오라클 서버에 연결할 것인지 선택
			System.out.print("▶ 연결할 오라클 서버의 IP주소 : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" +ip+ ":1521:xe", "myorauser", "eclass");
			// 127.0.0.1:1521 ==> IP:포트번호 ==> 소켓프로그램
			
			// >>> 수동커밋으로 전환하기
			conn.setAutoCommit(false);
			
			// >>> 3. 연결한 오라클서버(conn)에 SQL문을 전달할 Statement 객체 생성하기 <<<
			stmt = conn.createStatement();
			
			// >>> 4. SQL문 작성하기 <<<
			System.out.print("▶ 글쓴이 : ");
			String name = sc.nextLine();
			
			System.out.print("▶ 글내용 : ");
			String msg = sc.nextLine();
			
			String sql = "insert into jdbc_tbl_memo (no, name, msg)\n"+
						 "values (JDBC_SEQ_MEMO.nextval, '"+ name +"', '"+msg+"'  )";
			System.out.println("sql : " + sql);
			
			// >>> 5. Statement stmt 객체가 작성된 SQL문을 오라클서버에 보내서 실행되어지도록 한다
			int n = stmt.executeUpdate(sql);
			/*
				stmt.executeUpdate(sql); 에서
				파라미터로 들어오는 sql은 오로지 DML(insert, update, delete)문 만 사용이 가능하다.
				stmt.executeUpdate(sql); 을 실행한 결과는 int 타입으로 숫자를 돌려주는데
				sql 타입이 insert 라면 insert 되어진 행의 갯수를 돌려주고,
				sql 타입이 update 라면 update 되어진 행의 갯수를 돌려주고,
				sql 타입이 delete 라면 delete 되어진 행의 갯수를 돌려준다.
			 */
			
			System.out.println(n + "개 행이 입력됨");
		
			
			if (n==1) {
				
				do {
					
					System.out.print("▶ 정말로 입력 하시겠습니까? [Y/N] =>");
					String yn = sc.nextLine();
					
					if ("Y".equalsIgnoreCase(yn)) {
						conn.commit(); // 커밋!
						System.out.println(" >> 데이터 입력 성공하였습니다. <<\n");
						break;
					}
					else if ("N".equalsIgnoreCase(yn)) {
						conn.rollback(); // 롤백! 취소
						System.out.println(" >> 데이터 입력 취소 <<\n");
						break;
					}
					else {
						System.out.println(" >> [에러] Y 또는 N만 입력하세요! <<\n");
					}
											
				} while (true);

			}
			
			else {
				System.out.println(" >> [오류] 데이터 입력 오류 발생 <<");
			}
			
			conn.setAutoCommit(true); // 자동커밋으로 원상복구함

		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없거나 라이브러리에 등록되지 않았습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// >>> 6. 사용하였던 자원 반납하기 <<<
			// 반납 순서는 생성순서의 역순으로 한다.
			
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		sc.close();
		System.out.println("~~~~~~~~~ 프로그램 종료 ~~~~~~~~");

	} // end of void main()----------------------------------

}
