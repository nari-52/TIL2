package prac.jdbc.day02.preparedstatement;

import java.sql.*;
import java.util.*;

public class JdbcTest01_DDL {

	public static void main(String[] args) {

		Connection conn = null;
		// Connection conn �� ����Ŭ �����ͺ��̽� ������ ������ �ξ��ִ� ��ü�̴�.
		
		Statement stmt = null;
		/*
			�����ڰ� SQL��(����)�� �ۼ��ߴµ�,
			�ۼ��� SQL���� ��� ����Ŭ������ ������ �ؾ����� �����ؾ� �Ѵ�.
			�̶�, ��� ����Ŭ���������� Connection conn ���� �˰�,
			Connection conn�� ������ SQL��(����)�� Statement stmt(�����޺�)�� ���� ���۵ȴ�.
		 */

		try {
			// >>> 1. ����Ŭ ����̹� �ε� <<<
			/*
			     === OracleDriver(����Ŭ ����̹�)�� ���� ===
			     1). OracleDriver �� �޸𸮿� �ε������ش�.
			     2). OracleDriver ��ü�� �������ش�.
			     3). OracleDriver ��ü�� DriverManager�� ��Ͻ����ش�. 
			         --> DriverManager �� ���� ����̹����� Vector�� �����Ͽ� �������ִ� Ŭ�����̴�. 
			*/
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. � ����Ŭ������ ������ �ҷ�? (local �̳� remote��) <<<
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "myorauser", "eclass");
			// 27.0.0.1:1521 ==> IP:��Ʈ��ȣ ==> �������α׷�
			
			// >>> 3. ������ ����Ŭ����(conn)�� SQL��(����)�� ������ Statement ��ü (�����޺�) �����ϱ� <<<
			stmt = conn.createStatement();
			
			// >>> 4. SQL��(����)�� �ۼ��Ѵ�.
			String sql1 = " create table jdbc_tbl_memo "
						+ " ( no			number(4) not null "
						+ " , name		varchar2(20) "
						+ " , msg		varchar2(200) "
						+ " , writeday	date default sysdate "
						+ " , constraint PK_jdbc_tbl_memo primary key(no) "
						+ " ) ";	// ;create ���� ;�� �����Ѵ�. ���� ������
			
			String sql2 = " create sequence jdbc_seq_memo "
						+ " start with 1 "
						+ " increment by 1 "
						+ " nomaxvalue "
						+ " nominvalue "
						+ " nocycle "
						+ " nocache ";
			
			String sql3 = " insert into jdbc_tbl_memo(no, name, msg)"
						+ " values(jdbc_seq_memo.nextval, '�̼���', '�ȳ��ϼ���? �̼��� �λ�帳�ϴ�.') ";
			
			// >>> 5. Statement stmt ��ü (�����޺�)�� �ۼ��� SQL��(����)�� ����Ŭ������ ������ ������ �Ǿ������� �Ѵ�.
			boolean isSQL1 = stmt.execute(sql1);
			boolean isSQL2 = stmt.execute(sql2);
			boolean isSQL3 = stmt.execute(sql3);
			/*
				stmt.execute(sql1); �� �����ϸ� true �Ǵ� false �� ���´�.
				�Ķ���ͷ� ������ sql1 ���� select �� �϶��� ���������� ������ true �� ���Ͻ����ְ�,
				�� �̿��� DDL��(create, alter, drop, truncate) �̰ų� 
				DML��(insert, update, delete)�� ��쿡�� �����ߴٰ� �ϴ��� false�� ���Ͻ����ش�.
			 */
			System.out.println("Ȯ�ο� isSQL1 => " + isSQL1);
			// Ȯ�ο� isSQL1 => false // DDL ���̱� ������ �����ص� false�� ���´�
			
			System.out.println("Ȯ�ο� isSQL2 => " + isSQL2);
			// Ȯ�ο� isSQL2 => false
			
			System.out.println("Ȯ�ο� isSQL3 => " + isSQL3);
			// Ȯ�ο� isSQL3 => false
			
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar ������ ���ų� ���̺귯���� ��ϵ��� �ʾҽ��ϴ�.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. ����Ͽ��� �ڿ� �ݳ��ϱ� <<<
			// �ݳ��� ������ ���������� �������� �Ѵ�.
			
			try {
				if ( stmt != null ) stmt.close();
				if ( conn != null )conn.close();
			} catch (SQLException e) {
	
				e.printStackTrace();
			}
		}

		
	} // end of main()-----------------------------------------

}

