package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.entity.Order;
import com.entity.User;
import com.github.pagehelper.PageInfo;
import com.service.IMovieService;
import com.service.IOrderService;
import com.service.IScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/jsp/order")
public class OrderController {
	@Resource
	private IOrderService orderService;
	@Resource
	private IScheduleService scheduleService;  //支付、退票成功 座位+-
	@Resource
	private IMovieService movieService; //支付、退票成功  票房+-
	//查看订单是否 是支付的（返回给前端的数据）
	
	@RequestMapping("findOrderById")
	@ResponseBody
	public JSONObject findOrderById(@RequestParam("order_id")String order_id) {
		JSONObject obj = new JSONObject();
		Order order = orderService.findOrderById(order_id);
		List<Order> list = new ArrayList<Order>();
		list.add(order);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", list.size());
		obj.put("data",list);
		return obj;
	}
	
	@RequestMapping("findOrderByUserName")
	@ResponseBody
	public JSONObject findOrderByUserName(@RequestParam(value="page",defaultValue="1")Integer page,@RequestParam(value="limit",defaultValue="10")Integer limit,@RequestParam("user_name")String user_name) {
		PageInfo<Order> info = orderService.findOrdersByUserName(page, limit, user_name);
		JSONObject obj = new JSONObject();
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", info.getTotal());
		obj.put("data", info.getList());
		return obj;
	}
	
	@RequestMapping("findRefundOrderByUser")
	@ResponseBody
	public JSONObject findRefundOrderByUser(@RequestParam("user_name")String user_name) {
		JSONObject obj = new JSONObject();
		List<Order> list = this.orderService.findRefundOrderByUserName(user_name);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", list.size());
		obj.put("data", list);
		return obj;
	}
	
	@RequestMapping("findAllOrders")
	@ResponseBody
	public JSONObject findAllOrders() {
		JSONObject obj = new JSONObject();
		List<Order> list = orderService.findAllOrders();
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", list.size());
		obj.put("data", list);
		return obj;
	}
	
	@RequestMapping("findAllOrdersPage")
	@ResponseBody
	public JSONObject findAllOrdersPage(@RequestParam(value="page",defaultValue="1")Integer page,@RequestParam(value="limit",defaultValue="10")Integer limit,String keyword) {
		PageInfo<Order> info = orderService.findAllOrdersBySplitPage(page, limit, keyword);
		JSONObject obj = new JSONObject();
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", info.getTotal());
		obj.put("data", info.getList());
		return obj;
	}
	
	@RequestMapping("findAllRefundOrder")
	@ResponseBody
	public JSONObject findAllRefundOrder(@RequestParam(value="page",defaultValue="1")Integer page,@RequestParam(value="limit",defaultValue="10")Integer limit) {
		JSONObject obj = new JSONObject();
		PageInfo<Order> info = orderService.findOrdersByState(page, limit, 0);
		obj.put("code", 0);
		obj.put("msg", "");
		obj.put("count", info.getTotal());
		obj.put("data", info.getList());
		return obj;
	}
	
//	@RequestMapping("buyTickets")
//	@ResponseBody
//	public JSONObject buyTickets(@RequestParam("schedule_id")long schedule_id,@RequestParam("position[]")String[] position,@RequestParam("price")int price,HttpServletRequest request) {
//		//获取登录用户
//		User user = (User)request.getSession().getAttribute("user");
//		JSONObject obj = new JSONObject();
//		if(user == null) {
//			obj.put("code",200);
//			obj.put("msg", "您未登录,登录之后才可购票~");
//		}else {
//			int done = 0;
//			//总价/票的数量=单价
//			int order_price = price / position.length;
//			String user_id = "";
//			//补零，保证订单的id长度相同
//			switch(String.valueOf(user.getUser_id()).length()) {
//			case 1: user_id = "000" + String.valueOf(user.getUser_id()); break;
//			case 2: user_id = "00" + String.valueOf(user.getUser_id()); break;
//			case 3: user_id = "0" + String.valueOf(user.getUser_id()); break;
//			case 4: user_id = String.valueOf(user.getUser_id()); break;
//			}
//			//有几张票，添加几个订单
//			for(int i = 0;i < position.length;i++) {
//				Order order = new Order();
//				String order_id = "";
//				Date date = new Date();
//				SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
//				//id等于当前日期+用户补零后的id
//				order_id += dateFormat.format(date);
//				order_id += user_id;
//				String index = "";
//				//再把座位号去除汉字，拼到id上
//				switch(position[i].length()) {
//					case 4:
//						index = "0" + position[i].replaceAll("排", "0");
//						index = index.replaceAll("座", "");
//						break;
//					case 5:
//						if(position[i].charAt(2) >= 48 && position[i].charAt(2) <= 57) {
//							index = "0" + position[i].replaceAll("排", "");
//							index = index.replaceAll("座", "");
//						}else {
//							index = position[i].replaceAll("排", "0");
//							index = index.replaceAll("座", "");
//						}
//						break;
//					case 6:
//						index = position[i].replaceAll("排", "");
//						index = index.replaceAll("座", "");
//						break;
//				}
//				order_id += index;
//				order.setOrder_id(order_id);
//				order.setOrder_position(position[i]);
//				order.setSchedule_id(schedule_id);
//				order.setUser_id(user.getUser_id());
//				order.setOrder_price(order_price);
//				order.setOrder_time(new Date());
//				Integer rs = this.orderService.addOrder(order);
//				//座位-1
//				Integer rs1 = this.scheduleService.delScheduleRemain(schedule_id);
//				done++;
//
//			}
//			System.out.println("----------------------------");
//			int i=10/0;
//			System.out.println("----------------------");
//			System.out.println(10/0);
//			if(done == position.length) {
//				//增加票房
//				float sum = (float)price/10000;
//				Integer rs2 = this.movieService.changeMovieBoxOffice(sum, this.scheduleService.findScheduleById(schedule_id).getMovie_id());
//				obj.put("code",0);
//				obj.put("msg", "购票成功~");
//			}else {
//				obj.put("code",200);
//				obj.put("msg", "购票失败~");
//			}
//		}
//		return obj;
//	}
	@RequestMapping("buyTickets")
	@ResponseBody
	public JSONObject buyTickets(@RequestParam("schedule_id")long schedule_id,@RequestParam("position[]")String[] position,@RequestParam("price")int price,HttpServletRequest request) {
		//获取登录用户
		User user = (User)request.getSession().getAttribute("user");
		JSONObject obj = new JSONObject();
		if(user == null) {
			obj.put("code",200);
			obj.put("msg", "您未登录,登录之后才可购票~");
		}else {
			if(position.length == orderService.addOrder(schedule_id,position,price,user)) {
				obj.put("code",0);
				obj.put("msg", "购票成功~");
			}else {
				obj.put("code",200);
				obj.put("msg", "购票失败~");
			}
		}
		return obj;
	}
	@RequestMapping("applyForRefund")
	@ResponseBody
	public JSONObject applyForRefund(@RequestParam("order_id")String order_id) {
		JSONObject obj = new JSONObject();
		Integer rs = orderService.updateOrderStateToRefund(order_id);
		if(rs > 0) {
			obj.put("code", 0);
			obj.put("msg", "退票申请已发送~");
		}else {
			obj.put("code", 200);
			obj.put("msg", "操作失败~");
		}
		return obj;
	}
	
	@RequestMapping("agreeForRefund")
	@ResponseBody
	public JSONObject agreeForRefund(@RequestParam("order_id")String order_id) {
		JSONObject obj = new JSONObject();
		Integer rs = this.orderService.updateOrderStateToRefunded(order_id);
		if(rs > 0) {
			Order order = this.orderService.findOrderById(order_id);
			int price = order.getOrder_price();
			long movie_id = order.getOrder_schedule().getMovie_id();
			Integer rs2 = this.movieService.changeMovieBoxOffice((float)price/10000, movie_id);
			obj.put("code", 0);
			obj.put("msg", "退票成功");
		}else {
			obj.put("code", 200);
			obj.put("msg", "退票失败");
		}
		return obj;
	}
}
