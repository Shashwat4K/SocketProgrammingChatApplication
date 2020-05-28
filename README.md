# Chat Application based on Java Socket Programming
A plain and simple terminal-based text chat application, using Java Socket programming. To be used over LAN.
Multithreaded Client-Server application, with a single server and multiple clients.

To run,
Every participant must have Java installed in their PC or laptop and must be connected to the **same Local Area Network**.<br/>
Every participant should Compile <i>Server.java</i> and <i>Client.java</i> by:<br>
<code>javac Server.java</code><br>
<code>javac Client.java</code><br>

One participant must start the server by typing <code> java Server </code><br>
Every participant (including the one who started the Server) must run <code>java Client</code> from the same directory where the java Class files are generated (Or where the Server.class or Client.class reside).<br>

Enter the IP address and port number (IP address of the device on which the Server has started, Port number is decided by the Server runner default is 1234).<br>

Enter your Username and start chatting!<br>
**Warning**: This application is still under development, Some exceptions here and there.
## **Things to do in future versions**
<ul>
  <li> Personal messaging facilty, Selective Recipient facility </li>
  <li> Adding smooth exception less performance </li>
  <li> Background notifications </li>
  <li> File transfer facility </li> 
</ul>  

## **Introducing: '$' Commands** <br>
Adding few special commands with '$' prefix which will perform special tasks. <br>
<ol>
  <li> <b>$USERS</b> : Retrieves Usernames of currently connected Users. </li> 
</ol>  
