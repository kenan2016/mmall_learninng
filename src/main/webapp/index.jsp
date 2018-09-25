<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/9/26
  Time: 0:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试页面</title>
</head>
<body>
springMVC 文件上传

<form name="form1" method="post" action="/manage/product/upload.do" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <%--这里的upload_file 一定要和value = "upload_file", required = false) MultipartFile multipartFile保持一致--%>
    <input type="submit" value="上传文件">
</form>


richtext_img_upload.do

富文本图片上传
<form name="form1" method="post" action="/manage/product/richtext_img_upload.do" enctype="multipart/form-data">
    <input type="file" name="richtext_img_upload" />
    <%--这里的upload_file 一定要和value = "upload_file", required = false) MultipartFile multipartFile保持一致--%>
    <input type="submit" value="上传文件">
</form>
</body>
</html>
