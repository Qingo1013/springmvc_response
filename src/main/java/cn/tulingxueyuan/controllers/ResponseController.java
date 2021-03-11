package cn.tulingxueyuan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResponseController {

    @RequestMapping("/response01")
    public String response01() {
        return "index";
    }

    /**
     * 转发的实现
     *  1.默认就是进行转发，并且会进行视图解析
     *  2.可以显式的加上forward进行转发，forward需要自己返回完整视图逻辑逻辑名和路径，因为不会进行ViewResolver的解析
     *  与重定向不同：
     *      1.地址栏不会改变
     *      2.由服务器实现页面跳转
     *      3.知道项目的地址，不用自己写
     *      4.request域不会丢失
     *      5.一次请求
     * @return
     */
    @RequestMapping("/response02")
    public String response02() {
        //return "index";
        //使用这种方式时，视图解析器会失效，不会自动加前后缀，但是知道项目地址，所以不用加
        return "forward:/login.jsp";
    }

    /**
     *  重定向
     *  必须加上"redirect"的关键字
     *      注意：/会包含项目名，springmvc特殊之处，识别到"redirect"的关键字后，会在底层自动加上项目地址
     *  但是，servlet一定要加上项目名
     *
     *  与转发不同：
     *      1.地址栏会改变
     *      2.由客户端实现页面跳转
     *      3.不知道项目的地址，需要自己写
     *      4.request域会丢失
     *      5.两次请求
     */
    @RequestMapping("/response03")
    public String response03() {
        return "redirect:/login.jsp";
        //这种写法不行，因为WEB-INF中数据 访问受限
        // return "redirect:/WEB-INF/views/index.jsp";
    }
}
