package jdbc.day01.statement;

import java.sql.*;
import java.util.*;

public class JdbcTest05_Update {

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
						 "from jdbc_tbl_memo\n";
			
			// >>> 5. stmt ��ü�� SQL���� ����Ŭ������ ������ ����Ǿ������� �Ѵ�.
			rs = stmt.executeQuery(sql);
			
			
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

			} // end of while (rs.next())--------------------
			
			//////////////////////////////////////////////////////////////////////////////
			// === where ���� �ش��ϴ� �޴� �����ֱ�
			
			String menuNo = "";
			
			do {
				
				sql = "select no, name, msg\n"+
					  ",to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday   \n"+
					  "from jdbc_tbl_memo\n";
															
				System.out.println("\n -->>> ������ �����ϱ� <<<-- \n"
								 + " 1. ������ �۹�ȣ   2. ����\n"
								 + " -----------------------");
				
				System.out.print("�� �޴���ȣ�� �����ϼ��� : ");
				menuNo = sc.nextLine();
				
				switch (menuNo) {
					case "1" : // ������ �۹�ȣ
						System.out.print("��  ������ �۹�ȣ : ");
						String updateNo = sc.nextLine();	// ���� �۹�ȣ

						sql += " where no = " + updateNo;
						
						rs = stmt.executeQuery(sql);		
						
						if (rs.next()) {

							int no = rs.getInt(1);
							String name = rs.getString(2);
							String msg = rs.getString(3);
							String writeday = rs.getString(4);
							
							System.out.println("-----------------------------------------------------------");
						    System.out.println("�۹�ȣ\t�۾���\t�۳���\t\t\t�ۼ�����");
						    System.out.println("-----------------------------------------------------------");
						    							
							System.out.println(no +"\t"+name+"\t"+msg+"\t"+writeday );
							
							System.out.print("\n��  ������ �۳��� �Է� : ");
							String changMsg = sc.nextLine();	// ������ �۳���
													
							sql = " update jdbc_tbl_memo set msg = '"+changMsg+"'\n"+
								  " where no = " + updateNo ;
							
							int n = stmt.executeUpdate(sql);
							
							if (n == 1) {
								
								String yn = "";
								
								do {
									System.out.print(">> ������ �����Ͻðڽ��ϱ�? [Y/N] : ");
									yn = sc.nextLine();
									
									if ( "Y".equalsIgnoreCase(yn) ) {
										conn.commit();
										System.out.println(">>> ������ ���� ����! <<<");									
										break;
									}
									else if ( "N".equalsIgnoreCase(yn) ) {
										conn.rollback();
										System.out.println(">>> ������ ���� ����! <<<");									
										break;
									}
									else {
										System.out.println(">>> Y �Ǵ� N�� �Է� �����մϴ�.");
									}	
									
								} while (true);							
							} // end of if (n == 1)--------------------					

						}
					case "2" : // ����
						
						break;

					default:
						System.out.println(">> �޴��� ���� ��ȣ�� �����ϼ̽��ϴ�. \n ");
						break;
				} // end of switch (menuNo)-----------------

			} while (!"2".equals(menuNo));
			
		} catch (ClassNotFoundException e) {
			System.out.println(">> ojdbc6.jar ������ ���ų� ���̺귯���� ��ϵ��� �ʾҽ��ϴ�.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				try {
					// >>> 6. ����Ͽ��� �ڿ��ݳ��ϱ� <<<
					if (rs != null) rs.close();
					// SQLException ����ó���ϱ�
					if (stmt != null) stmt.close();
					if (conn != null) stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}			
		}
		
		sc.close();
		System.out.println(" ~~~~~~ ���α׷� ���� ~~~~~~");

	} // end of void main()-------------------------------

}
