<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>카트 연결 상황</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    
	<!-- BOOTSTRAP STYLES-->
    <link href="bootstrap.css" rel="stylesheet" />
     <!-- FONTAWESOME STYLES-->
    <link href="font-awesome.css" rel="stylesheet" />
        <!-- CUSTOM STYLES-->
    <link href="custom.css" rel="stylesheet" />
     <!-- GOOGLE FONTS-->
   <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />


</head>
<body>
     
           
          
    <div id="wrapper">
         <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="adjust-nav">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">
                        <img src="logo.png" />
                    </a>
                </div>
              
                
            </div>
        </div>
        <!-- /. NAV TOP  -->
        <nav class="navbar-default navbar-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="main-menu">
                 


                    <li >
                        <a href="index.html" ><i class="fa fa-desktop "></i>메뉴 </a>
                    </li>
                   

                    <li class="active-link">
                        <a href="CartConnectionStateList.jsp"><i class="fa fa-shopping-cart "></i>카트 연결 상황  </a>
                    </li>
                    <li>
                        <a href="list.jsp"><i class="fa fa-clipboard "></i>상품 보기  </a>
                    </li>
                    
                    <li>
                        <a href="insert.html"><i class="fa fa-plus "></i>상품 추가</a>
                    </li>
                    
                    <li>
                        <a href="ProductDelete.jsp"><i class="fa fa-minus "></i>상품 삭제</a>
                    </li>

                    <li>
                        <a href="ProductQueryResponse.jsp"><i class="fa fa-search "></i>상품 검색</a>
                    </li>
                    <li>
                        <a href="UserManagement.jsp"><i class="fa fa-users"></i>회원 관리</a>
                    </li>

                    
                    
                </ul>
                            </div>

        </nav>
        <!-- /. NAV SIDE  -->
        <div id="page-wrapper" >
          <div id="page-inner">
                <div class="row">
                    <div class="col-md-12">
                        <h2>카트 연결 상황</h2>
                    </div>
                </div>
                <!-- /. ROW  -->
                <hr />
                
                <hr>
                <div class="row">
                    <div class="col-lg-6 col-md-6">
        
                        <table class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>카트번호</th>
                                    <th>연결상태</th>
                             
                                </tr>
                            </thead>
                            <tbody>
	<%
		//db에서 상품정보 얻어와 테이블에 출력하기
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String jdbc_driver = "com.mysql.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://localhost/sample";
		
		
		try	{
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
			
			String sql = "select * from cart";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next())	{
				String id = rs.getString("cart_id");
				int isUse = rs.getInt("cart_use");
				
				if(isUse == 0) {
					out.println("<tr><td width='100'>" + id + "</td><td>사용가능</td></tr>");
				}
				else{
					out.println("<tr><td width='100'>" + id + "</td><td>사용중</td></tr>");
				}
			}
			
		} catch(SQLException se)	{
			System.out.println(se.getMessage());
		}finally{
			rs.close();
			pstmt.close();
			conn.close();
		}
		 
	%>
</tbody>
</table>
 </div>
                   
            </div>
         <!-- /. PAGE WRAPPER  -->
        </div>
    <div class="footer">
      
    
             <div class="row">
                <div class="col-lg-12" >
                    &copy;  2017 DongdukMart.com | Project By : ComputerSience 이아륜 이지호 양혜임 위혜진
                </div>
        </div>
        </div>
          

     <!-- /. WRAPPER  -->
    <!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
    <!-- JQUERY SCRIPTS -->
    <script src="jquery-1.10.2.js"></script>
      <!-- BOOTSTRAP SCRIPTS -->
    <script src="bootstrap.min.js"></script>
      <!-- CUSTOM SCRIPTS -->
    <script src="custom.js"></script>
    
</body>
</html>