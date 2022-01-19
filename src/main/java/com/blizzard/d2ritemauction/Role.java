package com.blizzard.d2ritemauction;

import lombok.Getter;

@Getter
public enum Role {

	   BANNED("ROLE_BANNED", "차단된 유저"),
	   USER("ROLE_USER", "일반 사용자");
	
	   Role(String key, String title) {
		this.key = key;
		this.title = title;
	}
	private final String key;
	private final String title;
	public String getKey() {
		return key;
	}
	public String getTitle() {
		return title;
	}

}
