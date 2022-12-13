package com.oopsmails.lucenesearch.model;

import java.util.HashMap;
import java.util.Map;

public enum Country {
	
	CANADA("CA","images/cdn_flag_colour.gif"),
	USA("US","images/us_flag_colour.gif"),
	UNKNOWN("UN","images/transp.gif"); // "Blank" image flag for unknown country

	private String code;
	private String imageRelativeURL;

	private static final Map<String,Country> lookup = new HashMap<String, Country>();
	static
	{
		for ( Country value : values() )
		{
			if ( !lookup.containsKey( value.code ) )
			{
				lookup.put( value.code, value );
			}
		}
	}

	private Country(String code, String imageRelativeURL) {
		this.code = code;
		this.imageRelativeURL = imageRelativeURL;
	}

	public String getCode() {
		return code;
	}
	
	public String imageRelativeURL() {
		return imageRelativeURL;
	}


	public Country[] getValues() { return values(); }
	
	public static Country lookup( String code )
	{
		return
			lookup.containsKey( code )
				? lookup.get( code )
				: UNKNOWN;
	}

}
