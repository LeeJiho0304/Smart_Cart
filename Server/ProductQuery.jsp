<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%
	request.setCharacterEncoding("utf-8");
%>
<HTML>
<HEAD>
<TITLE>상품 검색 결과</TITLE>
</HEAD>
<BODY>
<%
	String name = request.getParameter("product_name");
	String brname = request.getParameter("brand_name");
	String kname = request.getParameter("kind_name");
	
	Connection conn = null;
	PreparedStatement pstmt = null;

	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost/sample";
	
	try {
		// JDBC 드라이버 로드
		Class.forName(jdbc_driver);
		
		// 데이터베이스 연결정보를 이용해 Connection 인스턴스 확보
		conn = DriverManager.getConnection(jdbc_url, "root", "com123");
		
		// select 문장을 문자열 형태로 구성한다.
		String sql = "select product.product_id, product.product_name, product.price, brand.brand_name, kind.kind_name from product, brand, kind where product.brand_id = brand.brand_id and kind.kind_id = brand.kind_id";
		pstmt = conn.prepareStatement(sql);
		
		// select 문장을 수행하면 데이터정보가 ResultSet 클래스의 인스턴스로 리턴됨. 
		ResultSet rs = pstmt.executeQuery();
		
		// 마지막 데이터까지 반복 함. 
				int flag = 0;
				int i = 0;
				
				String proName = null; 
				String proId = null; 
			    String proPrice = null;
			    String proBrandName = null;
				
			    JSONObject Json = new JSONObject();
				JSONArray itemList = new JSONArray();
				
				
				
				while(rs.next()) {
					if(name != "") {
						if(rs.getString("product_name").equals(name)) {
							
							flag = 1;
							proName = rs.getString("product_name");
							proId = rs.getString("product_id");
						    proPrice = rs.getString("price");
						    proBrandName = rs.getString("brand_name");
						    
						    JSONObject tempJson = new JSONObject();
						    
							tempJson.put("proName", proName);
							tempJson.put("proId", proId);
							tempJson.put("proPrice", proPrice);
							tempJson.put("proBrandName", proBrandName);
							itemList.add(tempJson);
							i++;
							
						}
					}
					else if(brname != ""){
						if(rs.getString("brand_name").equals(brname)) {
								flag = 1;
								proName = rs.getString("product_name");
								proId = rs.getString("product_id");
							    proPrice = rs.getString("price");
							    proBrandName = rs.getString("brand_name");
							    
							    JSONObject tempJson = new JSONObject();
							    
							    tempJson.put("proName", proName);
								tempJson.put("proId", proId);
								tempJson.put("proPrice", proPrice);
								tempJson.put("proBrandName", proBrandName);
								itemList.add(tempJson);
						
								i++;
							}
					}
					else{
						if(rs.getString("kind_name").equals(kname)) {
							flag = 1;
							proName = rs.getString("product_name");
							proId = rs.getString("product_id");
						    proPrice = rs.getString("price");
						    proBrandName = rs.getString("brand_name");
						    
						    JSONObject tempJson = new JSONObject();
						    
						    tempJson.put("proName", proName);
							tempJson.put("proId", proId);
							tempJson.put("proPrice", proPrice);
							tempJson.put("proBrandName", proBrandName);
							itemList.add(tempJson);
					
							i++;
						}
					}
				}
				
				if(flag == 0)
				{
					out.println("해당상품이 없습니다.");
				}
				else{
					Json.put("Total", i);
					Json.put("List", itemList);
					
					System.out.println(Json);
					out.print(Json);
				}
				// 사용한 자원의 반납. 
				rs.close();
				pstmt.close();
				conn.close();
			} 
			catch (Exception e) {
				System.out.println(e);
			}
%>
</BODY>
</HTML>