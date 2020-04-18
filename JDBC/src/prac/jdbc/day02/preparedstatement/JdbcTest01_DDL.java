package prac.jdbc.day02.preparedstatement;

import java.sql.*;
import java.util.*;

public class JdbcTest01_DDL {

	public static void main(String[] args) {

		Connection conn = null;
		// Connection conn 은 오라클 데이터베이스 서버와 연결을 맺어주는 객체이다.
		
		Statement stmt = null;
		/*
			개발자가 SQL문(편지)을 작성했는데,
			작성한 SQL문을 어느 오라클서버에 실행을 해야할지 결정해야 한다.
			이때, 어느 오라클서버인지는 Connection conn 에서 알고,
			Connection conn에 전송할 SQL문(편지)은 Statement stmt(우편배달부)를 통해 전송된다.
		 */

		try {
			// >>> 1. 오라클 드라이버 로딩 <<<
			/*
			     === OracleDriver(오라클 드라이버)의 역할 ===
			     1). OracleDriver 를 메모리에 로딩시켜준다.
			     2). OracleDriver 객체를 생성해준다.
			     3). OracleDriver 객체를 DriverManager에 등록시켜준다. 
			         --> DriverManager 는 여러 드라이버들을 Vector에 저장하여 관리해주는 클래스이다. 
			*/
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. 어떤 오라클서버에 연결을 할래? (local 이냐 remote냐) <<<
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "myorauser", "eclass");
			// 27.0.0.1:1521 ==> IP:포트번호 ==> 소켓프로그램
			
			// >>> 3. 연결한 오라클서버(conn)에 SQL문(편지)을 전달할 Statement 객체 (우편배달부) 생성하기 <<<
			stmt = conn.createStatement();
			
			// >>> 4. SQL문(편지)을 작성한다.
			String sql1 = " create table jdbc_tbl_memo "
						+ " ( no			number(4) not null "
						+ " , name		varchar2(20) "
						+ " , msg		varchar2(200) "
						+ " , writeday	date default sysdate "
						+ " , constraint PK_jdbc_tbl_memo primary key(no) "
						+ " ) ";	// ;create 문의 ;는 생략한다. 쓰면 오류남
			
			String sql2 = " create sequence jdbc_seq_memo "
						+ " start with 1 "
						+ " increment by 1 "
						+ " nomaxvalue "
						+ " nominvalue "
						+ " nocycle "
						+ " nocache ";
			
			String sql3 = " insert into jdbc_tbl_memo(no, name, msg)"
						+ " values(jdbc_seq_memo.nextval, '이순신', '안녕하세요? 이순신 인사드립니다.') ";
			
			// >>> 5. Statement stmt 객체 (우편배달부)가 작성된 SQL문(편지)을 오라클서버에 보내서 실행이 되어지도록 한다.
			boolean isSQL1 = stmt.execute(sql1);
			boolean isSQL2 = stmt.execute(sql2);
			boolean isSQL3 = stmt.execute(sql3);
			/*
				stmt.execute(sql1); 을 실행하면 true 또는 false 가 나온다.
				파라미터로 들어오는 sql1 문이 select 문 일때만 정상적으로 성공시 true 를 리턴시켜주고,
				그 이외의 DDL문(create, alter, drop, truncate) 이거나 
				DML문(insert, update, delete)일 경우에는 성공했다고 하더라도 false를 리턴시켜준다.
			 */
			System.out.println("확인용 isSQL1 => " + isSQL1);
			// 확인용 isSQL1 => false // DDL 문이기 때문에 성공해도 false가 나온다
			
			System.out.println("확인용 isSQL2 => " + isSQL2);
			// 확인용 isSQL2 => false
			
			System.out.println("확인용 isSQL3 => " + isSQL3);
			// 확인용 isSQL3 => false
			
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar 파일이 없거나 라이브러리에 등록되지 않았습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. 사용하였던 자원 반납하기 <<<
			// 반납의 순서는 생성순서의 역순으로 한다.
			
			try {
				if ( stmt != null ) stmt.close();
				if ( conn != null )conn.close();
			} catch (SQLException e) {
	
				e.printStackTrace();
			}
		}

		
	} // end of main()-----------------------------------------

}

