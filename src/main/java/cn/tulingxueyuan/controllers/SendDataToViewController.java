package cn.tulingxueyuan.controllers;

import cn.tulingxueyuan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
//使用springmvc自动注解的方式
// 1.写在类前，要利用model，从model中拷贝一份"type"的值
// 即从model（model,modelmap,map,modelandview）中获取指定的属性写入session中
//底层会从model中找一个叫做type的属性找到了会将type拷贝一份到session中
//这种方式是依赖model的
//当前控制器下所有的处理方法，都会讲指定的model属性写入session
@SessionAttributes("type")

public class SendDataToViewController {
    /**
     *  使用servlet 原生API 的方式传输数据到View视图
     * @param request
     * @return
     */
    @RequestMapping("/servletAPI")
    public String servletAPI(HttpServletRequest request){
        request.setAttribute("type","servletAPI");
        return "main";
    }

    /**
     * 使用model的方式传输数据到View视图
     * @param model
     * @return
     * 底层还是requset实现
     */
    @RequestMapping("/model")
    public String model(Model model){
        System.out.println(model.getClass());
        model.addAttribute("type","model");

        return "main";
    }

    /**
     * 使用modelmap的方式传输数据到View视图
     * @param modelMap
     * @return
     */
    @RequestMapping("/modelmap")
    public String modelmap(ModelMap modelMap){
        System.out.println(modelMap.getClass());
        modelMap.addAttribute("type","modelmap");
        return "main";
    }
    /**
     * 使用map的方式传输数据到View视图
     * @return
     */
    @RequestMapping("/map")
    public String map(Map map){
        System.out.println(map.getClass());
        map.put("type","map");
        return "main";
    }

    @RequestMapping("/mav")
    public ModelAndView mav(){
        ModelAndView mv = new ModelAndView("main");
//        mv.setViewName;
        mv.addObject("type","ModelAndView");
        return mv;
    }

    /**
     *  通过参数绑定的方式获取servlet api---session
     */
    @RequestMapping("/servletapi/session")
    public String session01(HttpSession session){
        session.setAttribute("type","servletapi-session");
        return "main";
    }

    /**
     *  通过自动注入的方式获取servlet api---session（*****建议使用 ）
     *  推荐这种
     */
    @Autowired
    private HttpSession session;
    @RequestMapping("/autowired/session")
    public String session02(){
        session.setAttribute("type","autowiredsession");
        return "main";
    }

    /**
     *
     * @SessionAttribute 获取session
     * 属性required 用来设置session中某个属性必须存在，不存在则报错
     * model和session是互通的：session可以通过model去获取然后写入的指定属性，model也会从session中自动获取并写入指定属性
     *
     * @param type
     * @return
     */
    @RequestMapping("/annotation/session")
    public String session03(@SessionAttribute(value = "type", required = false) String type, Model model){
        System.out.println(type);
//        model.addAttribute("type",type);
        return "main";
    }

    /**
     * 所有处理方法被调用前，都会先调用注解@ModelAttribute下面的showModelAttribute方法
     * 应用场景：
     *  一、写在方法上
     *      1.通过@ModelAttribute来给全局变量赋值;设置servlet api--session（不推荐）
     *      2. 当我们调用执行全字段的更新数据库操作时，假如提供给用户的修改字段只有
     *      部分几个，这个时候就会造成其他字段更新丢失：
     *          解决：
     *              1.自己定制update语句，只更新指定的那些字段
     *              2.如果无法定制sql语句，可以在更新之前进行查询，怎么在更新之前查询？只
     *              能在springmvc 绑定请求参数之前查询，利用@ModelAttribute就可以在参数绑定之前
     *              查询， 但是怎么将查询出来的对象和参数的对象进行合并？springmvc具有该特性，会
     *              将model中和参数名相同的属性拿出来进行合并，将参数中的新字段进行覆盖，没有的字段
     *              进行保留。这样就可以解决这个问题。
     *  二、写在参数上
     *      可以省略，加上则会从model中获取一个指定的属性和参数进行合并，因为model和
     *      sessionAttribute具有共通的特性，所以如果session中有对应的属性也会进行合并
     */
    @RequestMapping("/modelattribute/session")
    public String session04(){
        session2.setAttribute("type","ModelAttribute-session");
        return "main";
    }


    /*
    此例子不明显，因为sessionid的缘故，一个线程的session被覆盖后，就找不到了
    @RequestMapping("/thread/test")
    public String session05(String name) throws InterruptedException {
        Thread.sleep(5000);
        session2.setAttribute("type",name);
        return "main";
    }*/
    //String username;这样会产生线程安全问题，因为会对共享变量同时进行读写操作
    @RequestMapping("/thread/test")
    public String session05(String name, HttpServletRequest request) throws InterruptedException {
        //声明在方法中，可以解决共享变量产生的线程安全问题
        String username=name;
        Thread.sleep(5000);
        request.setAttribute("type",username);
        return "main";
    }
    /**
     * 通过 @ModelAttribute来获取servlet api--session并赋值
     *
     */

    HttpSession session2;
    @ModelAttribute
    public  void  showModelAttribute(HttpSession session){
        this.session2=session;
        System.out.println("ModelAttribute-----------");
    }

    /**
     * 解决问题，修改username会把password变为null
     *
     * @param model
     */
/*    @ModelAttribute
    public  void  initUser(Model model){
        //从数据库中查询User  select * from user
        //假设该user是从数据库查询出来的
        User user = new User();
        user.setId(1); user.setUsername("qqa"); user.setPassword("13234");
        model.addAttribute("user",user);
    }*/

    /**
     * springmvc在进行参数绑定前，会将model中跟参数名符合的属性拿出来合并
     * 参数中新提交的字段会覆盖，缺失的字段会保留
     *
     * @param user
     * @return
     */
    @RequestMapping("/update")
    public String update(@ModelAttribute("user") User user){

        //update user set id=?,username=?,password=?  where id=?
        //这种全字段更新会把password设置为null
        System.out.println(user);
        return "main";
    }
    /*
    @ModelAttribute
    public  void  showModelAttribute(){
        System.out.println("ModelAttribute-----------");
    }*/
    /**
     *  3种方式获取servlet-api的线程安全问题（并发问题）
     *   线程不安全=有并发问题：同一个时间，多个线程，同时对数据/变量/资源进行读写操作
     *   就会产生并发问题（脏读、幻读）
     *   共享变量：只在内存中创建一次（static），单例中定义在类里的变量
     *           1.并行变串行
     *          2 .将变量变为非共享，声明在方法中或创建在ThreadLocal中
     *  1.听过参数绑定方式
     *      是线程安全的，因为该方式下变量是方法级别的，所以每次请求，处理方法都会在内存中开辟自己独立的空间
     *  2.通过@Autowired自动注入的方式
     *      是线程安全的，很特殊，虽然他是共享变量（单例类内级别变量），但是在springmvc底层是使用的ThreadLocal存储的
     *      servletapi，因此通过自动注入进来的servletapi是线程安全的
     *  3.通过@ModelAtrribute的方式（全局变量的方式）（根源通过参数绑定，区别在于赋值到类内变量级别）
     *      不是线程安全的，虽然session是通过参数绑定，但是之后赋值给了共享变量（单例类内级别变量）
     *      springmvc是否单例，有线程安全吗？怎么解决？
     *      是单例的。按照常规来说，不会有线程安全问题，因位springmvc都是方法级别的问题。
     */

}
