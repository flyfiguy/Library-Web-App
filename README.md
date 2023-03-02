# library-app
 Library web application. React front end. Spring Boot backend.

Frontend
-Visual Studio Code 1.75.0
-React 16.17.0
-node.js 16.17.0
-npm 8.15.1
-tsc 4.7.4
-Okta 6.4.3

Security
- Sign-in Method
	Okta
		-OIDC (OpenID Connect)
			-Use with Okta Sign-in Widget (6.3.3)
- Application Type
	Single-Page Application
- General
	App integration name: Library App
	Grant Type: Authorization Code
	Sign-in redirect URI: http://localhost:3000/login/callback
	Sign-out redirect URI: http://localhost:3000
	Trusted Origins - Base URI: http://localhost:3000
	Assignments - Controlled Access: Allow everyone in your organization to access
	Assignments - Enable immediate access - Enable immediate access with Federation Broker Mode
- Dev URL: 
- Cleint ID:
- Test user: testuser@email.com
- Test password: 


Backend
-Spring boot 3.0.2
-Lombok
-Rest Repositories
-Spring Data JPA
-Java 19.0.2
-Maven 3.8.7
-MySQL DB 8.0.31

Running the application
1) Start MySQL server
2) Start backend Java/Spring Boot Application
3) Start frontend React web application

RESTful APIs

PUT - http://localhost:8080/api/books/secure/checkout?bookId=1
	- Checkout a book. Reduces the copies available count.
	- Any user can only check out the same book once.

Success
{
    "id": 2,
    "title": "Become a Guru in JavaScript",
    "author": "Luv, Lena",
    "description": "Pellentesque varius aliquam lacus quis rhoncus. Nam a dui lectus. Vestibulum libero elit, ultricies sit amet sagittis eu, molestie at velit. Donec tincidunt tempus magna, quis facilisis libero elementum non. Sed velit lacus, laoreet sed augue fermentum, sagittis convallis metus. Sed nec est at massa venenatis aliquet. Donec pretium interdum fringilla. Sed ornare tellus enim, a tincidunt libero dictum vitae. Proin bibendum posuere dui. Donec sagittis neque massa, sed semper nulla vehicula at.",
    "copies": 15,
    "copiesAvailable": 14,
    "category": "FE",
    "img": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZYAAAJqCAYAAAAIQG...
}

Error
{
    "timestamp": "2023-02-16T17:09:27.288+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/books/secure/checkout"
}

GET - http://localhost:8080/api/books/secure/ischeckedout/byuser?bookId=1
	- Test to see if a particular book is currently checked out by a particular user already.
	- NOTE: need to add the user email as a query param still
	- Returns "true" if user already has a particular book checked out otherwise returns "false"
