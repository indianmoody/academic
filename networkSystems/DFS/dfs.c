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
#include <sys/stat.h>
#include <dirent.h>


/************************** Variables ****************************************/
char var1[15], var2[15];
char username[10][15], password[10][15];
int scanner = 1, serverNumber = 1;
char box[5][10000];

int checkUser = 1;
int flag1 = 0;
char tempUser[15], tempPassword[15];
char commandBuffer[40];
char method[15], filename[15];

char currentDirectory[100], path[100];

int received;

char listBuffer[200];

int list3;
char getChunk[6][10000];

int len3;
char getPath[100];
/******************************************************************************/

void main(int argc, char *argv[])
{
    int portno = atoi(argv[2]);
    char serverName[15];
    strcpy(serverName, argv[1]);

    //char string1[] = "/DFS1";
    //char string2[15];
    //strcpy(string2, string1);
    char s1[2];
    strcpy(s1,&serverName[(strlen(serverName)-1)]);
    serverNumber = atoi(s1);
    //printf("i = %i\n", i);

    // Reading dfs.conf file
    FILE *fp;
    fp = fopen("dfs.conf", "r");

    while (fscanf(fp, "%s %s", var1, var2) != EOF)
    {
        strcpy(username[scanner], var1);
        strcpy(password[scanner], var2);
        scanner++;
    }
    // dfs.conf file has been read and usernames and passwords have been saved


    // Creating server and client socket descriptors, binding, listening
    int sock, cli;
    struct sockaddr_in server, client;
    int len = sizeof(struct sockaddr_in);

    if((sock = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("socket: ");
        exit(-1);
    }

    server.sin_family = AF_INET;
    server.sin_port = htons(portno);
    server.sin_addr.s_addr = INADDR_ANY;
    bzero(&server.sin_zero, 8);

    if((bind(sock, (struct sockaddr *)&server, len)) == -1)
    {
        perror("bind: ");
        exit(-1);
    }

    if((listen(sock, 5)) == -1)
    {
        perror("listen: ");
        exit(-1);
    }
    // Now server is listening


    while(1)
    {
        // Accepting the connection
        if((cli = accept(sock, (struct sockaddr *)&client, &len)) == -1)
        {
            perror("accept: ");
            exit(-1);
        }


        // Printing Acknowledgement for coonection acceptance
        else
        {
            printf("New client has been connected from port no %d and IP %s\n", ntohs(client.sin_port), inet_ntoa(client.sin_addr));
        }


        // Receiving Username and Password
        char loginBuffer[50];
        memset ( (void*)loginBuffer, (int) '\0', 50);
        if (recv(cli, loginBuffer, 50, 0) <= 0)
        {
            printf("message not received");
        }

        else
        {
            printf("message from client: %s\n", loginBuffer);

        }


        // Scanning Client username and password
        sscanf(loginBuffer, "%s %s", tempUser, tempPassword);

        // Verify Username and Password
        //int checkUser = 1;
        flag1 = 0;
        for (checkUser = 1; checkUser<scanner; checkUser++)
        {
            if (strcmp(username[checkUser], tempUser) == 0)
            {
                if (strcmp(password[checkUser], tempPassword) == 0)
                {
                    char *validBuffer = "Valid username and password: Enter the Command...";
                    flag1++;
                    if (send(cli, validBuffer, strlen(validBuffer), 0) < 0)
                    {
                        printf("message not sent\n");
                    }
                    break;
                }
            }
        }

        if (flag1 == 0)
        {
            char *invalidBuffer = "Invalid Username or Password";
            if (send(cli, invalidBuffer, strlen(invalidBuffer), 0) < 0)
            {
                printf("invalid buffer not sent\n");
            }
        }

        // If username and password are valid then proceed...
        if (flag1 == 1)
        {
            printf("Waiting for command\n");

            //char commandBuffer[40];
            memset ( (void*)commandBuffer, (int) '\0', 40);
            if (recv(cli, commandBuffer, 40, 0) <= 0)
            {
                printf("message not received");
            }

            else
            {
                printf("message from client: %s\n", commandBuffer);

            }

            // Extracting command type (method) and file name
        memset ( (void*)method, (int) '\0', 15);
        memset ( (void*)filename, (int) '\0', 15);
            sscanf(commandBuffer, "%s %s", method, filename);

        printf ("%s\n", method);

	// Creating and opening directory path 
	memset(currentDirectory, 0, sizeof(currentDirectory));
	strcpy(currentDirectory, ".");
	strcat(currentDirectory, serverName);
	struct stat st1 = {0};
    	if (stat(currentDirectory, &st1) == -1)
    	{
        	mkdir(currentDirectory, 0777);
    	} 

	strcat(currentDirectory, "/");
	strcat(currentDirectory, tempUser);

	struct stat st2 = {0};
    	if (stat(currentDirectory, &st2) == -1)
    	{
        	mkdir(currentDirectory, 0777);
    	} 
/*	DIR *pDir;
    	struct dirent *pDirent;
	pDir = opendir(currentDirectory);

	close(pDir);
*/


            // Dividing according to commands

/************************ PUT PUT PUT ********************************************************/

            if (strcmp(method, "PUT") == 0)
            {
                printf("command received: PUT");

                if (send(cli, method, strlen(method), 0) < 0)
                {
                    printf ("Error with sending!");
                }

                // Receiving Chunks
                //char box[5][1024];
                //memset ( (void*)box, (int) '\0', 1024);

                if ((serverNumber == 1) || (serverNumber == 2) || (serverNumber == 3) )
                {
                    //memset ( (void*)box[serverNumber], (int) '\0', 1024);
                    if ((received = recv(cli, box[serverNumber], sizeof(box[serverNumber]), 0)) <= 0)
                    {
                        printf("box not received");
                    }
                    else
                    {
                        printf ("chunk received: %s\n", box[serverNumber]);
                    }

		// create/open file 
		memset(path, 0, sizeof(path));
		strcpy(path, currentDirectory);
		strcat(path, "/");
		strcat(path,"1.");
		strcat(path, filename);

		FILE *fput1;
   		fput1 = fopen( path , "w+" );


		// Writing first chunk into file
		printf ("The size of received buffer: %i\n", received);
		received = received-1;
		fwrite(box[serverNumber] , 1 , received , fput1);
		fclose(fput1);



                    if (send(cli, method, strlen(method), 0) < 0);
                    {
                        printf ("Error with sending!");
                    }

                    //memset ( (void*)box[serverNumber+1], (int) '\0', 1024);
                    if ((received = recv(cli, box[serverNumber+1], sizeof(box[serverNumber]), 0)) <= 0)
                    {
                        printf("box not received");
                    }

		// create/open file 
		memset(path, 0, sizeof(path));
		strcpy(path, currentDirectory);
		strcat(path, "/");
		strcat(path,"2.");
		strcat(path, filename);

		FILE *fput2;
   		fput2 = fopen( path , "w+" );


		// Writing first chunk into file
		received = received-1;
		fwrite(box[serverNumber+1] , 1 , received , fput2);
		fclose(fput2);
	


                }

                else if (serverNumber == 4)
                {

                    //memset ( (void*)box[4], (int) '\0', 1024);
                    if ((received = recv(cli, box[4], 10000, 0)) <= 0)
                    {
                        printf("box not received");
                    }

		// create/open file 
		memset(path, 0, sizeof(path));
		strcpy(path, currentDirectory);
		strcat(path, "/");
		strcat(path,"1.");
		strcat(path, filename);

		FILE *fput3;
   		fput3 = fopen( path , "w+" );


		// Writing first chunk into file
		received = received-1;
		fwrite(box[serverNumber] , 1 , received , fput3);
		fclose(fput3);


                    if (send(cli, method, strlen(method), 0) < 0);
                    {
                        printf ("Error with sending!");
                    }

                    //memset ( (void*)box[1], (int) '\0', 1024);
                    if ((received = recv(cli, box[1], 10000, 0)) <= 0)
                    {
                        printf("box not received");
                    }

		// create/open file 
		memset(path, 0, sizeof(path));
		strcpy(path, currentDirectory);
		strcat(path, "/");
		strcat(path,"2.");
		strcat(path, filename);

		FILE *fput4;
   		fput4 = fopen( path , "w+" );


		// Writing first chunk into file
		received = received-1;
		fwrite(box[1] , 1 , received , fput4);
		fclose(fput4);



                }



            }

/************** End PUT ********* End PUT ************* End PUT ***************************************************/

/************** GET ****** GET ******************** GET ************************************************************/

            else if (strcmp(method, "GET") == 0)
            {

		DIR *gDir;
    		struct dirent *gDirent;
		gDir = opendir(currentDirectory);
		while ((gDirent = readdir(gDir)) != NULL)
    		{
			if (strstr((gDirent->d_name), filename) != NULL)
			{
				
				memset(getPath, 0, sizeof(getPath));
				strcpy(getPath, currentDirectory);
				strcat(getPath, "/");
				strcat(getPath, (gDirent->d_name));

				FILE *fget;
   				fget = fopen( getPath , "r" );
				fseek(fget, 0, SEEK_END);
				len3 = ftell(fget);
				rewind(fget);
				fread(getChunk[serverNumber], 1, len3, fget);
				send(cli, getChunk[serverNumber], len3+1, 0);
				fclose(fget);
				break;

			}
			
    		}		


                //printf ("command received: GET");
        	//send(cli, method, strlen(method), 0);

            }


/*********************End GET ************* End GET ************** End GET*********************************************/

/****************** LIST  *************** LIST **************** LIST **************************************************/

            else if (strcmp(method, "LIST") == 0)
            {

                //printf("command received: LIST");
        	//send(cli, method, strlen(method), 0);

		strcpy(listBuffer, serverName);
		strcat (listBuffer, " server contains:  ");
		DIR *pDir;
    		struct dirent *pDirent;
		pDir = opendir(currentDirectory);
		while ((pDirent = readdir(pDir)) != NULL)
    		{
        		strcat(listBuffer, pDirent->d_name);
			strcat(listBuffer, "  ");
    		}

		send(cli, listBuffer, strlen(listBuffer), 0);

            }




            else
            {
                printf("invalid command!");
            }
        }


    }

    close(cli);
    close(sock);
}


