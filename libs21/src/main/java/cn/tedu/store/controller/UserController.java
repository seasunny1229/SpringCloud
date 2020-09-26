package cn.tedu.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.tedu.store.bean.ResponseResult;
import cn.tedu.store.bean.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameAlreadyExistsException;
import cn.tedu.store.service.ex.UsernameNotFoundException;

@RequestMapping("/user")
@Controller
public class UserController
	extends BaseController {
	
	@Resource(name="userService")
	private IUserService userService;
	
	@RequestMapping("/register.do")
	public String showRegister() {
		return "register";
	}
	
	@RequestMapping("/login.do")
	public String showLogin() {
		return "login";
	}
	
	@RequestMapping("/password.do")
	public String showPassword() {
		return "user_password";
	}
	
	@RequestMapping("/profile.do")
	public String showProfile(
			ModelMap modelMap, 
			HttpSession session) {
		// 查询当前登录的用户数据
		Integer uid = getUidFromSession(session);
		User user = userService.findUserById(uid);
		
		// 将用户数据转发到前端页面
		modelMap.addAttribute("user", user);
		
		// 执行转发
		return "user_profile";
	}
	
	@RequestMapping("/check_username.do")
	@ResponseBody
	public ResponseResult<Void> checkUsername(
			String username) {
		// 声明返回值
		ResponseResult<Void> rr;
		// 检查用户名是否存在
		boolean result 
			= userService
				.checkUsernameExists(
					username);
		// 判断检查结果
		if (result) {
			// 结果为true，表示用户名已经存在
			rr = new ResponseResult<Void>(
				0, "用户名已经被占用");
		} else {
			// 结果为false，表示用户名尚未被注册
			rr = new ResponseResult<Void>(
				1, "用户名可以使用");
		}
		// 返回
		return rr;
	}
	
	@RequestMapping("/check_email.do")
	@ResponseBody
	public ResponseResult<Void> checkEmail(
			String email) {
		// 声明返回值
		ResponseResult<Void> rr;
		// 验证电子邮箱是否已经被注册
		boolean result = userService
				.checkEmailExists(email);
		// 判断验证结果
		if (result) {
			rr = new ResponseResult<Void>(
				0, "邮箱已经被注册");
		} else {
			rr = new ResponseResult<Void>(
				1, "邮箱可以正常使用");
		}
		// 返回
		return rr;
	}
	
	@RequestMapping("/check_phone.do")
	@ResponseBody
	public ResponseResult<Void> checkPhone(
			String phone) {
		// 声明返回值
		ResponseResult<Void> rr;
		// 验证手机号码是否已经被注册
		boolean result = userService
				.checkPhoneExists(phone);
		// 判断验证结果
		if (result) {
			rr = new ResponseResult<Void>(
					0, "手机号已经被注册");
		} else {
			rr = new ResponseResult<Void>(
					1, "手机号可以正常使用");
		}
		// 返回
		return rr;
	}

	@RequestMapping(value="/handle_register.do",
			method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleRegister(
		@RequestParam("uname") String username,
		@RequestParam("upwd") String password,
		@RequestParam("phone") String phone,
		@RequestParam("email") String email) {
		// 声明返回值
		ResponseResult<Void> rr;
		
		// 封装数据
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setPhone(phone);
		user.setEmail(email);
		System.out.println("UserController.handleRegister()");
		System.out.println("\tuser=" + user);
		
		// 执行注册
		try {
			userService.register(user);
			rr = new ResponseResult<Void>(
					1, "注册成功");
			return rr;
		} catch (UsernameAlreadyExistsException e) {
			rr = new ResponseResult<Void>(
					0, e.getMessage());
			return rr;
		}
	}
	
	@RequestMapping(value="/handle_login.do", 
			method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleLogin(
		@RequestParam("username") String username,
		@RequestParam("password") String password,
		HttpSession session) {
		// 声明返回值
		// 无论登录成功与否，都返回ResponseResult<Void>对象
		ResponseResult<Void> rr;
		
		try {
			// 调用userService完成登录
			User user = 
				userService.login(
					username, password);
			rr = new ResponseResult<Void>(1);
			// 如果登录成功，在返回之前，
			// 调用HttpSession对象的setAttribute(String, Object)方法封装uid及username
			session.setAttribute(
				"uid", user.getId());
			session.setAttribute(
				"username", user.getUsername());
		} catch (UsernameNotFoundException e) {
			// 用户名不存在
			rr = new ResponseResult<Void>(0, e);
		} catch (PasswordNotMatchException e) {
			// 密码错误
			rr = new ResponseResult<Void>(-1, e);
		}
		// 返回
		return rr;
	}
	
	@RequestMapping("/logout.do")
	public String handleLogout(
		HttpServletRequest request,
		HttpSession session) {
		// 清除Session中的所有数据
		session.invalidate();
		// 重向向的目标
		String url = "../main/index.do";
		// 执行重定向
		return "redirect:" + url;
	}

	@RequestMapping(method=RequestMethod.POST, 
		    value="handle_change_password.do")
	@ResponseBody
	public ResponseResult<Void> handleChangePassword(
			@RequestParam("old_password") String oldPassword,
			@RequestParam("new_password") String newPassword,
			HttpSession session) {
		// 声明ResponseResult对象
		ResponseResult<Void> rr;
		// 从Session中获取用户ID
		Integer uid = getUidFromSession(session);
		try {
		    // 调用Service中的changePassword()方法
			userService.changePassword(
				uid, oldPassword, newPassword);
		    // 通过ResponseResult表示修改密码成功
			rr = new ResponseResult<Void>(1, "修改密码成功！");
		} catch (UserNotFoundException e) {
		    // 通过ResponseResult表示修改密码失败：用户不存在
			rr = new ResponseResult<Void>(-1, e);
		} catch (PasswordNotMatchException e) {
		    // 通过ResponseResult表示修改密码失败：密码错误
			rr = new ResponseResult<Void>(-2, e);
		}
		// 返回
		return rr;
	}
	
	@RequestMapping(value="/handle_edit_profile.do",
			method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleEditProfile(
		String username,
		Integer gender,
		String phone,
		String email,
		HttpSession session) {
		// 声明返回值
		ResponseResult<Void> rr;
		// 调用getUidFromSession()获取uid
		Integer uid = getUidFromSession(session);
		
		try {
			// 调用userService中的editProfile()方法
			userService.editProfile(
				uid, 
				username, gender, 
				phone, email);
			// 更新Session中的username
			User u = userService.findUserById(uid);
			session.setAttribute("username", u.getUsername());
			// 返回操作正确
			rr = new ResponseResult<Void>(1, "修改成功！");
		} catch (UserNotFoundException e) {
			// 返回用户不存在
			rr = new ResponseResult<Void>(-1, e);
		} catch (UsernameAlreadyExistsException e) {
			// 返回用户名已经被占用
			rr = new ResponseResult<Void>(-2, e);
		}
		
		// 返回
		return rr;
	}
	
	//UserController 添加处理图片下载方法
	@RequestMapping(value="/demo.do",
			produces="image/png")
	//produces 用于设定 content-type 属性
	@ResponseBody //与返回值byte[] 配合, 填充
	//Response 的Body部分
	public byte[] pngDemo() throws Exception{
		//读取一个png图片, 返回图片数据即可
		String path=
			"cn/tedu/store/controller/pwd.png";
		//从包中读取文件
		InputStream in =
			getClass().getClassLoader()
			.getResourceAsStream(path);
		//available()方法可以检查流中一次可以
		//读取的字节个数, 小文件就是文件的长度
		byte[] bytes=new byte[in.available()];
		//将文件中全部数据读取到 bytes 数组中
		in.read(bytes);
		in.close();
		return bytes;
	}
	
	@RequestMapping("/check_code.do")
	@ResponseBody
	public ResponseResult<Void> checkCode(
			String code, HttpSession session){
		String c = 
			(String)session.getAttribute("code");
		if(c==null){
			return new ResponseResult<Void>(
					0, "验证失败!");
		}
		//equalsIgnoreCase忽略大小写比较两个字符串
		//是否一致
		if(c.equalsIgnoreCase(code)){
			return new ResponseResult<Void>(
					1, "验证通过!");
		}
		return new ResponseResult<Void>(
				0, "验证失败!");
	}
	
	/**
	 * 生成验证码
	 */
	@RequestMapping(value="/code.do",
			produces="image/png")
	@ResponseBody
	public byte[] code(HttpSession session) throws IOException{
		//利用算法生成一个动态PNG图片
		String code=createCode(4);//生成验证码
		session.setAttribute("code", code);
		System.out.println(code);
		byte[] bytes=createPng(code);//生成图片
		return bytes;
	}
	//生成图片
	private byte[] createPng(String code)
		throws IOException{
		//创建图片对象
		BufferedImage img=new BufferedImage(
			100,40,BufferedImage.TYPE_3BYTE_BGR);
		//img.setRGB(0, 0, 0x0000ff); 
		//img.setRGB(50, 20, 0xffff00);
		//绘制5000个随机色点
		Random r = new Random();
		for(int i=0; i<5000; i++){
			int x = r.nextInt(img.getWidth());
			int y = r.nextInt(img.getHeight());
			int rgb = r.nextInt(0xffffff);//随机色
			img.setRGB(x, y, rgb);
		}
		//利用API绘制验证码字符串
		Graphics2D g = img.createGraphics();
		Color c = new Color(r.nextInt(0xffffff));
		g.setColor(c);
		Font font=new Font(Font.SANS_SERIF, 
				Font.ITALIC, 35);
		g.setFont(font);
		g.drawString(code, 5,34); 
		//利用API绘制5条混淆线
		for(int i=0; i<5; i++){
			int x1=r.nextInt(img.getWidth());
			int y1=r.nextInt(img.getHeight());
			int x2=r.nextInt(img.getWidth());
			int y2=r.nextInt(img.getHeight());
			g.drawLine(x1, y1, x2, y2);
		}
		
		//将图片对象编码为 png 数据
		//创建 数组输出流作为缓存区(酱油瓶)
		ByteArrayOutputStream out=
			new ByteArrayOutputStream();
		//将png图片数据(酱油)保存到缓存区
		ImageIO.write(img, "png", out);
		out.close();
		//获取缓存区中的png数据(酱油)
		byte[] bytes=out.toByteArray();
		return bytes;
	}
	//生成验证码
	private String createCode(int n) {
		String chs="abcdefghijkmnpqxy"
				+ "ABCDEFGHJKLMNPQRSTUV"
				+ "34568";
		char[] code=new char[n];
		Random r = new Random();
		for(int i=0; i<code.length; i++){
			int index=r.nextInt(chs.length());
			code[i]=chs.charAt(index);
		}
		return new String(code);
	}
	
}










