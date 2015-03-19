In this project, we were supposed to implement an online shopping project with negotiation feature. Customers can put their products online and write a initial price for their products. Customers can negotiate on that prices to buy the item for a lower price. Sellers can choose either continue the negotiation period by lowering the price or rejecting the customer. Multiple customers can negotiate for the same product but they don’t see each other’s prices or negotiation history. Sellers can choose any customer they want. Usage details will be explained later in more detail in user interface section of this report.

## Technologies Used
#### Maven:
Since we used too many dependencies in this project, we needed a dependency management tool for this purpose. Otherwise, our submission would have many jar files and its size would be bigger. In addition, adding jar files to projects decreases the performance of source version control systems such as CVS, SVN, etc.

#### SVN:
SVN was our choice as a source version control system. We met only once in whole project lifetime. We used SVN to commit and merge our work. We created a project plan and both of us worked from different places and SVN helped us a lot. Otherwise, merging code would be really painful.

#### Java Servlets:
Both business logic and input validations are done in Servlets. We have separated servlets for different tasks. Because of our limited time, we divided our system into only three layers, Controller layer which consists of servlets, DAO layer which sends SQL queries to our DB and returns results as Java objects, and View layer which is completely JSP.

#### JSP & JSTL:
We used JSP to display our content to our users. As a principle, we didn’t write Java code even a single line in JSP files. We used JSTL(Java Standard Tag Library) to access, iterate, and control our Java models. We separated common HTML code such as head tag and navigation menu into different JSP files so that we could include them in all pages.

#### Bootstrap: 
We didn’t write CSS code to make our design look pretty. Instead, we used bootstrap CSS library which already looks pretty.

#### MySQL: 
As a database management system, MySQL was our choice. We wrote many relational SQL queries where a result coming from one table depends on another result coming from another table. Thus, what we really need was a relational database.
