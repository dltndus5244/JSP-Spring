package com.myspring.pro30.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.member.dao.MemberDAO;
import com.myspring.pro30.member.vo.MemberVO;

@Service("memberService")
@Transactional(propagation=Propagation.REQUIRED)
public class MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private MemberVO memberVO;
	
	public List<MemberVO> listMembers() throws DataAccessException {
		List<MemberVO> membersList = new ArrayList<MemberVO>();
		membersList = memberDAO.selectAllMember();
		return membersList;
	}
	
	public void addMember(MemberVO member) throws DataAccessException {
		memberDAO.insertMember(member);
	}
	
	public MemberVO findMemberById(String id) throws DataAccessException {
		memberVO = memberDAO.selectMemberById(id);
		return memberVO;
	}
	
	public void modMember(MemberVO member) throws DataAccessException {
		memberDAO.updateMember(member);
	}
	
	public void delMember(String id) throws DataAccessException {
		memberDAO.deleteMember(id);
	}
	
	public MemberVO login(MemberVO member) throws DataAccessException {
		memberVO = memberDAO.loginById(member);
		return memberVO;
	}
}
