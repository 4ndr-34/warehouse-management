Application Overview
Warehouse Management
A warehouse would like to manage its inventory. For this they need a new system which should provide the following functionalities.

•	(DONE) Basic login/logout functionality for all users. User roles are as follows: 
o	CLIENT
o	WAREHOUSE_MANAGER
o	SYSTEM_ADMIN

•	CLIENT users should have the following functionalities:
o	(DONE)Can create a new order for items in the warehouse.
	(DONE) An order may have different statuses during its lifecycle: 
•	CREATED
•	AWAITING_APPROVAL
•	APPROVED
•	DECLINED
•	UNDER_DELIVERY
•	FULFILLED
•	CANCELED
o	(DONE) Update the order if the status is CREATED/DECLINED
o	(DONE) Add new items to the order
o	(DONE) Remove items from the order
o	(DONE) Modify the quantity of the items in the order
o	(DONE) Cancel his order if the status is not FULFILLED, UNDER_DELIVERY or CANCELED
o	(DONE) Submit his order if the status is CREATED or DECLINED. If the order is successfully submitted then the status becomes AWAITING_APPROVAL
o	(DONE) View his orders and filter them by status.
o	Bonus: Users can request a new password

•	WAREHOUSE_ MANAGER users should have the following functionalities:
o	(DONE) View all orders and filter them by status. 
	The list is not detailed and should only contains basic information. 
	Orders are sorted by their submission date starting from the newest.
o	(DONE)View detailed information about a specific order.
o	(DONE) Approve/Decline an order which is awaiting approval.
	Status should change accordingly.
	If the order is declined then a reason can be submitted.
o	(DONE) Manage items in the inventory. (Basic CRUD operations on items)
	Each item is packed in a cuboid shaped box and has a specific volume
o	Schedule a delivery for approved orders
	Deliveries are all done within a day.
o	(DONE) When scheduling a delivery, the manager must select a date and 1 or more trucks.
	(DONE) A truck can only complete one delivery a day and can carry a specific volume of items.
	(DONE) Weekends are off as the truck drivers are not working.
	(DONE) When the delivery is scheduled the status is set to UNDER_DELIVERY and the item quantity is updated.
	(WORKING ON IT) Manager can view all days on which a specific order can be scheduled for delivery based on the restrictions defined above. The period can be defined by the admin, e.g. for the upcoming 3 days) however the period is restricted to at most 30 days.
o	(DONE) Manager can also manage the trucks in the system. (Basic CRUD operations for trucks)
o	(WORKING ON IT) A cronjob is triggered on a daily basis which checks all scheduled deliveries against the current date and sets the status to FULFILLED if the date is reached.

•	SYSTEM_ADMIN users should have the following functionalities:
o	(DONE) Basic CRUD operations for the users. 
o	Define the period for which the managers can view orders.

•	(DONE) Order should have the following fields: 
o	Order Number (Generated automatically)
o	Submitted Date
o	Status
o	Items
o	Item
o	Requested Quantity
o	Deadline Date

•	(DONE) Trucks should have the following fields:
o	Chassis number (Unique and entered manually)
o	License Plate (Unique and entered manually)
o	Container volume

•	(DONE) Inventory Items should have the following fields:
o	Item Name
o	Quantity
o	Unit Price
o	Package Volume

















Technologies:
•	The application should be well structured.
•	The following technologies/frameworks are mandatory:
o	Spring 3 for the service layer (Spring MVC and Spring Boot)
o	Spring Data JPA / Hibernate for the persistence layer
o	MySQL (or any other RDBMS) |
o	Spring Security, implement JWT
o	Maven for the dependencies between the modules besides the other proprietary libraries.
o	Logging possibility via log4j.
o	Swagger.
•	Optional: Module for the Frontend having also the client for the Rest Web Services using Angular.
•	Bonus: JUnit Tests
•	Note: Prepare the Postman requests for the endpoints if Ul is not implemented.
