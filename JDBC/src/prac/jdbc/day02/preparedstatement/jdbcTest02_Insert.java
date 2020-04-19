package prac.jdbc.day02.preparedstatement;

/*	
	==== Statement �� PreparedStatement ====
	Statement �� PreparedStatement �� ���� ū �������� ĳ�� ������� �̴�.
	�̵��� �������� (SQL��)�� �м�([==�Ľ� parsing], �����˻�, �ε��� �������) �ϰ� ������ �� ����ȴ�.
	Statement�� �Ź� �������� (SQL��)�� ������ �� ���� ��� �ܰ� (�Ľ� parsing)�� ��ġ����
	PreparedStatement �� ó�� �ѹ��� ��� �ܰ� (�Ľ� parsing)�� ������ �� ĳ�ÿ� ��� �����Ѵ�.
	�׷��Ƿ� ������ �������� (SQL��)�� ����� PreparedStatement �� DB�� �ξ� ���� ���ϸ� �ֹǷ� ������ ��������.
	
	���� Statement �� ����ڰ� �Է��� �ܾ�(�˻��� �Ǵ� �Է´ܾ�)���� ����������
	PreparedStatement�� ��ġȦ��(?)�� ����ϹǷ� �Է��� �ܾ�(�˻��� �Ǵ� �Է´ܾ�)���� �������� �����Ƿ�
	Statement ���� PreparedStatement �� ���ȼ��� ���� PreparedStatement �� �ַ� ����Ѵ�.
*/

import java.sql.*;
import java.util.*;

public class jdbcTest02_Insert {

	public static void main(String[] args) {
		
		// Statement �� PreparedStatement�� �����ϱ�	
		
		Connection conn = null;
		// Connection conn �� ����Ŭ �����ͺ��̽� ������ ������ �ξ��ִ� ��ü�̴�.
		
		PreparedStatement pstmt = null;
		/*
			�����ڰ� �ۼ��� SQL���� ��� ����Ŭ �������� �����ؾ����� �����ؾ��Ѵ�.
			�̋�, ����Ŭ������ Connection conn���� �˰�,
			Connection conn�� ������ SQL���� Statement stmt�� ���� ���۵ȴ�.
		*/

		Scanner sc = new Scanner(System.in);
				
		try {
			// >>> 1. ����Ŭ ����̹� �ε� <<<
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. � ����Ŭ ������ ������ ������ ����
			System.out.print("�� ������ ����Ŭ ������ IP�ּ� : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" +ip+ ":1521:xe", "myorauser", "eclass");
			// 127.0.0.1:1521 ==> IP:��Ʈ��ȣ ==> �������α׷�
			
			// >>> ����Ŀ������ ��ȯ�ϱ�
			conn.setAutoCommit(false);
			
			// >>> 3. SQL�� �ۼ��ϱ� <<<
			System.out.print("�� �۾��� : ");
			String name = sc.nextLine();
			
			System.out.print("�� �۳��� : ");
			String msg = sc.nextLine();
			
			String sql = "insert into jdbc_tbl_memo2(no, name, msg)\n"+
						 "values (JDBC_SEQ_MEMO2.nextval, ?, ?)";

			// >>> 4. ������ ����Ŭ����(conn)�� SQL���� ������ Statement ��ü �����ϱ� <<<
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  name);  // 1�� ?�� �۾��� �־��
			pstmt.setString(2, msg);	// 2�� ?�� �۳��� �־��
			
			System.out.println("sql : " + sql);
			// �Է��� ������ name�� msg�� ���ȵǾ� ? �� ��µȴ�.
			
			// >>> 5. PreparedStatement pstmt ��ü�� �ۼ��� SQL���� ����Ŭ������ ������ ����Ǿ������� �Ѵ�
			int n = pstmt.executeUpdate(); // 4�� �������� sql���� �̹� pstmt�� ����ִ�.
			
			/*
				pstmt.executeUpdate(); ����
				����Ǿ��� sql���� DDL�� (create, alter, drop, truncate) �̶�� ���ϰ� 0�� ���´�.
				����Ǿ��� sql���� DML�� (insert, update, delete) �̶�� ���ϰ��� ����� ���ǰ����� ���´�. 
			 */
			
			System.out.println(n + "�� ���� �Էµ�\n");
			
			if (n==1) {
				
				do {
					System.out.print("�� ������ �Խñ��� ���ε� �Ͻðڽ��ϱ�? [Y/N] : ");
					String yn = sc.nextLine();
					
					if ("Y".equalsIgnoreCase(yn)) {
						conn.commit();
						System.out.println(">> ���ε� ����!! <<\n");
						break;						
					}
					else if ("N".equalsIgnoreCase(yn)) {
						conn.rollback();
						System.out.println(">> ���ε� ��� !! <<\n");
						break;
					}
					else {
						System.out.println(">> [����] Y �Ǵ� N �� �Է� �����մϴ�! << \n");
					}
					
				} while (true);

			} // end of if (n==1)-------------------------------
			else {
				System.out.println(">> ������ �Է¿� ������ �߻��Ͽ����ϴ�. << \n");
			}
			
			conn.setAutoCommit(true); // �ڵ�Ŀ������ ���󺹱�
			

		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar ������ ���ų� ���̺귯���� ��ϵ��� �ʾҽ��ϴ�.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. ����Ͽ��� �ڿ� �ݳ��ϱ� <<<
			// �ݳ� ������ ���������� �������� �Ѵ�.
			
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		sc.close();
		System.out.println("~~~~~~~~~ ���α׷� ���� ~~~~~~~~");

	} // end of void main()----------------------------------

}
