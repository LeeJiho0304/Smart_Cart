<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>상품 검색</title>
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
                   

                    <li>
                        <a href="CartConnectionStateList.jsp"><i class="fa fa-shopping-cart "></i>카트 연결 상황 </a>
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


                    <li class="active-link">
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
                        <h2>상품 검색</h2>
                    </div>
                </div>
                <!-- /. ROW  -->
                <hr />
                <div class="row">
                    <div class="col-lg-4 col-md-4">
                    <div class="panel-group" id="accordion">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" class="collapsed">상품이름 검색</a>
                                    </h4>
                                </div>
                                <div id="collapseOne" class="panel-collapse collapse" style="height: 0px;">
                                    <div class="panel-body">
                                        <div class="form-group">
                       				<form method="post"	action="ProductQueryResult.jsp">
									<fieldset id="regbox">
									
								<input class="form-control" type="text" name="product_name"/><br/>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">종류 검색</a>
                                    </h4>
                                </div>
                                <div id="collapseTwo" class="panel-collapse in" style="height: auto;">
                                    <div class="panel-body">
                                        <input class="form-control" type="text" name="kind_name"/><br/>

                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" class="collapsed">브랜드 검색</a>
                                    </h4>
                                </div>
                                <div id="collapseThree" class="panel-collapse collapse">
                                  

                                        <div class="panel-body">
                                            <input class="form-control" type="text" name="brand_name"/><br/>
                                        </div>
                                </div>
                            </div>
                        </div>
                        
			<input class="btn btn-success" type="submit" value="검색"/>
			<input class="btn btn-default" type="reset" value="취소"/>
		</fieldset>
	</form>
     <!-- /. ROW  --> 
    </div>
             <!-- /. PAGE INNER  -->
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