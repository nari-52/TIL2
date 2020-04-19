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

public class JdbcTest01_DDL {

	public static void main(String[] args) {
		
		// Statement �� PreparedStatement�� �����ϱ�		

		Connection conn = null;
		// Connection conn �� ����Ŭ �����ͺ��̽� ������ ������ �ξ��ִ� ��ü�̴�.
		
		PreparedStatement pstmt = null;
		// Connection conn�� ������ SQL��(����)�� PreparedStatement pstmt�� ���� ���۵ȴ�
		
		ResultSet rs = null;
		// select �Ǿ��� ������� �����ϴ� �뵵

		try {
			// >>> 1. ����Ŭ ����̹� �ε� <<<
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. � ����Ŭ������ ������ �ҷ�? (local �̳� remote��) <<<
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "myorauser", "eclass");
			// 127.0.0.1:1521 ==> IP:��Ʈ��ȣ ==> �������α׷�
			
			
			// ********** ���̺� �����ϱ� ********** //			
			// >>> 3. SQL��(����)�� �ۼ��Ѵ�.
			String sql = "select *\n"+
						 "from user_tables\n"+
						 "where table_name = ? ";
			
			// >>> 4. ������ ����Ŭ����(conn)�� SQL��(����)�� ������ Statement ��ü (�����޺�) �����ϱ� <<<
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "JDBC_TBL_MEMO2"); // �ش� �̸��� ���̺��� �ִ��� Ȯ���Ѵ�.
			
			// >>> 5. PreparedStatement pstmt ��ü(�����޺�)�� �ۼ��� SQL���� ����Ŭ ������ ������ ������ �Ǿ������� �Ѵ�.
			rs = pstmt.executeQuery(); // select ���� .executeQuery() ���
			
			if (rs.next()) {
				// JDBC_TBL_MEMO2 ���̺��� �����Ѵٶ��
				// ���� JDBC_TBL_MEMO2 ���̺��� drop �Ѵ�.
				pstmt.close(); // ����� ���ſ�				
				
				sql = "drop table JDBC_TBL_MEMO2 purge  ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.executeUpdate();
				// ����Ǿ��� sql���� DDL�� (create, alter, drop, truncate)�̶�� ���ϰ��� 0�� ���´�.
				rs.close(); // ����� ���ſ�
				
			}
			
			// �ش� ���̺��� ������ �ϳ� �����Ѵ�.
			sql = " create table jdbc_tbl_memo2 "
				+ " ( no		 number(4) not null "
				+ " , name		 varchar2(20) "
				+ " , msg		 varchar2(200) "
				+ " , writeday	 date default sysdate "
				+ " , constraint PK_jdbc_tbl_memo primary key(no) "
				+ " ) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			// ����Ǿ��� sql���� DDL�� (create, alter, drop, truncate)�̶�� ���ϰ��� 0�� ���´�.
			
			// ********** ������ �����ϱ� ********** //
			sql =   "select *\n"+
					"from user_sequences\n"+
					"where sequence_name = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "JDBC_SEQ_MEMO2");
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// ���� JDBC_SEQ_MEMO2 �������� �����Ѵٶ��
				// ���� JDBC_SEQ_MEMO2 �������� drop �Ѵ�.
				
				sql = " drop sequence JDBC_SEQ_MEMO2 ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				// ����Ǿ��� sql���� DDL��(create, alter, drop, truncate)�̶�� ���ϰ��� 0 �� ���´�.
				
			}
			// ���� JDBC_SEQ_MEMO2 �������� �������� ������ �������ش�.
			
			sql = " create sequence jdbc_seq_memo2 "
				+ " start with 1 "
				+ " increment by 1 "
				+ " nomaxvalue "
				+ " nominvalue "
				+ " nocycle "
				+ " nocache ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			
			sql = " insert into jdbc_tbl_memo2(no, name, msg)"
				+ " values(jdbc_seq_memo2.nextval, '�̼���', '�ȳ��ϼ���? �̼��� �λ�帳�ϴ�.') ";
			
			pstmt = conn.prepareStatement(sql);
			int n = pstmt.executeUpdate();
			// ����Ǿ��� sql���� DML��(insert, update, delete)�̶�� ���ϰ��� ����� ���ǰ����� ���´�. 
			
			System.out.println(n + "�� ���� �ԷµǾ����ϴ�.");

		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar ������ ���ų� ���̺귯���� ��ϵ��� �ʾҽ��ϴ�.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// >>> 6. ����Ͽ��� �ڿ� �ݳ��ϱ� <<<
			// �ݳ��� ������ ���������� �������� �Ѵ�.
			
			try {
				if ( rs != null) rs.close();
				if ( pstmt != null ) pstmt.close();
				if ( conn != null )conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	} // end of main()-----------------------------------------

}

