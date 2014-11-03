package com.moomeen;

import org.springframework.stereotype.Service;

import com.moomeen.endo2java.EndomondoSession;

@Service
public class EndomondoSessionHolder {

	private com.moomeen.endo2java.EndomondoSession internalSession;

	public void init(com.moomeen.endo2java.EndomondoSession session){
		this.internalSession = session;
	}

	public EndomondoSession getSession(){
		return internalSession;
	}

}
