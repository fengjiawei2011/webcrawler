package sjsu.cmpe282.webcawler.util;

import java.util.UUID;

public class IDProducer {
	
	public static String getId(){
		
		return UUID.randomUUID().toString();
	}
}
