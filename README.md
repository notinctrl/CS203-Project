# TTTicket - Concert Ticket Booking Website

## Quickstart Guide
- Clone the repository
- Make sure your file directory is on the same level as pom.xml + jdk is configured correctly
- run the cmd in command prompt: <mvnw spring-boot:run> [for Windows] or </.mvn spring-boot:run> [for Linux/MacOSX] (without the <>)
![Screenshot 2023-11-12 213301](https://github.com/notinctrl/CS203-Project/assets/110706158/844873c7-2a77-4817-93c1-b2b6055b1f0f)
<p align="center">Console should look like this after successful boot up. "Tomcat started on port(s): [port number]" will need to be typed into browser later.</p>
- Open up the browser and type "http://localhost:[port number]/index" to access the main page
- This version works on Windows, MacOSX, with Chrome/Safari browsers.

![Screenshot 2023-![Screenshot 2023-11-12 213704](https://github.com/notinctrl/CS203-Project/assets/110706158/6d13ef85-decf-4e1b-ad4c-a5cbb10f2991)

## Account Credentials
**NOTE: if you would like to simulate users buying tickets from each other via marketplace, load another instance of the website on Incognito Mode.**
3 accounts have been premade for your use:
1. **Username:** admin           **Password:** goodpassword  -> has admin privileges (able to access list of users/sensistive information)
2. **Username:** normaluser      **Password:** goodpassword  -> has user role
3. **Username:** normalbuyeruser **Password:** goodpassword  -> has user role

## Key Features
1. **Login feature**              : Buy and store your tickets from your favourite concerts. (Please use the above login credentials to access the website's full features.)
![Screenshot 2023-11-12 214829](https://github.com/notinctrl/CS203-Project/assets/110706158/b3026c54-fe2c-4950-85ea-0eb780e76d9f)
2. **Browse Concerts**            : Lookup and read about a specific concert.
![Screenshot 2023-11-12 213727](https://github.com/notinctrl/CS203-Project/assets/110706158/35019e31-98fd-443b-9e58-06831847f756)
4. **Select your seating sector** : Check each concert's sector on how many seats are left in each sector. (login required)
![Screenshot 2023-11-12 213813](https://github.com/notinctrl/CS203-Project/assets/110706158/90217024-1bc4-4932-ba37-955911e11742)
5. **Select your seat**           : Select a seat based on the seating plan. Make your choice based on the simulated view! (login required)
![Screenshot 2023-11-11 151643](https://github.com/notinctrl/CS203-Project/assets/110706158/894d57ff-c44c-4abe-95db-2bbf972d6370)
6. **View Shopping Cart Tickets** : Cart your tickets, and browse for some more concerts, no need to checkout right away! When you are done, click to checkout! (login required)
![Screenshot 2023-11-12 215034](https://github.com/notinctrl/CS203-Project/assets/110706158/803e9192-7bca-448c-908f-e0a499a3a90f)
7. **View Purchased Tickets**     : Your checked-out tickets will appear here. View your secure, animated entrance QR code here or add your ticket to the Marketplace! (login required)
![Screenshot 2023-11-12 215059](https://github.com/notinctrl/CS203-Project/assets/110706158/9dbfd0e6-1396-4479-a167-08e03c634cc4)
8. **Marketplace**                : The official platform for user-sold tickets! Browse and purchase tickets placed by your fellow users, or remove your Marketplace tickets if you change your mind.
![Screenshot 2023-11-12 215125](https://github.com/notinctrl/CS203-Project/assets/110706158/25bdf30f-94c2-4145-a58c-a8bd15b9e4ed)
![Screenshot 2023-11-12 215138](https://github.com/notinctrl/CS203-Project/assets/110706158/3b78e6ad-63ac-45d4-a25b-2d6b52b660eb)
