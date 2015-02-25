/**
 * 
 */
/**
 * This package contains many classes containing the server. 
 * Abstract AbstractServer implementing Runnable, which is extended by EchoServer.
 * AbstractServer is using the Class ConnectionToClient and is passing it down. 
 * EchoServer takes ConnectionToClient and adds functionality to AbstractServer.
 * EchoServer is using ChatIf, which is in the client package to help display messages.
 * EchoServer is using RoomList to help manage the ConnectionToClient Thread[].
 * RoomList is implementing RoomListInterface, and is an ArrayList of Rooms.
 * Room is implementing RoomInterface and Comparable, and is an ArrayList of ClientInfo.
 * ClientInfo is implementing Comparable, and is taking in ConnectionToClient.
 * @author Tyler M
 *
 */
package server;