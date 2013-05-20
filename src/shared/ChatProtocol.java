package shared;

public enum ChatProtocol {
	MESSAGE,
	LOGIN,
	LOGOUT,
	JOIN_CHATROOM,
	LEAVE_CHATROOM,
	CREATE_CHATROOM,
	GET_CHATROOM_TITLE,
	SET_CHATROOM_TITLE,
	USER_JOINED,
	USER_LEFT,
	USER_KICKED,
	ADMIN_LOGIN,
	SEND_FILE,
	RECEIVE_FILE
}
