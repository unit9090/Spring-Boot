package com.itwill.spring3.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCreateDto {
	
	private long postId;
	private String replyText;
	private String writer;
	
}
