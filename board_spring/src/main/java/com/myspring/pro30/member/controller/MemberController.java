package com.myspring.pro30.member.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.pro30.member.service.MemberService;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping("/member")
public class MemberController {
	static Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;
	
	@RequestMapping(value="/listMembers.do", method=RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<MemberVO> membersList = new ArrayList<MemberVO>();
		membersList = memberService.listMembers();
		
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		
		return mav;
				
	}
	
	@RequestMapping("/memberForm.do")
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@RequestMapping(value="/addMember.do", method=RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		memberService.addMember(member);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}
	
	@RequestMapping(value="/modForm.do", method=RequestMethod.GET)
	public ModelAndView modForm(@RequestParam("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		MemberVO member = memberService.findMemberById(id);
		String viewName = (String) request.getAttribute("viewName");
		
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("member", member);
		
		return mav;
	}
	
	@RequestMapping(value="/modMember.do", method=RequestMethod.POST)
	public ModelAndView modMember(@ModelAttribute("member") MemberVO member,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		memberService.modMember(member);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}
	
	@RequestMapping(value="/delMember.do", method=RequestMethod.GET)
	public ModelAndView delMember(@RequestParam("id") String id,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		memberService.delMember(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}
	
	@RequestMapping(value="/loginForm.do", method=RequestMethod.GET)
	public ModelAndView loginForm(@RequestParam(value="action", required=false) String action, 
								  @RequestParam(value="articleNO", required=false) String s_articleNO,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		//글쓰기창 요청명을 action 속성으로 세션에 저장합니다.
		HttpSession session = request.getSession();
		session.setAttribute("action", action);
		
		//답글쓰기 하기 전에 로그인 체크
		if (s_articleNO != null) {
			session.setAttribute("s_articleNO", s_articleNO);
		}
		
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("member") MemberVO member,
							RedirectAttributes rAttr,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		
		if (memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
		
			//로그인 성공 시 세션에 저장된 action 값을 가져옵니다.
			String action = (String) session.getAttribute("action");
			session.removeAttribute("action");
			
			//로그인 성공 시 mav에 articleNO add해줌
			String s_articleNO = (String) session.getAttribute("s_articleNO");
			session.removeAttribute("s_articleNO");
			if (s_articleNO != null) {
				int articleNO = Integer.parseInt(s_articleNO);
				mav.addObject("articleNO", articleNO);
			}
			if (action != null) { //action 값이 null이 아니면 action 값을 뷰이름으로 지정해 글쓰기창으로 이동합니다.
				mav.setViewName("redirect:" + action);
			} else { 
				mav.setViewName("redirect:/member/listMembers.do");
			}
		} else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");
		}
		
		return mav;
	}
	
	@RequestMapping(value="/logout.do", method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}
	
}
