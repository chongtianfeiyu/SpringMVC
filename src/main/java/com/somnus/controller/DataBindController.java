package com.somnus.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.somnus.domain.Account;
import com.somnus.interceptor.AuthPassport;

@Controller
@RequestMapping(value = "/databind")
public class DataBindController {

	@AuthPassport
	@RequestMapping(value="/parambind", method = {RequestMethod.GET})
    public ModelAndView paramBind(){
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("parambind");
        return modelAndView;
    }
	
	@AuthPassport
	@RequestMapping(value="/parambind", method = {RequestMethod.POST})
    public ModelAndView paramBind(HttpServletRequest request, @RequestParam("urlParam") String urlParam, 
            @RequestParam("formParam") String formParam, @RequestParam("formFile") MultipartFile formFile){
		
		//如果不用注解自动绑定，我们还可以像下面一样手动获取数据
		String urlParam1 = ServletRequestUtils.getStringParameter(request, "urlParam", null);
		String formParam1 = ServletRequestUtils.getStringParameter(request, "formParam", null);
        MultipartFile formFile1 = ((MultipartHttpServletRequest) request).getFile("formFile"); 
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("urlParam", urlParam);
		modelAndView.addObject("formParam", formParam);
		modelAndView.addObject("formFileName", formFile.getOriginalFilename());
		
		modelAndView.addObject("urlParam1", urlParam1);
		modelAndView.addObject("formParam1", formParam1);
		modelAndView.addObject("formFileName1", formFile1.getOriginalFilename());
		
		modelAndView.setViewName("parambindresult");  
        return modelAndView;
    }
	/* ******************************************************************************/
	
	@RequestMapping(value="/modelautobind", method = {RequestMethod.GET})
	public String modelAutoBind(Model model){
		//界面用到了【springmvc标签】才需要用model设置一个对象，给前端用来绑定
		/*model.addAttribute("account", new Account());*/
		return "modelautobind";
	}
	/**               以下三个方法都可以用来拿到对象后，并把对象传递到结果界面                            */
	@RequestMapping(value="/modelautobind", method = {RequestMethod.POST})
	public String modelAutoBind(@ModelAttribute("account") Account account){
		System.out.println("第一种方式：" + account);
		return "modelautobindresult";
	}
	
	@RequestMapping(value="/modelautobind2", method = {RequestMethod.POST})
	public String modelAutoBind(Model model, Account account){
		System.out.println("第二种方式：" + account);
		model.addAttribute("account", account);
		return "modelautobindresult";
	}
	
	@RequestMapping(value="/modelautobind3", method = {RequestMethod.POST})
	public String modelAutoBind(HttpServletRequest request,Account account){
		System.out.println("第三种方式：" + account);
		request.setAttribute("account", account);
		return "modelautobindresult";
	}
	
	/* ******************************************************************************/
	//@CookieValue Test
	@RequestMapping(value="/cookiebind", method = {RequestMethod.GET})
    public String cookieBind(Model model,@CookieValue(value="JSESSIONID", defaultValue="") String jsessionId){
		
		model.addAttribute("jsessionId", jsessionId);
        return "cookiebindresult";
    }
	
	//@RequestHeader Test
	@RequestMapping(value="/requestheaderbind", method = {RequestMethod.GET})
    public String requestHeaderBind(Model model, @RequestHeader(value="User-Agent", defaultValue="") String userAgent){
		
		model.addAttribute("userAgent", userAgent);
        return "requestheaderbindresult";
    }
	
	/* ******************************************************************************/
	//@RequestBody Test
	@RequestMapping(value="/requestbodybind", method = {RequestMethod.GET})
    public String requestBodyBind(){
        return "requestbodybind";
    }
	
	@RequestMapping(value="/requestbodybind", method = {RequestMethod.POST})
	@ResponseBody
    public Account requestBodyBind(@RequestBody Account account){
		System.out.println(account);
		return account;
    }
	
	/** @RequestParam注解了某参数，必传，除非单独设置required；如果某个参数没有使用注解，那么可传可不传*/
	@RequestMapping(value="/json", method = {RequestMethod.POST})
    @ResponseBody
    public Account json(@RequestParam("username") String username,
            @RequestParam("password") String password){
	    Account account = new Account();
	    account.setUsername(username);
	    account.setPassword(password);
        return account;
    }
	
	@RequestMapping(value="/json2", method = {RequestMethod.POST})
	@ResponseBody
    public Account json2(Account account){
		System.out.println(account);
		return account;
    }
	
}