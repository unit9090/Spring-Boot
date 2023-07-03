package com.itwill.spring3.repository.member;

public enum Role {
	
	// enum은 enum 안에서만 생성자를 호출할 수 있다.
	// 그래서 enum은 객체를 미리 생성해야 한다.
	// 아래는 상수를 정의한다.
	// 즉, 상수를 정의하는 클래스를 enum이라고 한다.
	USER("ROLE_USER", "USER"),
	ADMIN("ROLE_ADMIN", "ADMIN");
	
	private final String key;
	private final String name;
	
	Role(String key, String name) {
		this.key = key;
		this.name = name;
	}
	
	public String getKey() {
		return this.key;
	}
	
}
