<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%
	request.setCharacterEncoding("utf-8");
%>
<%
	// 데이터베이스 연결관련 변수 선언
	Connection conn = null;
	PreparedStatement pstmt = null;
	// 데이터베이스 연결관련정보를 문자열로 선언
	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost/sample";
	try {
		// JDBC 드라이버 로드
		Class.forName(jdbc_driver);
		// 데이터베이스 연결정보를 이용해 Connection 인스턴스 확보
		conn = DriverManager.getConnection(jdbc_url, "root", "com123");
		}catch (Exception e) {
			System.out.println(e);
		}
%>
<HTML>
<HEAD>
<TITLE>nfc Query Result</TITLE>
</HEAD>
<BODY>
<%

			try {
				// select 문장을 문자열 형태로 구성한다.
				String sql = "select * from product";
				pstmt = conn.prepareStatement(sql);
				// select 문장을 수행하면 데이터정보가 ResultSet 클래스의 인스턴스로 리턴됨. 
				ResultSet rs = pstmt.executeQuery();
				// 마지막 데이터까지 반복함. 
				int flag = 0;
				while(rs.next()) {
					if(rs.getString("nfc_id").equals(request.getParameter("nfc_id"))) {
							flag = 1;
							String proName = rs.getString("product_name");
						    String proPrice = rs.getString("price");
						    
							
							JSONObject tempJson = new JSONObject();
							tempJson.put("proName", proName);
							tempJson.put("proPrice", proPrice);
							
							System.out.println(tempJson);
							out.print(tempJson);

					}
				}
				if(flag == 0)
				{
					out.println("일치하는 상품이 없습니다.");
				}
				// 사용한 자원의 반납. 
				rs.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		%>
	
</BODY>
</HTML>