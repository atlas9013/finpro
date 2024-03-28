package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Member;

import jakarta.transaction.Transactional;

@Repository
public interface MemberDAO extends JpaRepository<Member, String> {
	
	@Query(value = "select count(*) from member where jumin1 = ?1 and jumin2 = ?2", nativeQuery = true)
	public int checkMember(String jumin1, String jumin2);	
	
	@Query(value = "select count(*) from member where id = ?1", nativeQuery = true)
	public int checkId(String id);	
	
	@Query(value = "select passwd from member where id = ?1", nativeQuery = true)
	public String getMemberPasswd(String id);	
	
	@Query(value = "select * from member where id = ?1", nativeQuery = true)
	public List<Member> findMemberId(String id);
	
	@Query(value = "select * from member where name = ?1", nativeQuery = true)
	public List<Member> findMemberName(String name);
	
	@Query(value = "select * from member where id = ?1", nativeQuery = true)
	public Member getMember(String id);
	
	@Query(value = "select * from member where name = ?1 and jumin1 = ?2 and jumin2 = ?3", nativeQuery = true)
	public List<Member> searchMemberId(String name, String jumin1, String jumin2);
	
	@Query(value = "select * from member where id = ?1 and jumin1 = ?2 and jumin2 = ?3", nativeQuery = true)
	public List<Member> searchMemberPasswd(String id, String jumin1, String jumin2);
	
	
	@Modifying
	@Query(value = "delete from member where id = ?1", nativeQuery = true)
	@Transactional
	public int deleteMember(String id);
	
	// 아이디로 회원 찾기
	public Optional<Member> findById(String id);
		
	// 이메일으로 회원 찾기
	public Optional<Member> findByEmail(String email);
	
	// 전화번호로 회원 찾기
	public Optional<Member> findByPhone(String phone);
	
//	// 비밀번호 변경
//	public int changePwd(String id, String pwd);

}

