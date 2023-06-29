package com.itwill.spring3.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyUpdateDto {
	
	private long replyId;
	private String replyText;
	
}
