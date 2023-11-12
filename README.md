# TTTicket - Concert Ticket Booking Website

## Quickstart Guide
- Clone the repository
- Make sure your file directory is on the same level as pom.xml + jdk is configured correctly
- run the cmd in command prompt: <mvnw spring-boot:run> [for Windows] or </.mvn spring-boot:run> [for Linux/MacOSX] (without the <>)
![Screenshot 2023-11-12 213301](https://github.com/notinctrl/CS203-Project/assets/110706158/44208e29-4d62-442c-92c8-3a9ec067bda3)
<p align="center">Console should look like this after successful boot up. "Tomcat started on port(s): [port number]" will need to be typed into browser later.</p>
- Open up the browser and type "http://localhost:[port number]/index" to access the main page
- This version works on Windows, MacOSX, with Chrome/Safari browsers.

![Screenshot 2023-11-12 213704](https://github.com/notinctrl/CS203-Project/assets/110706158/f45cd796-fb14-4d78-a045-f87a71f08925)

## Account Credentials
**NOTE: if you would like to simulate users buying tickets from each other via marketplace, load another instance of the website on Incognito Mode.**
3 accounts have been premade for your use:
1. **Username:** admin **Password:** goodpassword            -> has admin privileges (able to access list of users/sensistive information)
2. **Username:** normaluser **Password:** goodpassword       -> has user role
3. **Username:** normalbuyeruser **Password:** goodpassword  -> has user role

## Key Features
1. **Login feature**              : Buy and store your tickets from your favourite concerts. (Please use the above login credentials to access the website's full features.)
![Screenshot 2023-11-12 214829](https://github.com/notinctrl/CS203-Project/assets/110706158/c8f7e49d-9e62-48e3-80c4-8c1ae9cda4f3)
2. **Browse Concerts**            : Lookup and read about a specific concert.
![Screenshot 2023-11-12 213727](https://github.com/notinctrl/CS203-Project/assets/110706158/0240ee51-5d2b-4d0e-92cd-af41c812274f)
3. **Select your seating sector** : Check each concert's sector on how many seats are left in each sector. (login required)
![Screenshot 2023-11-12 213813](https://github.com/notinctrl/CS203-Project/assets/110706158/7c7f1fe8-91df-4e98-92dd-8c75b9ef5036)
4. **Select your seat**           : Select a seat based on the seating plan. Make your choice based on the simulated view! (login required)
![Screenshot 2023-11-11 151643](https://github.com/notinctrl/CS203-Project/assets/110706158/f154e376-244e-48fe-97e0-0c1b7e2b4911)
5. **View Shopping Cart Tickets** : Cart your tickets, and browse for some more concerts, no need to checkout right away! When you are done, click to checkout! (login required)
![Screenshot 2023-11-12 215034](https://github.com/notinctrl/CS203-Project/assets/110706158/29b612c2-1d2a-4767-9a58-b5a59482ee68)
6. **View Purchased Tickets**     : Your checked-out tickets will appear here. View your secure, animated entrance QR code here or add your ticket to the Marketplace! (login required)
![Screenshot 2023-11-12 215059](https://github.com/notinctrl/CS203-Project/assets/110706158/79b9d5b2-46b7-47ac-a426-f6a06572f342)
7. **Marketplace**                : The official platform for user-sold tickets! Browse and purchase tickets placed by your fellow users, or remove your Marketplace tickets if you change your mind.
![Screenshot 2023-11-12 215125](https://github.com/notinctrl/CS203-Project/assets/110706158/e995c005-faa5-42f1-a55f-a039c8bdd1c8)
![Screenshot 2023-11-12 215138](https://github.com/notinctrl/CS203-Project/assets/110706158/48e4fd64-309f-4905-80aa-b85df1753e3c)
