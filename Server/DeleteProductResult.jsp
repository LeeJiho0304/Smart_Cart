<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>

<%
	request.setCharacterEncoding("utf-8");
%>
<HTML>
<HEAD>
<TITLE>상품 삭제</TITLE>
</HEAD>
<BODY>
<%
	String id = request.getParameter("id");

	//db에 저장하기 

	Connection conn = null;
	PreparedStatement pstmt = null;

	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost/sample";
	
	int n = 0;
	
	try {
		try{
		// JDBC 드라이버 로드
		Class.forName("com.mysql.jdbc.Driver");
		//System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 검색 실패");
		}
		// 데이터베이스 연결정보를 이용해 Connection 인스턴스 확보
		try{
		conn = DriverManager.getConnection("jdbc:mysql://localhost/sample", "root", "com123");
		}catch(SQLException e) {
			System.out.println("sql 접속 실패");
		}
		String sql = "delete from product where product_id = ?";
		pstmt = conn.prepareStatement(sql);
				
		pstmt.setString(1, id);
		
		n = pstmt.executeUpdate();
		out.println(n);
		
	} catch(SQLException se)	{
		System.out.println(se.getMessage());
	}finally{
		pstmt.close();
		conn.close();
	}
%>
<script type="text/javascript">
	if(<%=n%> > 0)	{
		alert("정상적으로 삭제 되었습니다.");
		location.href="http://52.79.205.255:8080/Sample/ProductDelete.jsp";
	}
	else	{
		alert("삭제 실패했습니다.");
		history.go(-1);
	}
</script>
</body>
</html>