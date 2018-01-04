# Triple-DES
Encryption and Decryption using Triple DES
The purpose of this project is to build a client/server application which uses symmetric encryption to exchange a new key. The communication between the client and the server is to be implemented as sockets.
•	The client application will take a plaintext message (m) from the user, encrypt the message using a TripleDES key (assuming an existing key, k), and then send the ciphertext over the socket to the server.
•	The server will read the ciphertext from the socket, get the secret key k from the user, and then decrypt the ciphertext. The decrypted message (dm) should be printed as part of the screen output.
•	The server then generates a new TripleDES key (nk), encrypts dm by using nk, encrypts nk by using the existing symmetric key (k), and then sends the encrypted new key and the encrypted dm over to the client.
•	Once the client receives the encrypted dm and the encrypted nk, it first decrypts the encrypted nk to get the new symmetric key, and then uses the new key to decrypt the encrypted dm. The outcome from the decryption of dm should be compared against the original plaintext message (m), which was originally sent over to the server. Print appropriate message depending on the result of the comparison.
