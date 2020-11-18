package com.myspring.pro30.member.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.member.vo.MemberVO;

@Repository("memberDAO")
public class MemberDAO {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private MemberVO memberVO;
	
	public List<MemberVO> selectAllMember() throws DataAccessException {
		List<MemberVO> membersList = new ArrayList<MemberVO>();
		membersList = sqlSession.selectList("mapper.member.selectAllMember");
		return membersList;
	}
	
	public void insertMember(MemberVO member) throws DataAccessException {
		sqlSession.insert("mapper.member.insertMember", member);
	}
	
	public MemberVO selectMemberById(String id) throws DataAccessException {
		memberVO = sqlSession.selectOne("mapper.member.selectMemberById", id);
		return memberVO;
	}
	
	public void updateMember(MemberVO member) throws DataAccessException {
		sqlSession.update("mapper.member.updateMember", member);
	}
	
	public void deleteMember(String id) throws DataAccessException {
		sqlSession.delete("mapper.member.deleteMember", id);
	}
	
	public MemberVO loginById(MemberVO member) throws DataAccessException {
		memberVO = sqlSession.selectOne("mapper.member.login", member);
		return memberVO;
	}
}
