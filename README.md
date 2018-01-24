# auctioneero
A small server-client app to showcase the use of Hibernate, Jersey, JAXB, and MySQL. 

Auctioneero is a proof of concept project that acts as an auction website where users can upload items to sell and bid on items that others have put up. The main goal of this project is to learn about developing a scalable web application using these technologies. 

# Tech 
## General Structure
There are 3 main parts:
1. The client side
    * Either a webpage or an app
2. Load balancer
    * May or may not be implemented; the idea here is to allow for scaling out to many server apps. Don't want that kind of bottleneck in real life!
3. The server side
    * Handles the business logic by via a RESTful API. This is important so we can decouple the server and client, allowing for connections from multiple difference sources.


## [Hibernate](http://hibernate.org/) & [MySQL](https://www.mysql.com/)
Hibernate will be used as the ORM (Object-Relational Mapping) tool, bridging the gap between the database and in-memory objects. This is to make it more fluid to work with the database in code, having to worry much less about parsing output from raw SQL queries. The RDBMS (Relational Database Management System) that will be used is MySQL, persisting objects such as user and auction item info.

## [Jersey](https://jersey.github.io/)
Jersey is the framework that will be used to help implement the RESTful service. As mentioned above, this paradigm will decouple our server and client, allowing for connections from multiple different sources (eg. both a phone app and a webpage) without changing the backend.

## [JAXB](https://docs.oracle.com/javase/tutorial/jaxb/intro/index.html)
JAXB is a framework that is similar to Hibernate in that it maps in-memory objects to a persisted state, but in this case it's via XML instead of a database. I may end up using this for things like configuration, for example if I want to have a data driven 'Categories' enum.

