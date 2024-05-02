package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Member;

import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class MemberService implements UserDetailsService {

	@Autowired
	private MemberDAO dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public MemberService() {
		System.out.println("MemberService 생성됨");
	}
	
	public void insert(Member m ) {
		m.setPasswd(passwordEncoder.encode(m.getPasswd()) );
		dao.save(m);
	}
	
	public void update(Member m ) {
		dao.save(m);
	}
	
	public int checkMember(String jumin1, String jumin2) {
		return dao.checkMember(jumin1, jumin2);
	}
	
	public int checkId(String id) {
		return dao.checkId(id);
	}
	
	public String getMemberPasswd(String id) {
		return dao.getMemberPasswd(id);
	}
	
	public List<Member> findMemberId(String id){
		return dao.findMemberId(id);
	}
	
	public List<Member> findMemberName(String name){
		return dao.findMemberName(name);
	}
	
	public Member getMember(String id) {
		return dao.getMember(id);
	}
	
	public List<Member> searchMemberId(String name, String jumin1, String jumin2){
		return dao.searchMemberId(name, jumin1, jumin2);
	}
	
	public List<Member> searchMemberPasswd(String id, String jumin1, String jumin2){
		return dao.searchMemberPasswd(id, jumin1, jumin2);
	}
	
	public int deleteMember(String id) {
		return dao.deleteMember(id);
	}
	
	public Optional<Member> findById(String id){
		return dao.findById(id);
	}
	
	// 이메일으로 회원 찾기
	public Member findByEmail(String email){
		return dao.findByEmail(email);
	}
	
	// 전화번호로 회원 찾기
	public Member findByPhone(String phone){
		return dao.findByPhone(phone);
	}
	
	// 비밀번호 변경
	public int changePasswd(String id, String pwd) {
		return dao.changePasswd(id, pwd);
	}
	
	public List<Member> memberList(){
		return dao.memberList();
	}
	
	public List<Member> getPosition(String role){
		return dao.getPosition(role);
	}
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("loadUserByUsername 동작함!");
		System.out.println("username:"+username);
		
		Optional<Member> obj = dao.findById(username);
		UserDetails user = null;
		if(obj.isPresent()) {
			Member m = obj.get();
			user = User.builder().username(username).password(m.getPasswd()).roles(m.getRole()).build();
		}else {
			throw new UsernameNotFoundException(username);
		}
		
		return user;
	}

}
