# Boat Overview App (Java/Spring Boot)

Welcome to the Boat Overview App! This is a simple application that allows users to manage and view a list of boats. It includes authentication/authorization, CRUD operations for boats, and detailed views for each boat.

Use Cases:
1. User Login and Redirect
    When a user opens the app, they are presented with a login screen.
    The user needs to log in using valid credentials.
    After a successful login, the user is redirected to the overview page (UC2).

2. Boat Overview
    Once logged in, the user can see a list of all boat resources.

3. Manage Boats
    Users have the ability to add, update, or delete boat entries.
    Boats must have at least the following attributes: Name and Description.
    The input data is validated for accuracy.

4. Boat Detail View
    Users can select a boat item in the list to view detailed information about it.

## Security 

    Implementation authentication and authorization for the app.
    Only authenticated users can access resources.

	Use the following credentials for testing:
        	User: user, Password: password
        	User: admin, Password: password

## CRUD Endpoint for Boats

    Implementation CRUD (Create, Read, Update, Delete) endpoints to manage boat resources.

    Each boat should include attributes: Name and Description.
    Validate input data to ensure accuracy and completeness.

### Endpoints for testing:
	Display list: "/"
        Add boat: "/addBoat"
        Update boat: "/updBoat/{id}"
        Delete boat: "/deleteBoat/{id}"
        Clear boat list: "/clearList"
        View boat details: "/getBoat/{id}"
    
Use Postman to test the API requests. 
Autorization: 
	Type = Basic Auth;
	Username = user;
	Password = password;
