#TTTicket - Concert Ticket Booking Website

##Quickstart Guide
- Clone the repository
- Make sure your file directory is on the same level as pom.xml + jdk is configured correctly
- run the cmd: <mvnw spring-boot:run> [for Windows] or </.mvn spring-boot:run> [for Linux/MacOSX] (without the <>)
- Open up the browser and type http://localhost:[port number you are configured to]/index to access the main page
- This version works on Windows, MacOSX, with Chrome/Safari browsers.

##Account Credentials
NOTE: if you would like to simulate users buying tickets from each other via marketplace, load another instance of the website on Incognito Mode.
3 accounts have been premade for your use:
1. Username: admin Password: goodpassword            -> has admin privileges (able to access list of users/sensistive information)
2. Username: normaluser Password: goodpassword       -> has user role
3. Username: normalbuyeruser Password: goodpassword  -> has user role

##Key Features
1. Login feature              : Buy and store your tickets from your favourite concerts. (Please use the above login credentials to access the website's full features.)
2. Browse Concerts            : Lookup and read about a specific concert.
3. Select your seating sector : Check each concert's sector on how many seats are left in each sector. (login required)
4. Select your seat           : Select a seat based on the seating plan. Make your choice based on the simulated view! (login required)
5. View Shopping Cart Tickets : Cart your tickets, and browse for some more concerts, no need to checkout right away! When you are done, click to checkout! (login required)
6. View Purchased Tickets     : Your checked-out tickets will appear here. View your secure, animated entrance QR code here or add your ticket to the Marketplace!
7. Marketplace                : The official platform for user-sold tickets! Browse and purchase tickets placed by your fellow users, or remove your Marketplace tickets if you change your mind.
