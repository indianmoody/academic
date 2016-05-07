
// Author: Gaurav Bishnoi

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

/******************************** Variables **************************************************/

char var1[15], var2[15], var3[15];
int var4, i = 1, j = 0;
char serverName[8][10], IPaddress[8][10], username[15], password[15];
int portNo[8];
char chunk[6][10000];

char command[50], method[10], filename[10], commandBuffer[50], loginBuffer[50], ackLoginBuffer[100], acceptance[20];
char s[2] = " ";
char *token;

int sock, cli;                                // Socket descriptors for server and client
struct sockaddr_in server, client;                    // Address structure for server and client
int len = sizeof(struct sockaddr_in);

int listSize;

char getChunk[6][10000], getFullChunk[40000];
int received[6], totalReceived;
/************************************************************************************************/

int main ()
{

    // Reading Configuration File
    FILE *fd;
        fd = fopen("dfc.conf", "r");

        while (fscanf(fd, "%s %s %s %i", var1, var2, var3, &var4) != EOF)
        {
            if (strcmp(var1, "Server") == 0)
            {
                    strcpy(serverName[i], var2);
                    strcpy(IPaddress[i], var3);
                    portNo[i] = var4;
            }

            else if (strcmp(var1, "Username:") == 0)
            {
                    strcpy(username, var2);
            }

            else if (strcmp(var1, "Password:") == 0)
            {
                    strcpy(password, var2);
            }
            i++;
        }

        for ( j=1; j<=4; j++)
        {
            printf("Server: %s\t", serverName[j]);
        }

        printf("\n");

        for ( j=1; j<=4; j++)
        {
            printf("IP: %s\t", IPaddress[j]);
        }

        printf ("\n");

        for ( j=1; j<=4; j++)
        {
            printf("portNo: %i\t", portNo[j]);
        }

        printf("\nUsername: %s    Password: %s\n", username, password);

        fclose(fd);
    // dfc.conf has been read


    // fill loginBuffer
    memset ( (void*)loginBuffer, (int) '\0', 50);
    strcpy (loginBuffer, username);
    strcat (loginBuffer, " ");
    strcat (loginBuffer, password);


    while (fgets(command, sizeof(command), stdin)  != NULL)
    {

        memset ( (void*)commandBuffer, (int) '\0', 50);
        strcpy(commandBuffer, command);
    printf("commandBuffer: %s\n", commandBuffer);

        // Extraction of method, filename
	sscanf(command, "%s %s", method, filename);        

        printf ("method = %s, filename = %s\n username = %s, password = %s\n", method, filename, username, password);

        // Creating socket descriptor for client
            if((cli = socket(AF_INET, SOCK_STREAM, 0)) == -1)
            {
                perror("client: ");
                exit(-1);
            }




/******************************************** PUT PUT PUT ********************************/
        // Comparing method with command 'PUT'
        if (strcmp(method, "PUT") == 0)
        {
            int loopNumber = 1;


            /******************* tackling file (filename) *************/
    FILE *fput;
    int len2, len1, k;
    

       fput = fopen(filename, "r");
       if( fput == NULL )
       {
          perror ("Error opening file");
          return(-1);
       }
       fseek(fput, 0, SEEK_END);

       len2 = ftell(fput);
    printf("Total size of file = %d bytes\n", len2);

    len1 = len2/4;
	printf ("1/4th of length = %d bytes\n", len1);

    rewind(fput);

    for (k=1; k<5; k++)
    {
        fread(chunk[k], 1, len1, fput);
    }

	printf("%s\n", chunk[2]);
	
	fclose(fput);
    // file has been divided into chunks






            /**********************************************************/


            for (loopNumber=1; loopNumber<5; loopNumber++)
            {


        // Creating socket descriptor for client
            if((cli = socket(AF_INET, SOCK_STREAM, 0)) == -1)
            {
                perror("client: ");
                exit(-1);
            }

// configuring settings of server address structure
            server.sin_family = AF_INET;
            server.sin_port = htons(portNo[loopNumber]); // setting port number according to server
            server.sin_addr.s_addr = inet_addr("127.0.0.1");
            bzero(&server.sin_zero, 8);

                connect(cli, (struct sockaddr *)&server, len);

                // send login details to server
                if (send(cli, loginBuffer, strlen(loginBuffer), 0) < 0)
                {
                    printf("login details not sent\n");
                }

                // receiving acknowledgement for login details
                memset ( (void*)ackLoginBuffer, (int) '\0', 100);
                if (recv(cli, ackLoginBuffer, 100, 0) < 0)
                {
                    printf("acknowledge for login buffer not received!\n");
                }

                //checking for Valid or Invalid
                sscanf(ackLoginBuffer, "%s", acceptance);
                if (strcmp(acceptance, "Valid") == 0)
                {

                    // send command
                    if (send(cli, commandBuffer, strlen(commandBuffer), 0) < 0)
                    {
                        printf("command not sent\n");
                    }

                char buffu[20];
                memset ( (void*)buffu, (int) '\0', 20);
                if (recv(cli, buffu, 20, 0) < 0)
                {
                    printf("acknowledge for login buffer not received!\n");
                }
                else
                {
                    printf("buffu: %s\n", buffu);
                }


                // send chunks
                if ((loopNumber == 1) || (loopNumber == 2) || (loopNumber == 3))
                {

                    if (send (cli, chunk[loopNumber], len1+1, 0) < 0)
                    {
                        printf ("error with sending chunk\n");
                    }

                    char buffu1[20];
                    memset ( (void*)buffu1, (int) '\0', 20);
                    if (recv(cli, buffu1, 20, 0) < 0)
                    {
                        printf("problem with buffu1");
                    }


                    if (send (cli, chunk[loopNumber+1], len1+1, 0) < 0)
                    {
                        printf ("error with sending chunk\n");
                    }

                }

                else if (loopNumber == 4)
                {

                    if (send (cli, chunk[4], len1+1, 0) < 0)
                    {
                        printf ("error with sending chunk\n");
                    }

                    char buffu2[20];
                    memset ( (void*)buffu2, (int) '\0', 20);
                    if (recv(cli, buffu2, 20, 0) < 0)
                    {
                        printf("problem with buffu2");
                    }


                    if (send (cli, chunk[1], len1+1, 0) < 0)
                    {
                        printf ("error with sending chunk\n");
                    }

                }


                }

                else
                {
                    printf ("Invalid Username or Password");
                }



        close(cli);


            }

        }

/***************************************** GET GET GET GET **********************************************/
// Comparing method with command 'GET'
        if (strcmp(method, "GET") == 0)
        {
            int loopNumber = 1;
            for (loopNumber=1; loopNumber<5; loopNumber++)
            {


        	// Creating socket descriptor for client
            	if((cli = socket(AF_INET, SOCK_STREAM, 0)) == -1)
            	{
                	perror("client: ");
                	exit(-1);
            	}

		// configuring settings of server address structure
            	server.sin_family = AF_INET;
            	server.sin_port = htons(portNo[loopNumber]); // setting port number according to server
            	server.sin_addr.s_addr = inet_addr("127.0.0.1");
            	bzero(&server.sin_zero, 8);

                connect(cli, (struct sockaddr *)&server, len);

                // send login details to server
                if (send(cli, loginBuffer, strlen(loginBuffer), 0) < 0)
                {
                    printf("login details not sent\n");
                }

                // receiving acknowledgement for login details
                memset ( (void*)ackLoginBuffer, (int) '\0', 100);
                if (recv(cli, ackLoginBuffer, 100, 0) < 0)
                {
                    printf("acknowledge for login buffer not received!\n");
                }

                //checking for Valid or Invalid
                sscanf(ackLoginBuffer, "%s", acceptance);
                if (strcmp(acceptance, "Valid") == 0)
                {

                    // send command
                    if (send(cli, commandBuffer, strlen(commandBuffer), 0) < 0)
                    {
                        printf("command not sent\n");
                    }

        	
        	memset ( (void*)getChunk[loopNumber], (int) '\0', 10000);
                if ((received[loopNumber] = recv(cli, getChunk[loopNumber], 10000, 0)) < 0)
                	{
                    		printf("chunk not received!\n");
                	}
        	else
        		{
            			printf("buffu: %s\n", getChunk[loopNumber]);

                	}

        	}

                else
                {
                    printf ("Invalid Username or Password");
                }



        	close(cli);


            }

	FILE *fget;
   	fget = fopen( filename , "w+" );


	// Writing into file
	totalReceived = received[1] + received[2] + received[3] + received[4] - 4;
	strcpy(getFullChunk, getChunk[1]);
	strcat(getFullChunk, getChunk[2]);
	strcat(getFullChunk, getChunk[3]);
	strcat(getFullChunk, getChunk[4]);
	fwrite(getFullChunk , 1 , totalReceived , fget);

	fclose(fget);	


        }


/**********************************************************************************************************/

/**************************LIST LIST LIST LIST **********************************************************/
// Comparing method with command 'LIST'
        if (strcmp(method, "LIST") == 0)
        {
            int loopNumber = 1;
            for (loopNumber=1; loopNumber<5; loopNumber++)
            {


        // Creating socket descriptor for client
            if((cli = socket(AF_INET, SOCK_STREAM, 0)) == -1)
            {
                perror("client: ");
                exit(-1);
            }

// configuring settings of server address structure
            server.sin_family = AF_INET;
            server.sin_port = htons(portNo[loopNumber]); // setting port number according to server
            server.sin_addr.s_addr = inet_addr("127.0.0.1");
            bzero(&server.sin_zero, 8);

                connect(cli, (struct sockaddr *)&server, len);

                // send login details to server
                if (send(cli, loginBuffer, strlen(loginBuffer), 0) < 0)
                {
                    printf("login details not sent\n");
                }

                // receiving acknowledgement for login details
                memset ( (void*)ackLoginBuffer, (int) '\0', 100);
                if (recv(cli, ackLoginBuffer, 100, 0) < 0)
                {
                    printf("acknowledge for login buffer not received!\n");
                }

                //checking for Valid or Invalid
                sscanf(ackLoginBuffer, "%s", acceptance);
                if (strcmp(acceptance, "Valid") == 0)
                {

                    // send command
                    if (send(cli, commandBuffer, strlen(commandBuffer), 0) < 0)
                    {
                        printf("command not sent\n");
                    }

        char listBuffer[200];
        memset ( (void*)listBuffer, (int) '\0', 100);
                if ((listSize = recv(cli, listBuffer, 200, 0)) < 0)
                {
                    printf("list if files not received!\n");
                }
        else
        {
            printf("%s\n", listBuffer);

                }

        }

                else
                {
                    printf ("Invalid Username or Password");
                }



        close(cli);


            }

        }




/**********************************************************************************************************/

    }
    return 0;
}


