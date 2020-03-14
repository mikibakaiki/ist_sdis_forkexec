package com.forkexec.pts.domain;

import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.pts.domain.exception.InsufficientPointsException;
import com.forkexec.pts.domain.exception.InvalidPointsException;

public class User {

	private String email;
	private AtomicInteger points;
	private TagDomain tag;
	
	
	public User() {

	}
	
	public void setEmail(String UserEmail){
		this.email = UserEmail;

	}
	
	public void setBalance(int balance) {
		this.points = new AtomicInteger(balance);
	}
	
	public void setTag(TagDomain tag) {
		this.tag = tag;			
	}
	
	public synchronized int spendPoints(int n) throws InsufficientPointsException, InvalidPointsException{
		 
		if(n <= 0){
			throw new InvalidPointsException();
		}
		
		else if((points.get() > 0)&&( (points.get() - n) >= 0)) {
			 points.getAndAdd(-n);
			 return pointsBalance();
		 } else {
			 throw new InsufficientPointsException();
		 }
	}

	/**
	*Pontos sao recebidos em euros fazer conversao para pontos comes
	*/
	public synchronized int addPoints(int pontos) throws InvalidPointsException{
		
		
		if(pontos <= 0){
			throw new InvalidPointsException();
		}
		else {
			points.getAndAdd(pontos);
			return pointsBalance();
		}
	}
	

	public int pointsBalance() {
		return points.intValue();
	}
	
	
	public String getEmail() {
		return email;
	}
	
	public TagDomain getTag() {
		return tag;
	}

}
