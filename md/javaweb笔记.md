```java
//指定response的content-type为text/plain，也可以指定为json等
@Produces("text/plain")
//指定访问路径为demo
@Path("demo")
```



### 域的作用范围：

ServletContext设置了属性之后，只要web没有停止，则在整个web中都可通过获取context来获取到属性

```java
servletContext.setAttribute("type","one");
servletContext.gettAttribute("type");
```

### http协议（超文本传输协议）

指客户端和服务器之间通信时需要遵守的规则，http中的数据称之为报文。

#### 请求的http协议格式

- GET请求
  - 请求行：
    - 请求方式：	GET
    - 请求的资源路径：/login.html
    - 请求的协议版本：HTTP/1.1
  - 请求头：
    - Accept：告诉服务器客户端可以接受的数据类型
    - Accept-Language：zh-CN/en_US
    - User-Agent：用户代理，一般用浏览器访问就是浏览器信息
    - Accept-Encoding：服务器可接受的数据编码压缩格式
    - Host：服务器的IP和端口
    - Connection：告诉服务器请求连接如何处理
      - Keep-Alive：告诉服务器保持一段时间连接后断开
      - Closed：马上关闭
- POST请求
  - 请求行：
    - 请求方式：POST
    - 请求资源路径：
    - 请求协议版本号：
  - 请求头：
    - Accept：告诉服务器客户端可以接受的数据类型
    - Accept-Language：zh-CN/en_US
    - Referer：表示请求发起时，浏览器地址栏中的地址从哪来
    - User-Agent：用户代理，一般用浏览器访问就是浏览器信息
    - Content-Type：表示发送的数据类型
    - Content-Length:发送数据的长度
    - Cache-Control：表示如何控制缓存
      - no-cache：无缓存
    - 空行:
  - 请求体：发送给服务器的数据



GET请求：

1. form标签 method=get
2. a标签
3. link标签引入css
4. script标签引入js
5. img引入图片
6. iframe引入html页面
7. 在浏览器中输入地址后敲回车

POST请求：

1. form标签中 method=post

#### 响应码

```
200			请求成功
302			请求重定向
404			请求服务器已收到，但页面未找到
500			服务器内部错误
```

#### MIME类型数据说明

``` 
超文本标记语言文本 .html,.html text/html 
普通文本 .txt text/plain 
RTF文本 .rtf application/rtf 
GIF图形 .gif image/gif 
JPEG图形 .jpeg,.jpg image/jpeg 
au声音文件 .au audio/basic 
MIDI音乐文件 mid,.midi audio/midi,audio/x-midi 
RealAudio音乐文件 .ra, .ram audio/x-pn-realaudio 
MPEG文件 .mpg,.mpeg video/mpeg 
AVI文件 .avi video/x-msvideo 
GZIP文件 .gz application/x-gzip 
TAR文件 .tar application/x-tar
```



doPost请求参数中存在乱码，则需要在获取参数前设置编码格式：

```java
//设置请求体编码格式为utf-8
req.setCharacterEncoding("utf-8");
String username = req.getParameter("username");
```

跳转转发

```java
//跳转到根目录下的next.html，根目录下的next.html
req.getRequestDispatcher("/next.html").forward(req,resp);
```

设置基础目录

```java
	这样做就相当于设置了当前路径，而不是浏览器的访问路径
    <base href="http://localhost:8080/Test1/a/b/c.html">
```

文件流的使用：字节流和字符流只能用一个，不能同时使用

### 乱码问题解决：

1.

```java
//服务器端设置编码为utf-8
resp.setCharacterEncoding("utf-8");
//通过响应头，设置浏览器也为utf-8
resp.setHeader("Content-Type","text/html; charset-UTF-8");

//或者下面一步到位,但是下面这种方法需要在获取流之前使用
resp.setContentType("text/html; charset-UTF-8");
```

```java
//自定义过滤器解决乱码
//xml中配置过滤器
 package com.fuck.web.filter;
 
 import java.io.IOException;
 import java.io.UnsupportedEncodingException;
 import java.util.Map;
 
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletRequestWrapper;
 
 /**
  * 解决get和post请求 全部乱码
  * 
  * @author seawind
  * 
  */
 public class GenericEncodingFilter implements Filter {
 
     @Override
     public void destroy() {
     }
 
     @Override
     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         // 转型为与协议相关对象
         HttpServletRequest httpServletRequest = (HttpServletRequest) request;
         // 对request包装增强
         HttpServletRequest myrequest = new MyRequest(httpServletRequest);
         response.setContentType("text/html;charset=utf-8");
         chain.doFilter(myrequest, response);
     }
 
     @Override
     public void init(FilterConfig filterConfig) throws ServletException {
     }
 
 }
 
 // 自定义request对象
 class MyRequest extends HttpServletRequestWrapper {
 
     private HttpServletRequest request;
 
     private boolean hasEncode;
 
     public MyRequest(HttpServletRequest request) {
         super(request);// super必须写
         this.request = request;
     }
 
     // 对需要增强方法 进行覆盖
     @Override
     public Map getParameterMap() {
         // 先获得请求方式
         String method = request.getMethod();
         if (method.equalsIgnoreCase("post")) {
             // post请求
             try {
                 // 处理post乱码
                 request.setCharacterEncoding("utf-8");
                 return request.getParameterMap();
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }
         } else if (method.equalsIgnoreCase("get")) {
             // get请求
             Map<String, String[]> parameterMap = request.getParameterMap();
             if (!hasEncode) { // 确保get手动编码逻辑只运行一次
                 for (String parameterName : parameterMap.keySet()) {
                     String[] values = parameterMap.get(parameterName);
                     if (values != null) {
                         for (int i = 0; i < values.length; i++) {
                             try {
                                 // 处理get乱码
                                 values[i] = new String(values[i].getBytes("ISO-8859-1"), "utf-8");
                             } catch (UnsupportedEncodingException e) {
                                 e.printStackTrace();
                             }
                         }
                     }
                 }
                 hasEncode = true;
             }
             return parameterMap;
         }
 
         return super.getParameterMap();
     }
 
     @Override
     public String getParameter(String name) {
         Map<String, String[]> parameterMap = getParameterMap();
         String[] values = parameterMap.get(name);
         if (values == null) {
             return null;
         }
         return values[0]; // 取回参数的第一个值
     }
 
     @Override
     public String[] getParameterValues(String name) {
         Map<String, String[]> parameterMap = getParameterMap();
         String[] values = parameterMap.get(name);
         return values;
     }
 
 }

```

```xml
<!--tomcat配置文件解决乱码-->
<Connector port="8080" maxThreads="150" minSpareThreads="25" maxSpareThreads="75"
    enableLookups="false" redirectPort="8443" acceptCount="100"
    connectionTimeout="20000" disableUploadTimeout="true" URIEncoding="UTF-8" />
```

```xml
<!--spring配置乱码-->
    <filter>
        <filter-name>encoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>/</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encoding</filter-name>
        <url-pattern>/</url-pattern>
    </filter-mapping>
```

#### spring解决json乱码问题

**单个方法通过RequestMapping解决json乱码**

```java
//RequestMapping设置produces utf-8解决json中文乱码
@RequestMapping(value="/hello",produces="application/json;charset=utf-8")
```

**spring.xml文件中全局配置解决json乱码**

```xml
	
<!--spring设置json乱码-->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">


    <context:component-scan base-package="com.huang.controller"/>
    <mvc:default-servlet-handler/>
    <!--设置json乱码，目前会报错-->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
    <!--设置视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
</beans>
```



### 请求重定向1

服务器升级页面重定向，在请求头设置了status和Location后

	1. 浏览器会再发出Location地址的请求，地址栏地址变为Location中的地址
	2. 不共享request域中的数据：
	3. 无法访问WEB-INF下的数据
	4. 可访问工程外的资源（http://www.baidu.com）

```java
//设置响应状态码302,代表重定向
resp.setStatus(302);
//设置请求头
resp.setHeader("Location","http://localhost:8080/Test1/response2");
```

### 请求重定向2

```java
resp.sendRedirect("http://localhost:8080")
```



# EL表达式

```html
<!--写java代码块-->
<%
   	Person person = new Person();
   	person.name="hdt";
   pageContext.setAttribute("p",person)
%>
    <!--用上面的属性p来使用对象 ,调用元素，获取类的成员变量则必须声明成员的get方法-->
    输出：${p.name}
```



### 数据库数据导出到excel提供下载

POI导出excel的三种workbook的区别

三种workbook分别是HSSFWorkbook、XSSFWorkbook、SXSSFWorkbook

**第一种：HSSFWorkbook**

针对EXCEL 2003版本，扩展名为.xls，此种的局限就是导出的行数最多为65535行。因为导出行数受限，不足7万行，所以一般不会发送内存溢出(OOM)的情况

**第二种：XSSFWorkbook**

这种形式的出现是由于第一种HSSF的局限性产生的，因为其导出行数较少，XSSFWorkbook应运而生，其对应的是EXCEL2007+ ，扩展名为.xlsx ，最多可以导出104万行，不过这样就伴随着一个问题–OOM内存溢出。因为使用XSSFWorkbook创建的book sheet row cell 是存在内存中的，并没有持久化到磁盘上，那么随着数据量的增大，内存的需求量也就增大。那么很有可能出现 OOM了，那么怎么解决呢？

**第三种:SXSSFWorkbook** 　poi.jar 3.8+

SXSSFWorkbook可以根据行数将内存中的数据持久化写到文件中。

此种的情况就是设置最大内存条数，比如设置最大内存量为5000行， new SXSSFWookbook(5000)，当行数达到 5000 时，把内存持久化写到文件中，以此逐步写入，避免OOM。这样就完美解决了大数据下导出的问题

#### 使用SXSSFWorkbook处理，对.xlsx类型文件下载

```java
    @UserLoginToken
    @RequestMapping(value = "/excelall", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcel(HttpServletResponse response) throws IOException {
		//从token中获取当前登录用户的用户名
        String token= TokenUtil.getToken();
        String username=TokenUtil.getTokenUsername(token);

        //创建SXSSFWorkbook 对象
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        Sheet sheet = workbook.createSheet("信息表");
		
        //获取list对象，下面将list对象写入excel中
        List<Promember> downloadData = promemberService.findByBadge(username);

        //设置导出的文件名称
        String fileName = "项目成员表"  + ".xlsx";//设置要导出的文件的名字
        
        //当前数据写入的位置行数，行头写在0行
        int rowNum = 1;
		
        //设置表格的标题
        String[] headers = { "项目成员工号", "项目成员名称", "项目校内标号", "金额", "是否是负责人"};
        
        //创建第一行表头的写入位置，row，cell
        Row titleRow = sheet.createRow(0);

        //在excel表中添加表头
        for(int i=0;i<headers.length;i++){
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(headers[i]);

        }

        //在表中存放查询到的数据放入对应的列
        for (Promember promember : downloadData) {
            Row row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(promember.getPromembadge());
            row1.createCell(1).setCellValue(promember.getPromemname());
            row1.createCell(2).setCellValue(promember.getPid());
            row1.createCell(2).setCellValue(promember.getFee());
            row1.createCell(2).setCellValue(promember.getIscharge());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        //文件命中包含中文，所以需要对filename进行转编码
        response.setHeader("Content-disposition", "attachment;filename=" +new String(fileName.getBytes("utf-8"),"ISO8859-1") );
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
```

#### 使用HSSFWorkbook处理，对.xls类型文件下载

```java
    @UserLoginToken
    @RequestMapping(value = "/excelallnotx", method = RequestMethod.GET)
    @ResponseBody
    public void downloadAllClassmate(HttpServletResponse response) throws IOException {

        String token= TokenUtil.getToken();
        // System.out.println("token:"+token);
        String username=TokenUtil.getTokenUsername(token);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");

        List<Awardorgandep> downloadData = awardorgandepService.findByBadge(username);

        String fileName = "科研奖励"  + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        String[] headers = { "评奖组织单位", "获奖项目名称", "奖励名称类型", "获奖等级",
                "获奖时间（以证书日期为准）", "获奖人员姓名（按顺序）", "完成单位名称（按顺序）", "证书编号",
                "个人证书","单位证书"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (Awardorgandep awardorgandep : downloadData) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(awardorgandep.getAwardunit());
            row1.createCell(1).setCellValue(awardorgandep.getAwardname());
            row1.createCell(2).setCellValue(awardorgandep.getAwardnametype());
            row1.createCell(3).setCellValue(awardorgandep.getAwardlevel());
            row1.createCell(4).setCellValue(awardorgandep.getAwarddate());
            row1.createCell(5).setCellValue(awardorgandep.getAwardstaffname());
            row1.createCell(6).setCellValue(awardorgandep.getCompletedepname());
            row1.createCell(7).setCellValue(awardorgandep.getCertno());
            row1.createCell(8).setCellValue(awardorgandep.getPersoncerti());
            row1.createCell(9).setCellValue(awardorgandep.getUnitcerti());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
```



# Filter

今日内容：



![image-20210421102008660](C:\Users\18270\AppData\Roaming\Typora\typora-user-images\image-20210421102008660.png)

1. 在java类中继承Filter接口，实现接口中的方法，主要在doFilter中实现逻辑，在web.xml文件中完成filter的配置。

每次拦截处理逻辑后，如果需要接着让用户往下访问资源，则需要执行下面方法：

```java
//用户接着往下访问用户的目标资源
filterchain.doFilter(servletRequest,servletResponse)
```

2. 怎样修改tomcat在服务器的日志乱码问题，

查看ideal的编码，然后找到tomcat安装目录下的conf文件夹中的logging.properties文件，修改 如下：

java.util.logging.ConsoleHandler.encoding = UTF-8

后重启服务器执行即可

3.  创建工程后默认生成了工程目录名称，修改了tomcat配置中的url后报错无法访问![image-20210421170327089](C:\Users\18270\AppData\Roaming\Typora\typora-user-images\image-20210421170327089.png)需要同步修改Deloyment下的内容。

![image-20210421170239729](C:\Users\18270\AppData\Roaming\Typora\typora-user-images\image-20210421170239729.png)

3. 由于tomcat内部config默认设置，导致工程打开默认识别webapp下面的index.jsp页面，如果需要修改的话，则在web.xml中增加下面代码

   ```xml
   <welcome-file-list>
       <welcome-file>index1.jsp</welcome-file>
   </welcome-file-list>
   ```



filter的FilterChain.doFilter()方法，该方法在实现Filter接口的doFilter方法中调用默认参数filterChain类的doFilter方法使用：

![image-20210422105233213](C:\Users\18270\AppData\Roaming\Typora\typora-user-images\image-20210422105233213.png)

### pom文件配置

过滤资源

```xml
<!--静态资源导出问题-->    
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
</build>
```

maven包

```xml
<!--@stereotype.Controller-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.5</version>
</dependency>
<!--自动getter setter-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.18</version>
</dependency>
<!--json-->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.11.4</version>
</dependency>
<!--junit-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
</dependency>
<!--mysql-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.23</version>
</dependency>
<!--数据库连接池-->
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.2</version>
</dependency>
<!--servlet -jsp-->
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>jsp-api</artifactId>
    <version>2.2</version>
</dependency>
<!--web.xml中的DispatcherServlet-->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>

<!--mybatis-->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.6</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.6</version>
</dependency>
<!--spring\spring.xml中的mvc配置-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.5</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.3.5</version>
</dependency>

<!--上传文件-->

```

### Spring架构搭建

spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!--扫描位置，让其内的注解生效，，由IOC容器管理-->
    <context:component-scan base-package="com.huang.controller"/>
    <!--使用注解加载映射器和解析器，省略配置步骤-->
    <mvc:annotation-driven/>
    <!--让springmvc不使用视图解析器解析静态资源-->
    <mvc:default-servlet-handler/>
	<!--配置视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
    
    <!--文件上传配置-->
    <bean class="org.springframework.web.multipart.commons.CommonsMultipartResolver" id="multipartResolver">
        <!--请求的编码格式必须和jsp的pageencoding属性一直，以便正确读取表单的内容，默认为IOS-8859-1-->
        <property name="defaultEncoding" value="utf-8"/>
        <!--文件大小上线，单位字节，10485760=10m-->
        <property name="maxUploadSize" value="10485760"/>
        <property name="maxInMemorySize" value="40960"/>
    </bean>
    <!--设置json乱码，目前导入这个配置会报错-->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
    <!--设置拦截器-->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <bean class="com.huang.controller.Interception"/>
        </mvc:interceptor>
    </mvc:interceptors>
</beans>
```

web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!--配置DispatcherServlet，自动分发servlet-->
    <servlet>
        <servlet-name>mvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>mvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <!--编码设置-->
    <filter>
        <filter-name>encoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>/</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encoding</filter-name>
        <url-pattern>/</url-pattern>
    </filter-mapping>
</web-app>

```

### 注解

方法注释

```java
@RequestMapping		可配置地址，通过method参数配置访问的方法是post、get、delete、update等
@PostMapping		可配置地址，设置方法为post
@GetMapping 		可配置地址，设置方法为get
@ResponseBody		表示返回为字符串，不进行视图解析器解析，只传递json可以用这个
```

类注释

```java
@Controller			配置表示属于controller
@RestController		配置表示该类的所有返回都属于字符串，不进行视图解析，只传递json可以用这个
```

- @Autowired：自动装配，通过类型，名字
  - 如果@Autowired不能唯一自动装配，则需要通过@Qualifier(value="xxx")来进行指定
- @Nullable ：参数标记了这个注解啧说明字段可为null
- @Resource：自动装配，通过名字类型



### FastJson

导包

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.76</version>
</dependency>
```

### 拦截器 Interception

**1. 类实现接口HandlerInterceptor**

```java
public class Interception implements HandlerInterceptor {
	
    //返回false表示拦截
    //True表示不拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```

**2. spring.xml文件中添加**

```xml
    <mvc:interceptors>
        <mvc:interceptor>
            <!--/**表示根目录下的所有路径（包括子路径）都拦截-->
            <mvc:mapping path="/**"/>
            <bean class="com.huang.controller.Interception"/>
        </mvc:interceptor>
    </mvc:interceptors>
```



### 问题



问题1：

```shell
新建一个javaenterprise项目，无法访问webapp下的png图片资源，jpg图片可正常访问。
```

问题2：

```
新建一个filter，在web.xml文件中配置了filter后报错如下：
21-Apr-2021 20:12:07.394 信息 [RMI TCP Connection(3)-127.0.0.1] org.apache.jasper.servlet.TldScanner.scanJars 至少有一个JAR被扫描用于TLD但尚未包含TLD。 为此记录器启用调试日志记录，以获取已扫描但未在其中找到TLD的完整JAR列表。 在扫描期间跳过不需要的JAR可以缩短启动时间和JSP编译时间。
21-Apr-2021 20:12:07.413 严重 [RMI TCP Connection(3)-127.0.0.1] org.apache.catalina.core.StandardContext.startInternal 一个或多个筛选器启动失败。完整的详细信息将在相应的容器日志文件中找到
21-Apr-2021 20:12:07.413 严重 [RMI TCP Connection(3)-127.0.0.1] org.apache.catalina.core.StandardContext.startInternal 由于之前的错误，Context[/demo]启动失败
```

问题3：

```
ThreadLocal的使用
```

## SpringBoot

springboot的自动装配配置都在spring-boot-autoconfigure-2.2.0.RELEASEjar包下

![image-20210902113307259](C:\Users\18270\AppData\Roaming\Typora\typora-user-images\image-20210902113307259.png)

### pom.xml

pom.xml文件设置了starter-web后才作为一个web项目，通过springinitialize新建项目，未勾选springweb时添加如下依赖

```xml
<!--starter-web后才作为一个web项目-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--thymeleaf-->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
</dependency>
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-java8time</artifactId>
</dependency>
```

在pom配置了一个父工程的依赖，springboot几乎提前导入需要的jar包

### application.properties

```properties
#设置端口
server.port=8081
```

**通过注解将配置在yaml文件中的值赋值给类，以及jsr303校验属性验证**

1. yaml文件中配置person类的属性,yaml文件中可以使用EL表达式

```yaml
person:
	name: huang
	age: 23
	birth: 2019/12/12
	maps:{key1: val1,key2: val2}
	email: huang@163.com
```

2. 创建类时，加上注解

```java
@Component//注册bean，设置为容器中的组件，才能使用ConfigurationProperties功能
@ConfigurationProperties(prefix="person")//赋值yaml文件中的person对象
@Validated//jsr303校验
public class Person{
    private String name;
    private Integer age;
    private Date birth;
    private Map<String,Object> maps;
    @Email(message="自定义错误提示")//设置校验，赋值不符合邮箱格式会报错
    private String email;
    Person(){}
    Person(String name,Integer age,Date birth,Map<String,Object> maps,String email){
        this.name=name;
        this.age=age;
        this.birth=birth;
        this.maps=maps;
        this.email=email;
    }
}
```

3. 使用

```java
@Autowired
Person person= new Person()
```

**使用properties配置文件获取值**

1. 配置文件huang.properties

```properties
name=huang
```

2. 配置类

```java
@configurationProperties("classpath:huang.properties")
public class Person{
    @Value("name")
    private String name;
}
```

获取一个值时，使用properties；多个使用yaml

### yaml文件可放置的位置

file:./config/（工程根目录下创建config下）  file:./(工程根目录下)     classpath:./config/   classpath:./（类路径下，一般是resources下）

优先级也如上顺序，第一个最高，如有重复变量，则取优先级高的

yaml/yml实现多环境配置：https://www.cnblogs.com/tudou1179006580/p/14875313.html

### thymeleaf使用

1. pom.xml文件中导入依赖

   ```xml
   <!--thymeleaf-->
   <dependency>
       <groupId>org.thymeleaf</groupId>
       <artifactId>thymeleaf-spring5</artifactId>
   </dependency>
   <dependency>
       <groupId>org.thymeleaf.extras</groupId>
       <artifactId>thymeleaf-extras-java8time</artifactId>
   </dependency>
   ```

2. html文件中引入`<html lang="en" xmlns:th="http://thymeleaf.org">`

3. 























