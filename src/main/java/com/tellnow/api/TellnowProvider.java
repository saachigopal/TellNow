package com.tellnow.api;

public enum TellnowProvider {
	facebook("facebook"), twitter("twitter"), securitycode("securitycode");
	private String name;

	TellnowProvider(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static TellnowProvider fromString(String name) {
		if (name != null) {
			for (TellnowProvider b : TellnowProvider.values()) {
				if (name.equalsIgnoreCase(b.name)) {
					return b;
				}
			}
		}
		return null;
	}
}
