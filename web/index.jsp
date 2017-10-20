<%@ page import="com.model.User" %>
<%--
  Created by IntelliJ IDEA.
  User: Sang
  Date: 15/11/16
  Time: 下午6:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
  <% User user = (User)request.getSession().getAttribute("user");
      System.out.println(user);
      if(user==null){
        response.sendRedirect(request.getContextPath()+"/login.html");
      }
  %>
  <meta charset="utf-8">
  <title>都印网后台主页</title>
  <link  href="easyui/bootstrap/easyui.css" rel="stylesheet">
  <link  href="easyui/icon.css" rel="stylesheet">
  <script src="easyui/jquery-2.1.1.min.js"></script>
  <script src="easyui/jquery.easyui.min.js"></script>
  <script src="easyui/easyui-lang-zh_CN.js"></script>
  <link rel="stylesheet"  href="css/index.css">
</head>
<body>
<div id="main">
  <div id="header">
    <div class="head-login">
      <ul >
        <li><a  id="username" href="javascript:void(0)">username</a></li>
      </ul>
    </div>
    <ul class="head">
      <li class="logo">
        <img src="img/logo.jpg"></li>
      <li class="tel-img">
        <p class="tel">400-821-7686</p>
        <p class="time">客服时间：09:00--20:00</p>
      </li>
    </ul>
    <div class="list-menu">
      <ul>
        <li class="active"><a href="index.html">产品参数管理</a></li>
      </ul>
    </div>
  </div>
  <div class="container">
    <div class="side ">
      <!--<p>产品报价</p>-->
      <ul class="self">
        <li class="active">
          <a class="mp " onclick="(function(){$('.inner0').attr('src','machine.html') })()">机器</a>
        </li>
        <li>
          <a  class="dy" onclick="(function(){$('.inner0').attr('src','paper.html') })()">纸张</a>
        </li>
        <li>
          <a  class="bgj" onclick="(function(){$('.inner0').attr('src','sticker.html') })()" >不干胶</a>
        </li>
        <li>
          <a class="wtld" onclick="(function(){$('.inner0').attr('src','craft.html') })()">后道</a>
        </li>
        <li>
          <a class="ze" onclick="(function(){$('.inner0').attr('src','decoration.html') })()">价格修改</a>
        </li>
      </ul>
    </div>
    <div id="p" class="easyui-panel" style="width: 1045px;height: 670px;">
      <iframe class="inner0" style="height:99%; width: 100%;border: none;" src="machine.html">
      </iframe>

    </div>

  </div>
  <div class="copyright" style="border-top: 1px solid #ccc;width:100%">
    <p>电话：4008-217-686   电子邮箱： cuishh@163.com</p>
    <p> Copyright ©2013 duyinwang.com, All Rights Reserved 沪ICP备13038666号-3	 都印网 版权所有 </p>
  </div>
</div>

<script>
  $(function(){
    var height = $(document).height();
    $('#mask').css('height',height);
    $(".self li").click(function () {
      $(this).addClass('active').siblings().removeClass('active');
    })
  })
</script>
</body>
</html>
