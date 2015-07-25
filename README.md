# Parotte
This web application is a Twitter-like application written in Play 
Framework 2.4.x.  It was prepared for the Advanced Web Technologies 
Unit at the University of New England (COMP391) Trimester 2 2015.

## Terminology
Object names are based around the phrase 'pithy bon mot'.  In this 
application, a posted statement is referred to as a `BonMot` while
a hashtag is known as a `Pith`.

## Usage
Implementation notes and general features of the application are listed
below to assist in the testing, review and marking process.

### Ports
The following ports are the output of the `my_tomcat_ports` command on 
turing.  

Environment | Port
------------|-----
Dev         |	`52084`   
Test        | `51084`    
Prod        |	`50084` (to be marked)  

### Commands
The following shell commands are available in the root directory of the 
application to facilitate startup / shutdown procedures.

`./debug`       Starts the application in `~run` mode on port `52804`
`./startup`	    Stops/Starts the application on port `50084` and evolves h2  
.`/shutdown`	  Stops the application  

## General Notes

### Browser and Platform compatibility
The application uses both [jQuery](https://jquery.com/) and Twitter 
[bootstrap](https://getbootstrap.com) components in order
to target 'A' grade browsers across all modern platforms.

*Desktop*
TODO: Pics

*Mobile*
TODO: Pics

### Landing Page
The landing page (also known as the home page) provides a list of the 
latest BonMots sorted in descending order.  

### BonMot Display
The rendering of a BonMot has three stages:

- Scala template is rendered 
  - the username is presented as a hyperlink to the user's profile
  - timestamp is provided raw
  - bonmot and piths are provided raw
- [Moment.js](https://momentjs.com) is used to format the date time into 'x Ns ago' format
- Regex / Javascript replace is used to format the piths into clickable links

_Note: This process may change in the future when we move to client side React.js_

### Navigation Bar 
The top nav bar, available on all pages, will collapse on mobile devices 
into a drop down menu.  The drop down menu is triggered when the user 
clicks the hamburger icon in the upper right hand corner of the browser window.

The menu provides links to the following pages:

- Home
- Search
- Register‡
- Login‡
- Admin‡‡
- Logout‡‡‡ 

‡   Hidden when logged in  
‡‡  Only visible to the admin user  
‡‡‡ Only visible when logged in  

### Search Page
Users can search by one of two ways:

- Search for users by entering their user name prefixed by the '@' symbol
- Search for piths by entering the pith prefixed by the '#' symbol

Search results will be displayed below in list format.

TODO: Pic

The user can also navigate to the search page by clicking on any of the hyperlinked
Piths in any BonMot.

_Note: Pagination is currently not enabled and is limited to the last 25 entries_

### Registration
Users can register by choosing a unique username, email and password.  No email confirmation 
or activation is neccessary.  The password is encrypted using salted blowfish encryption 
provided by [jBCrypt](http://www.mindrot.org/projects/jBCrypt/).

Once the registration has been processed by the server the user is immediately 
redirected to the login page to 
start using the application.

Note that usernames must be unique, and no email address can be used more than once.  The user
will receive a warning if they violate any of the validation logic.

TODO: Pic

### Login Page
Users may login with the username / password they have used in registration.

TODO: pic

Error messages are displayed where login details cannot be determined to be authentic.

TODO: pic

### Admin Mode
The admin account admin@admin.com / password allows the user to access the 
administrative panel where they can view the list of currently active users.

In addition, the admin can ban a user which will force a logout on the next
HTTP request.

TODO: pic

### Logout
Users can log out at any time using the logout button in the upper right hand corner.

Note that after a period of inactivity, users sessions will expire and they will 
officially be logged out on the next HTTP request.
