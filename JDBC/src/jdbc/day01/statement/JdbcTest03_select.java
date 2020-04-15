package jdbc.day01.statement;

import java.sql.*;
import java.util.*;

public class JdbcTest03_select {

	public static void main(String[] args) {
		
		Connection conn = null;
		// Connection conn�� ����Ŭ �����ͺ��̽� ������ ������ �ξ��ִ� ��ü
		
		Statement stmt = null;
		// conn�� ������ SQL���� Statement stmt�� ���� ���۵ȴ�.
		
		ResultSet rs = null;
		// select �Ǿ��� ������� �����ϴ� �뵵
		
		Scanner sc = new Scanner (System.in);
		
		
		
		try {
			// >>> 1. ����Ŭ ����̹� �ε�
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// ClassNotFoundException ����ó���ϱ�
			
			// >>> 2. � ����Ŭ ������ ��������?
			System.out.print("�� ������ ����Ŭ ������ IP �ּ� : ");
			String ip = sc.nextLine();
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":1521:xe", "myorauser", "eclass");
			// SQLException ����ó���ϱ�
			
			conn.setAutoCommit(false); 
			// ���� Ŀ������ ��ȯ
			
			// >>> 3. Statement ��ü �����ϱ� <<<
			stmt = conn.createStatement();
			
			// >>> 4. SQL�� �强
			String sql = "select no, name, msg\n"+
						 ",to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday   \n"+
						 "from jdbc_tbl_memo\n"+
						 "order by no desc";
			
			// >>> 5. stmt ��ü�� SQL���� ����Ŭ������ ������ ����Ǿ������� �Ѵ�.
			rs = stmt.executeQuery(sql);
			/*
				stmt.executeQuery(sql);���� �Ķ������ sql�� ������ select ���� ���´�.
				stmt.executeQuery(sql); �� �����ϸ� select �Ǿ��� ������� �������µ�
				�� Ÿ���� ResultSet ���� �����´�.
				
				=== �������̽� ResultSet�� �ֿ��� �޼ҵ� ===
				1. next() 	: select �Ǿ��� ��������� Ŀ���� �������� �÷��ִ� �� 			����Ÿ�� boolean
				2. first()	: select �Ǿ��� ��������� Ŀ���� ���� ó������ �Ű��ִ� ��		����Ÿ�� boolean
				3. last() 	: select �Ǿ��� ��������� Ŀ���� ���� ���������� �Ű��ִ� �� 		����Ÿ�� boolean
				
				== Ŀ���� ��ġ�� �࿡�� �÷��� ���� �о���̴� �޼ҵ�
				getInt(����) : �÷�Ÿ���� �����̸鼭 ������ �о���϶�
								�Ķ���� ���ڴ� �÷��� ��ġ ��
				
				getInt(����) : �÷��� Ÿ���� �����̸鼭 ������ �о���̶�
			                  	�Ķ���� ���ڴ� �÷��� �Ǵ� alias�� 
			                  
			   getLong(����) : �÷��� Ÿ���� �����̸鼭 ������ �о���̶�
			                    	�Ķ���� ���ڴ� �÷��� ��ġ�� 
			                 
			   getLong(����) : �÷��� Ÿ���� �����̸鼭 ������ �о���̶�
			                     	�Ķ���� ���ڴ� �÷��� �Ǵ� alias��                
			   
			   getFloat(����) : �÷��� Ÿ���� �����̸鼭 �Ǽ��� �о���̶�
			                      	�Ķ���� ���ڴ� �÷��� ��ġ�� 
			                 
			   getFloat(����) : �÷��� Ÿ���� �����̸鼭 �Ǽ��� �о���̶�
			                     	�Ķ���� ���ڴ� �÷��� �Ǵ� alias�� 
			                      
			   getDouble(����) : �÷��� Ÿ���� �����̸鼭 �Ǽ��� �о���̶�
			                       	�Ķ���� ���ڴ� �÷��� ��ġ�� 
			                 
			   getDouble(����) : �÷��� Ÿ���� �����̸鼭 �Ǽ��� �о���̶�
			                     	�Ķ���� ���ڴ� �÷��� �Ǵ� alias��    
			                        
			   getString(����) : �÷��� Ÿ���� ���ڿ��� �о���̶�
			                     	�Ķ���� ���ڴ� �÷��� ��ġ�� 
			                 
			   getString(����) : �÷��� Ÿ���� ���ڿ��� �о���̶�
			                     	�Ķ���� ���ڴ� �÷��� �Ǵ� alias��     
				
			 */
			
			System.out.println("------------------------------------------------------");
			System.out.println(" �۹�ȣ\t�۾���\t�۳���\t\t�ۼ�����");
			System.out.println("------------------------------------------------------");
			
			
			while (rs.next()) {
				// rs.next()�� select �Ǿ��� ��������� Ŀ���� ��ġ(���� ��ġ)�� �������� �ű� ��
				// ���� �����ϸ� true, ���� ������ false�� ���Ͻ����ش�.
				
				int no = rs.getInt(1);				// �Ǵ� rs.getInt("no");	�Ǵ� rs.getInt("NO"); �÷��̸��� ��ҹ��� ���� ����.
				String name = rs.getString(2);		// �Ǵ� rs.getString("name");		�Ǵ� rs.getString("NAME");
				String msg = rs.getString(3);		// �Ǵ� rs.getString("msg");		�Ǵ� rs.getString("MSG");
				String writeday = rs.getString(4);	// �Ǵ� rs.getString(writeday);	�Ǵ� rs.getString(WRITEDAY);
				
				System.out.println(no + "\t" +name+ "\t" +msg+ "\t"+writeday);

			}		

			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					if (rs != null) rs.close();
					// SQLException ����ó���ϱ�
					if (stmt != null) stmt.close();
					if (conn != null) stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}			
		}
		
		sc.close();
		System.out.println(" ~~~~~~ ���� ~~~~~~");

	} // end of void main()-------------------------------

}
