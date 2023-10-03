视频网址：https://www.bilibili.com/video/BV1Fd4y1k7rq/?spm_id_from=333.337.search-card.all.click


说白了就是写两个过滤器，一个注销的执行器，把这三个东西给security框架就行，
再写一个权限认证失败执行的方法和自定义查询数据库检查账号密码的方法。就行


第一步：引入依赖

第二步：写个创建token和解析token的方法

第三步：自定义UsernamePasswordAuthenticationFilter账号密码认证过滤器，重写三个方法，默认只有post请求才会经过这个过滤器。
    
    1.账号密码认证的方法，把前端提交的表单数据拿到，得到账号密码给框架
    2.登录成功执行的方法，从入参authResult得到用户名，根据用户名查询数据库获得用户权限，把权限存到redis，并用jwt生成用户数据的token返回给前端
    3.登录失败执行的方法，返回个错误信息

第四步：自定义BasicAuthenticationFilter过滤器，重写doFilterInternal方法，用于Http基本认证，自动解析Http请求头中名为Authentication的内容，并获得内容中“basic”开头之后的信息。

    //从请求头获得token
    //使用jwt解析token获得username
    //从redis中获得该用户名对应的权限
    //将取出的权限存入到权限上下文中，表示当前token对应的用户具备哪些权限
    //生成权限信息对象
    //把权限信息对象存入到权限上下文中
    //放行

第五步：编写注销处理器 实现LogoutHandler，注销时具体要执行的业务
    
    //1.从请求头中获得前端携带的token
    //2.使用jwt解析token
    //3.删除redis中的数据

第六步：编写账号密码验证的逻辑
    
    也就是写UserDetailsService的实现类，重写loadUserByUsername方法
    //根据用户名从数据库查询到该用户的信息
    //根据用户名从数据库查询到该用户的权限信息
    //把用户信息和权限信息封装到UserDetails的实现类对象返回

第七步：写个授权认证失败执行的方法。（注意不是认证失败，是访问接口但没有权限执行的方法）

    public class UnAuthEntryPoint implements AuthenticationEntryPoint {
      /**
      * 权限认证失败执行的方法
      * @param request
      * @param response
      * @param authException
      * @throws IOException
      * @throws ServletException
      */
      @Override
      public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
      //ResultModel:状态码，信息，数据
      ResponseUtil.out(response,ResultModel.error());
      }
    }

第八步：写配置类，SecurityConfig extends WebSecurityConfigurerAdapter

    1.重写configure(HttpSecurity http)
    http .把上面的两个过滤器和处理器都添加，
    2.密码加密方式为加密
        @Bean
        PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    3.重写configure(AuthenticationManagerBuilder auth)
    //指定获得用户名和密码的方式
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

第九步：写个util工具类，用来方便获取当前登录的用户