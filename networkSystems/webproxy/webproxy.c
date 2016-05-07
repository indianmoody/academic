#include<stdio.h>
#include<stdlib.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netinet/in.h>
#include<error.h>
#include<string.h>
#include<unistd.h>
#include<arpa/inet.h>
#include<netdb.h>
#include <sys/stat.h>
#include <dirent.h>


/******************************* Variables *******************************************************/
int portNo = 10001;		//port number for proxy
int len;				//sizeof(sockaddr_in)

char commandBuffer[10], siteBuffer[50], methodBuffer[20];
char replyServerBuffer[40960];
/***************************************************************************************************/


void main(int argc, char *argv[])
{
	
	portNo = atoi(argv[1]);					// Assigning port number to proxy
	
	// creating server socket descriptor
	int sockClient, sockClient2, sockServer;
    struct sockaddr_in client, server;
    len = sizeof(struct sockaddr_in);
    
    /*********************** getaddrinfo zone *************************/
    
    int status;
    struct addrinfo hints;
    struct addrinfo *serverInfo;
    
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE;
    
    /**********************************************************************/
	
   	/*if((sockServer = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    	{
          	perror("client: ");
            exit(-1);
        }
        
    server.sin_family = AF_INET;
    server.sin_port = htons(10500);
    server.sin_addr.s_addr = INADDR_ANY;
    bzero(&server.sin_zero, 8);                                   */
    // server socket structure is created
	
	
	// Creating client socket descriptor, binding, listening
    if((sockClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("socket: ");
        exit(-1);
    }

    client.sin_family = AF_INET;
    client.sin_port = htons(portNo);
    client.sin_addr.s_addr = INADDR_ANY;
    bzero(&client.sin_zero, 8);

    if((bind(sockClient, (struct sockaddr *)&client, len)) == -1)
    {
        perror("bind: ");
        exit(-1);
    }

    if((listen(sockClient, 5)) == -1)
    {
        perror("listen: ");
        exit(-1);
    }
    // Now client is listening
    
    
    
    
    // client connects
    
    while (1)
    {
    	// Accept connection from client
    	if((sockClient2 = accept(sockClient, (struct sockaddr *)&client, &len)) == -1)
        {
            perror("accept: ");
            exit(-1);
        }
        
        // receive request
        char requestBuffer[50];
        memset ( (void*)requestBuffer, (int) '\0', 50);
        if (recv(sockClient2, requestBuffer, 50, 0) <= 0)
        {
            printf("message not received");
        }

        else
        {
            printf("message from client: %s\n", requestBuffer);

        }
        
        // Scanning Command, URL and File type from Request
        sscanf(requestBuffer, "%s %s %s", commandBuffer, siteBuffer, methodBuffer);
        
        // verifying for "GET"
        if (strcmp(commandBuffer, "GET") == 0)
        {
        	
        	//creating socket descriptor for website/server
        	getaddrinfo(siteBuffer, "http", &hints, &serverInfo);
        	
        	sockServer = socket(serverInfo->ai_family, serverInfo->ai_socktype, serverInfo->ai_protocol);
        	//bind(sockServer, serverInfo->ai_addr, serverInfo->ai_addrlen);
        	
        	if(connect(sockServer, serverInfo->ai_addr, serverInfo->ai_addrlen) < 0)
        	{
        		printf("connection to server failed!\n");
        	}
        	else
        	{
        		printf("Connected to server, YAEY\n");
        	}
        	
        	char *requestBuffer2 = "GET http://www.google.com/ HTTP/1.1\r\nHost: www.google.com\r\n\r\n";
        	
        	if (send(sockServer, requestBuffer2, strlen(requestBuffer2), 0) < 0)
                {
                    printf("login details not sent\n");
                }
            else
            {
            	printf("message sent: %s\n", requestBuffer2);
            }
                
            
            memset ( (void*)replyServerBuffer, (int) '\0', 40960);
                if (recv(sockServer, replyServerBuffer, 40960, 0) < 0)
                {
                    printf("acknowledge for login buffer not received!\n");
                }
                
                else
                {
                	printf("data received from server: %s\n", replyServerBuffer);
                }
            
            
            if (send(sockClient, replyServerBuffer, strlen(replyServerBuffer), 0) < 0)
                {
                    printf("login details not sent to client\n");
                }
        	
        }
        
    }
     
    
    
	
}
