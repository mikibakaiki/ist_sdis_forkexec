package com.forkexec.pts.domain;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; 


import com.forkexec.pts.domain.exception.UserAlreadyExistsException;
import com.forkexec.pts.domain.exception.BadInitException;



/**
 * Points
 * <p>
 * A points server.
 */
public class Points {

    /**
     * Constant representing the default initial balance for every new client
     */
    private static final int DEFAULT_INITIAL_BALANCE = 100;
    /**
     * Global with the current value for the initial balance of every new client
     */
    private final AtomicInteger initialBalance = new AtomicInteger(DEFAULT_INITIAL_BALANCE);

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() { }

	/**
	 * UDDI server URL
	 */
	private String uddiURL = null;

	/**
	 * rst name
	 */
	private String stationTemplateName = null;


    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Points INSTANCE = new Points();
    }

    public static synchronized Points getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
	 * Map of existing users <email, User>. Uses concurrent hash table
	 * implementation supporting full concurrency of retrievals and high
	 * expected concurrency for updates.
	 */
	private Map<String, User> UsersList = new ConcurrentHashMap<>();
	
	public synchronized User activateUser(String email) throws UserAlreadyExistsException {
		
		User user1 = UsersList.get(email);
		if(user1 != null) {
			throw new UserAlreadyExistsException();
		}
		else {
			User user = new User();
			TagDomain tag = new TagDomain();
			tag.setSeq(0);
			user.setEmail(email);
			user.setBalance(initialBalance.get());
			user.setTag(tag);
			UsersList.put(email, user);
			return user;
		}
	}	
	
	public synchronized User getUser(String email) {
		
		User user = UsersList.get(email);
		if(user == null) {
			User user1 = new User();
			TagDomain tag = new TagDomain();
			tag.setSeq(0);
			user1.setEmail(email);
			user1.setBalance(initialBalance.get());
			user1.setTag(tag);
			UsersList.put(email, user1);
			return user1;
		}
		return user;
	}
	
	public synchronized void reset() {
		UsersList.clear();
		initialBalance.set(DEFAULT_INITIAL_BALANCE);
	}
	
	public synchronized void init(int newBalance) throws BadInitException{
		
		if(newBalance < 0 ) {
			throw new BadInitException();
		}
		
		else {
			initialBalance.set(newBalance); 
		}
	}
}
