package prac.jdbc.day01.statement;

import java.sql.*;
import java.util.*;

public class jdbcTest02_Insert {

	public static void main(String[] args) {
		
		Connection conn = null;
		// Connection conn �� ����Ŭ �����ͺ��̽� ������ ������ �ξ��ִ� ��ü�̴�.
		
		Statement stmt = null;
		/*
			�����ڰ� �ۼ��� SQL���� ��� ����Ŭ �������� �����ؾ����� �����ؾ��Ѵ�.
			�̋�, ����Ŭ������ Connection conn���� �˰�,
			Connection conn�� ������ SQL���� Statement stmt�� ���� ���۵ȴ�.
		*/

		Scanner sc = new Scanner(System.in);
		
		
		try {
			// >>> 1. ����Ŭ ����̹� �ε� <<<
			/*
				=== OracleDriver(����Ŭ ����̹�)�� ���� ===
				1) OracleDriver �� �޸𸮿� �ε������ش�.
				2) OracleDriver ��ü�� �������ش�.
				3) OracleDriver ��ü�� DriverManager�� ��Ͻ����ش�.
					--> DriverManager�� ���� ����̹����� Vector�� �����Ͽ� �������ִ� Ŭ�����̴�.
			 */
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. � ����Ŭ ������ ������ ������ ����
			System.out.print("�� ������ ����Ŭ ������ IP�ּ� : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" +ip+ ":1521:xe", "myorauser", "eclass");
			// 127.0.0.1:1521 ==> IP:��Ʈ��ȣ ==> �������α׷�
			
			// >>> ����Ŀ������ ��ȯ�ϱ�
			conn.setAutoCommit(false);
			
			// >>> 3. ������ ����Ŭ����(conn)�� SQL���� ������ Statement ��ü �����ϱ� <<<
			stmt = conn.createStatement();
			
			// >>> 4. SQL�� �ۼ��ϱ� <<<
			System.out.print("�� �۾��� : ");
			String name = sc.nextLine();
			
			System.out.print("�� �۳��� : ");
			String msg = sc.nextLine();
			
			String sql = "insert into jdbc_tbl_memo (no, name, msg)\n"+
						 "values (JDBC_SEQ_MEMO.nextval, '"+ name +"', '"+msg+"'  )";
			System.out.println("sql : " + sql);
			
			// >>> 5. Statement stmt ��ü�� �ۼ��� SQL���� ����Ŭ������ ������ ����Ǿ������� �Ѵ�
			int n = stmt.executeUpdate(sql);
			/*
				stmt.executeUpdate(sql); ����
				�Ķ���ͷ� ������ sql�� ������ DML(insert, update, delete)�� �� ����� �����ϴ�.
				stmt.executeUpdate(sql); �� ������ ����� int Ÿ������ ���ڸ� �����ִµ�
				sql Ÿ���� insert ��� insert �Ǿ��� ���� ������ �����ְ�,
				sql Ÿ���� update ��� update �Ǿ��� ���� ������ �����ְ�,
				sql Ÿ���� delete ��� delete �Ǿ��� ���� ������ �����ش�.
			 */
			
			System.out.println(n + "�� ���� �Էµ�");
		
			
			if (n==1) {
				
				do {
					
					System.out.print("�� ������ �Է� �Ͻðڽ��ϱ�? [Y/N] =>");
					String yn = sc.nextLine();
					
					if ("Y".equalsIgnoreCase(yn)) {
						conn.commit(); // Ŀ��!
						System.out.println(" >> ������ �Է� �����Ͽ����ϴ�. <<\n");
						break;
					}
					else if ("N".equalsIgnoreCase(yn)) {
						conn.rollback(); // �ѹ�! ���
						System.out.println(" >> ������ �Է� ��� <<\n");
						break;
					}
					else {
						System.out.println(" >> [����] Y �Ǵ� N�� �Է��ϼ���! <<\n");
					}
											
				} while (true);

			}
			
			else {
				System.out.println(" >> [����] ������ �Է� ���� �߻� <<");
			}
			
			conn.setAutoCommit(true); // �ڵ�Ŀ������ ���󺹱���

		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar ������ ���ų� ���̺귯���� ��ϵ��� �ʾҽ��ϴ�.");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// >>> 6. ����Ͽ��� �ڿ� �ݳ��ϱ� <<<
			// �ݳ� ������ ���������� �������� �Ѵ�.
			
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		sc.close();
		System.out.println("~~~~~~~~~ ���α׷� ���� ~~~~~~~~");

	} // end of void main()----------------------------------

}
