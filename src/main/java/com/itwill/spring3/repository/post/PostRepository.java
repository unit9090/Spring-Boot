package com.itwill.spring3.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	// id 내림차순 정렬:
	// SELECT * FROM POSTS ORDER BY ID DESC
	List<Post> findByOrderByIdDesc();
	
	// 제목으로 검색:
	// SELECT * FROM POSTS P
	// WHERE LOWER(P.TITLE) LIKE LOWER('%' || ? || '%')
	// ORDER BY P.ID DESC
	List<Post> findByTitleContainsIgnoreCaseOrderByIdDesc(String title);
	
	// 내용으로 검색:
	// SELECT * FROM POSTS P
	// WHERE LOWER(P.CONTENT) LIKE LOWER('%' || ? || '%')
	// ORDER BY P.ID DESC
	List<Post> findByContentContainsIgnoreCaseOrderByIdDesc(String content);

	// 작성자로 검색:
	// SELECT * FROM POSTS P
	// WHERE LOWER(P.AUTHOR) LIKE LOWER('%' || ? || '%')
	// ORDER BY P.ID DESC
	List<Post> findByAuthorContainsIgnoreCaseOrderByIdDesc(String author);
	
	// 제목 또는 내용으로 검색:
	// SELECT * FROM POSTS P
	// WHERE LOWER(P.TITLE) LIKE LOWER('%' || ? || '%')
	// OR LOWER(P.CONTENT) LIKE LOWER('%' || ? || '%')
	// ORDER BY P.ID DESC
	List<Post> findByTitleContainsIgnoreCaseOrContentContainsIgnoreCaseOrderByIdDesc(String title, String content);
	
	// JPQL(JPA Query Language) 문법으로 쿼리를 작성하고, 그 쿼리를 실행하는 메서드 이름을 설정
	// JPQL은 Entity 클래스의 이름과 필드 이름들을 사용해서 작성.
	// (주의) DB 테이블 이름과 컬럼 이름을 사용하지 않음!
	@Query(
		"SELECT p FROM Post p " +
		"WHERE LOWER(p.title) LIKE LOWER('%' || :keyword || '%') " +
		"OR LOWER(p.content) LIKE LOWER('%' || :keyword || '%') " +
		"ORDER BY p.id DESC"
	)
	List<Post> searchByKeyword(@Param("keyword") String keyword);
	
	
}
