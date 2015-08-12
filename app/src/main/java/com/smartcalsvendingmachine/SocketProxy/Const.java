package com.smartcalsvendingmachine.SocketProxy;

public class Const {
	public final static int SERVER_PORT = 1971;
	public final static String OK = "OK";
	public final static String ERROR = "ERROR";
	
	//customer constants
	public final static String BUY_CARD = "BUY_CARD";
	public final static String CHECK_BALANCE = "CHECK_BALANCE";
	public final static String UPDATE_BALANCE = "UPDATE_BALANCE";
	
	//employee constants
	public final static String AUTHENTICATE = "AUTH";
    public final static String CHECK_MACHINE = "CHECK_MACHINE";
	public final static String GET_ITEM_IDS = "GET_ITEM_IDS";
    public final static String GET_OTHER_ITEM_IDS = "GET_OTHER_ITEM_IDS";
    public final static String GET_ITEM = "GET_ITEM";
    public final static String ADD_MACHINE = "ADD_MACHINE";
    public final static String ADD_ITEM_TO_MACHINE = "ADD_ITEM_TO_MACHINE";
    public final static String GET_FILE = "GET_FILE";
	public final static String UPDATE_MACHINE_ITEM_QUANTITY = "UPDATE_MACHINE_ITEM_QUANTITY";
	public final static String DELETE_ITEM_FROM_MACHINE = "DELETE_ITEM_FROM_MACHINE";
	
	//machine constants
	public final static String GET_UPDATED_IDS = "GET_UPDATED_IDS";
	public final static String ADD_SALE = "ADD_SALE";
    public final static String UPDATE_SYNC_DATE = "UPDATE_SYNC_DATE";
}