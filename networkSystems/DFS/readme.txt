Aim: create a distributed file system for reliable and secure file storage.

One client DFC (Distributed File Client) is uploading and downloading files onto and from 4 servers DFS1, DFS2, DFS3 and DFS4. (DFS means Distributed File Server.) In order to be able to run this assignment in a single machine. The DFS servers are all running locally with different port numbers	from 10001 to 10004. When DFC want to upload a file to the	4 DFS servers, it first split the file in to 4 equal length pieces P1, P2, P3, P4 (a small length difference is acceptable if the total length can not be divided by 4). Then the DFC group the 4 pieces in to 4 pairs (P1, P2), (P2, P3), (P3, P4), (P4, P1). At last the DFC uploads them onto 4 DFS servers. So now the file has redundancy, 1 failed server will not affect the integrity of the file.

"1.txt" and "2.txt" are sample texts.

Server details are read from "dfs.conf" and Client details are read from "dfc.conf".