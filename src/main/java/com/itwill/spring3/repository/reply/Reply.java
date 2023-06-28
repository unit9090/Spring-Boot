package com.itwill.spring3.repository.reply;

import com.itwill.spring3.repository.BaseTimeEntity;
import com.itwill.spring3.repository.post.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude = {"post"})
//-> post 제외하고 toString을 만들어줌.
@Entity
@Table(name = "REPLIES")
@SequenceGenerator(name = "REPLIES_SEQ_GEN", sequenceName = "REPLIES_SEQ", allocationSize = 1)
public class Reply extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLIES_SEQ_GEN")
	private Long id;	// Primary Key
	
	@ManyToOne(fetch = FetchType.LAZY)	// EAGER(기본값): 즉시 로딩, LAZY: 지연 로딩.
	//-> 여러 개(Many)의 댓글이 한 개(One)의 포스트에 달려 있을 수 있음을 나타냄.
	private Post post;	// Foreign Key, 관계를 맺고 있는 엔터티.
	
	@Column(nullable = false)	// Not Null
	private String replyText;	// 댓글 내용
	
	@Column(nullable = false)	// Not Null
	private String writer;		// 댓글 작성자

}
